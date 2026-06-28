# Anime/Manga List Sorting — Design

**Date:** 2026-06-28
**Module:** `myanimelist`
**Status:** Approved (pending spec review)

## Goal

Let the user sort their anime and manga lists by one of four fields, in
ascending or descending order. Sorting applies client-side to the already-loaded
list and integrates with the existing filter/search pipeline.

## Background

Both list pages currently sort with a hard-coded
`.sortedByDescending { it.anime.mean }` (resp. `it.manga.mean`) inside the
ViewModel's `applyFilter()`:

- `ui/animelist/AnimeListViewModel.kt:169-172`
- `ui/mangalist/MangaListViewModel.kt:160-162`

The anime and manga list features are structurally near-identical (ViewModel,
`*State.kt`, `*Page.kt`, filter chip row). Each list already holds the full
result set in a `cached` list and re-derives the visible list via `applyFilter()`
on every filter/search/sort change. Sorting slots into that same pipeline with no
new network calls.

## Sort fields

All four fields are backed by existing model data on **both** anime and manga
(verified in `MalAnime.kt`, `MalManga.kt`, `MalAnimeListStatus.kt`,
`MalMangaListStatus.kt`):

| Field (enum)  | Display       | Anime selector            | Manga selector            | Default dir | Nulls |
|---------------|---------------|---------------------------|---------------------------|-------------|-------|
| `Rating`      | "Rating"      | `anime.mean: Float?`      | `manga.mean: Float?`      | Descending  | last  |
| `MyScore`     | "My score"    | `listStatus.score: Int?`  | `listStatus.score: Int?`  | Descending  | last  |
| `ReleaseDate` | "Release date"| `anime.startDate: String?`| `manga.startDate: String?`| Descending  | last  |
| `FinishDate`  | "Date finished"| `listStatus.finishDate: String?` | `listStatus.finishDate: String?` | Descending | last  |

Notes:
- `startDate` / `finishDate` are ISO `YYYY-MM-DD` strings (sometimes partial,
  e.g. `YYYY` or `YYYY-MM`). ISO date strings sort correctly lexicographically,
  so no date parsing is required.
- **Nulls always sort last**, regardless of direction. Entries with no rating /
  no personal score / no date never crowd the top of the list. This is the one
  intentional asymmetry vs. a naive comparator flip.

## Components

### 1. `SortOption.kt` (new) — `ui/home/`

Lives in `ui/home/` alongside `MalTab`, since it is shared by both list features.

```kotlin
enum class SortField(val displayName: String) {
    Rating("Rating"),
    MyScore("My score"),
    ReleaseDate("Release date"),
    FinishDate("Date finished"),
}

enum class SortDirection { Ascending, Descending }

data class SortOption(
    val field: SortField,
    val direction: SortDirection,
) {
    companion object {
        val Default = SortOption(SortField.Rating, SortDirection.Descending)
    }
}

// Default direction applied when switching TO a field.
fun SortField.defaultDirection(): SortDirection = SortDirection.Descending
```

A generic, nulls-last comparator helper usable by both ViewModels:

```kotlin
fun <T, R : Comparable<R>> sortedWithOption(
    items: List<T>,
    direction: SortDirection,
    selector: (T) -> R?,
): List<T> = items.sortedWith(
    Comparator { a, b ->
        val ka = selector(a); val kb = selector(b)
        when {
            ka == null && kb == null -> 0
            ka == null -> 1          // nulls last
            kb == null -> -1         // nulls last
            direction == SortDirection.Ascending -> ka.compareTo(kb)
            else -> kb.compareTo(ka)
        }
    }
)
```

The hand-written `Comparator` is used (not `compareBy(...).reversed()`) precisely
because `reversed()` flips null ordering; we want nulls last in **both**
directions.

### 2. ViewModel changes — `AnimeListViewModel`, `MangaListViewModel`

Each gets, mirrored:

```kotlin
private val _sortOption = MutableStateFlow(SortOption.Default)
val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

fun onSortSelected(field: SortField) {
    val current = _sortOption.value
    _sortOption.value = if (current.field == field) {
        current.copy(
            direction = if (current.direction == SortDirection.Descending)
                SortDirection.Ascending else SortDirection.Descending
        )
    } else {
        SortOption(field, field.defaultDirection())
    }
    applyFilter()
}
```

In `applyFilter()`, replace the hard-coded sort. Anime example:

```kotlin
val sort = _sortOption.value
val sorted = when (sort.field) {
    SortField.Rating      -> sortedWithOption(filtered, sort.direction) { it.first.mean }
    SortField.MyScore     -> sortedWithOption(filtered, sort.direction) { it.second?.score }
    SortField.ReleaseDate -> sortedWithOption(filtered, sort.direction) { it.first.startDate }
    SortField.FinishDate  -> sortedWithOption(filtered, sort.direction) { it.second?.finishDate }
}
val cardData = sorted.map { AnimeCardData(it.first, it.second) }
```

Manga is identical with `manga`/`MangaCardData` selectors. The
`Content(emptyList())` vs `Empty` logic at the end of `applyFilter()` is
unchanged.

### 3. `SortButton.kt` (new) — `ui/home/`

Mirrors `UpcomingFilterButton`: a 32.dp circular icon button, selected/unselected
styling via `AppThemeExtended.colorScheme`. Uses a local `ic_sort` vector drawable
(see Assets). Placed as the **first** item in the existing filter chip `LazyRow`
in each list page (before the upcoming-filter button), so sort is the leading
control.

### 4. `SortBottomSheet.kt` (new) — `ui/home/`

A `ModalBottomSheet` (same idiom as `EditAnimeBottomSheet`), shared by both list
pages. Lists the four `SortField` entries as rows. The currently active field
shows a trailing up/down arrow indicating direction; inactive fields show no
arrow. Tapping a row invokes `onSortSelected(field)` and keeps the sheet open so
the user can flip direction with a second tap; the sheet dismisses on scrim tap /
back. Signature:

```kotlin
@Composable
fun SortBottomSheet(
    current: SortOption,
    onSortSelected: (SortField) -> Unit,
    onDismiss: () -> Unit,
)
```

### 5. Page wiring — `AnimeListPage.kt`, `MangaListPage.kt`

- Collect `sortOption` from the ViewModel.
- Add a local `rememberSaveable` boolean (or a nullable state) controlling sheet
  visibility — kept in the composable, not the ViewModel, consistent with how
  the edit sheet is triggered by a ViewModel field but the sheet's own
  presentation is local.
- Add `SortButton` to `AnimeFilterChipsRow` / manga equivalent, opening the sheet.
- Render `SortBottomSheet` when visible.

## Assets

The repo's cursor rule `no-material-icons-extended` forbids
`material-icons-extended`. Add a local vector drawable
`myanimelist/src/main/res/drawable/ic_sort.xml` (standard Material "sort" path).
Arrow indicators in the sheet reuse existing chevron/arrow drawables if present;
otherwise add `ic_arrow_upward.xml` / `ic_arrow_downward.xml` as local vectors.

## Data flow

```
SortButton tap
  → sheet opens (local composable state)
  → row tap → viewModel.onSortSelected(field)
      → updates _sortOption (flip dir if same field, else field + default dir)
      → applyFilter() re-derives visible list from `cached`
      → _listState emits new Content(sorted)
  → LazyColumn recomposes in new order
```

No network calls. Operates on the already-loaded `cached` list.

## Testing

No-TDD per project convention; verify by build + manual run. Sanity checks:

- Default launch sorts by Rating desc (matches today's behavior).
- Switching field applies default desc; re-tapping same field flips to asc.
- Entries with `null` rating/score/date appear last in both directions.
- Sort composes with status filter, upcoming filter, and search query.
- Works identically on anime and manga tabs.

## Out of scope (YAGNI)

- DataStore persistence — sort resets to default each launch, consistent with the
  existing non-persisted status/search/upcoming filters.
- Title (A–Z) sort — not requested.
- New per-entry "date added / last updated" sort — not present in the API model.
