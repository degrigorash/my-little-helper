# Anime/Manga List Sorting Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Let the user sort their anime and manga lists by community rating, personal score, release date, or finish date, in either direction, via a sort button + bottom sheet in the existing filter row.

**Architecture:** Sorting is a client-side operation slotted into each list ViewModel's existing `applyFilter()` pipeline, replacing the hard-coded `mean`-descending sort. A shared `SortOption` model + nulls-last comparator lives in `ui/home/`. The UI adds a circular `SortButton` (mirroring `UpcomingFilterButton`) that opens a shared `SortBottomSheet` (mirroring `EditAnimeBottomSheet`'s `ModalBottomSheet` idiom). State is in-memory only; default is Rating descending (preserving current behavior).

**Tech Stack:** Kotlin, Jetpack Compose, Material3, Hilt, StateFlow.

**Project convention:** No TDD ([feedback_no_tdd.md](../../../.claude/projects)). Tasks follow implement → compile-verify → commit. There is no unit-test harness for these UI features; verification is a successful module compile via `./gradlew :myanimelist:compileDebugKotlin` plus the manual checks in the final task.

**Reference files (patterns to follow):**
- `myanimelist/src/main/java/com/grig/myanimelist/ui/home/UpcomingFilterButton.kt` — circular icon button style
- `myanimelist/src/main/java/com/grig/myanimelist/ui/animeedit/EditAnimeBottomSheet.kt` — `ModalBottomSheet` idiom
- `myanimelist/src/main/res/drawable/ic_upcoming.xml` — vector drawable format
- Spec: `docs/superpowers/specs/2026-06-28-list-sorting-design.md`

---

## File Structure

**Create:**
- `myanimelist/src/main/java/com/grig/myanimelist/ui/home/SortOption.kt` — sort enums, `SortOption`, nulls-last comparator helper. Shared by both list features.
- `myanimelist/src/main/java/com/grig/myanimelist/ui/home/SortButton.kt` — circular sort icon button.
- `myanimelist/src/main/java/com/grig/myanimelist/ui/home/SortBottomSheet.kt` — modal sheet listing sort fields. Shared by both list pages.
- `myanimelist/src/main/res/drawable/ic_sort.xml` — sort icon vector.
- `myanimelist/src/main/res/drawable/ic_arrow_upward.xml` — ascending indicator vector.
- `myanimelist/src/main/res/drawable/ic_arrow_downward.xml` — descending indicator vector.

**Modify:**
- `myanimelist/src/main/java/com/grig/myanimelist/ui/animelist/AnimeListViewModel.kt` — add sort state + `onSortSelected`; replace hard-coded sort in `applyFilter()`.
- `myanimelist/src/main/java/com/grig/myanimelist/ui/mangalist/MangaListViewModel.kt` — same as anime.
- `myanimelist/src/main/java/com/grig/myanimelist/ui/animelist/AnimeListPage.kt` — collect sort state, add `SortButton` to chip row, render sheet.
- `myanimelist/src/main/java/com/grig/myanimelist/ui/mangalist/MangaListPage.kt` — same as anime.

---

## Task 1: Sort model + comparator

**Files:**
- Create: `myanimelist/src/main/java/com/grig/myanimelist/ui/home/SortOption.kt`

- [ ] **Step 1: Create `SortOption.kt`**

```kotlin
package com.grig.myanimelist.ui.home

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

/** Direction applied when switching to a field. All four fields default to descending. */
fun SortField.defaultDirection(): SortDirection = SortDirection.Descending

/**
 * Sorts [items] by [selector], with null keys always placed last regardless of [direction].
 * A hand-written comparator is used (not compareBy(...).reversed()) because reversed()
 * would flip null ordering; we want nulls last in both directions.
 */
fun <T, R : Comparable<R>> sortedWithOption(
    items: List<T>,
    direction: SortDirection,
    selector: (T) -> R?,
): List<T> = items.sortedWith(
    Comparator { a, b ->
        val ka = selector(a)
        val kb = selector(b)
        when {
            ka == null && kb == null -> 0
            ka == null -> 1
            kb == null -> -1
            direction == SortDirection.Ascending -> ka.compareTo(kb)
            else -> kb.compareTo(ka)
        }
    }
)
```

- [ ] **Step 2: Compile the module**

Run: `./gradlew :myanimelist:compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add myanimelist/src/main/java/com/grig/myanimelist/ui/home/SortOption.kt
git commit -m "feat: add sort model and nulls-last comparator"
```

---

## Task 2: Drawable assets

**Files:**
- Create: `myanimelist/src/main/res/drawable/ic_sort.xml`
- Create: `myanimelist/src/main/res/drawable/ic_arrow_upward.xml`
- Create: `myanimelist/src/main/res/drawable/ic_arrow_downward.xml`

The repo's `no-material-icons-extended` cursor rule forbids `material-icons-extended`, so these are local vector drawables. Paths are the standard Material icons. `#FFFFFF` fill matches the existing `ic_upcoming.xml` convention (tint is applied at the call site).

- [ ] **Step 1: Create `ic_sort.xml`**

```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M3,18h6v-2L3,16v2zM3,6v2h12L15,6L3,6zM3,13h9v-2L3,11v2z" />
</vector>
```

- [ ] **Step 2: Create `ic_arrow_upward.xml`**

```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M4,12l1.41,1.41L11,7.83V20h2V7.83l5.58,5.59L20,12l-8,-8 -8,8z" />
</vector>
```

- [ ] **Step 3: Create `ic_arrow_downward.xml`**

```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M20,12l-1.41,-1.41L13,16.17V4h-2v12.17l-5.58,-5.59L4,12l8,8 8,-8z" />
</vector>
```

- [ ] **Step 4: Compile the module (verifies drawables parse)**

Run: `./gradlew :myanimelist:compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 5: Commit**

```bash
git add myanimelist/src/main/res/drawable/ic_sort.xml \
        myanimelist/src/main/res/drawable/ic_arrow_upward.xml \
        myanimelist/src/main/res/drawable/ic_arrow_downward.xml
git commit -m "feat: add sort and direction-arrow drawables"
```

---

## Task 3: SortButton composable

**Files:**
- Create: `myanimelist/src/main/java/com/grig/myanimelist/ui/home/SortButton.kt`

Mirrors `UpcomingFilterButton.kt` exactly (32.dp circle, `AppThemeExtended.colorScheme`), but is never "selected" — it is a plain action button that opens the sheet, so it always uses the unselected (outlined) styling.

- [ ] **Step 1: Create `SortButton.kt`**

```kotlin
package com.grig.myanimelist.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.R

@Composable
fun SortButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_sort),
            contentDescription = "Sort",
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(name = "Sort Button")
@Composable
private fun SortButtonPreview() {
    AppTheme(darkTheme = false) {
        SortButton(onClick = {})
    }
}

@Preview(name = "Sort Button - Dark")
@Composable
private fun SortButtonDarkPreview() {
    AppTheme(darkTheme = true) {
        SortButton(onClick = {})
    }
}
```

- [ ] **Step 2: Compile the module**

Run: `./gradlew :myanimelist:compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add myanimelist/src/main/java/com/grig/myanimelist/ui/home/SortButton.kt
git commit -m "feat: add sort button composable"
```

---

## Task 4: SortBottomSheet composable

**Files:**
- Create: `myanimelist/src/main/java/com/grig/myanimelist/ui/home/SortBottomSheet.kt`

A `ModalBottomSheet` (same idiom as `EditAnimeBottomSheet`) listing all four `SortField` entries. The active field shows a trailing up/down arrow indicating direction; inactive fields show none. Tapping a row calls `onSortSelected(field)` and leaves the sheet open (so a second tap on the active field flips direction). Sheet dismisses on scrim tap / back via `onDismissRequest`.

- [ ] **Step 1: Create `SortBottomSheet.kt`**

```kotlin
package com.grig.myanimelist.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.grig.myanimelist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    current: SortOption,
    onSortSelected: (SortField) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = "Sort by",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            SortField.entries.forEach { field ->
                val isActive = field == current.field
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSortSelected(field) }
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = field.displayName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                        color = if (isActive) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    if (isActive) {
                        Icon(
                            painter = painterResource(
                                if (current.direction == SortDirection.Ascending)
                                    R.drawable.ic_arrow_upward
                                else R.drawable.ic_arrow_downward
                            ),
                            contentDescription = if (current.direction == SortDirection.Ascending)
                                "Ascending" else "Descending",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
```

- [ ] **Step 2: Compile the module**

Run: `./gradlew :myanimelist:compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add myanimelist/src/main/java/com/grig/myanimelist/ui/home/SortBottomSheet.kt
git commit -m "feat: add sort bottom sheet composable"
```

---

## Task 5: Wire sort into AnimeListViewModel

**Files:**
- Modify: `myanimelist/src/main/java/com/grig/myanimelist/ui/animelist/AnimeListViewModel.kt`

- [ ] **Step 1: Add imports**

Add to the import block at the top of the file (after the existing `com.grig.myanimelist...` imports):

```kotlin
import com.grig.myanimelist.ui.home.SortField
import com.grig.myanimelist.ui.home.SortOption
import com.grig.myanimelist.ui.home.SortDirection
import com.grig.myanimelist.ui.home.defaultDirection
import com.grig.myanimelist.ui.home.sortedWithOption
```

- [ ] **Step 2: Add sort state**

After the `_searchQuery` / `searchQuery` declaration block (around line 39-40), add:

```kotlin
    private val _sortOption = MutableStateFlow(SortOption.Default)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()
```

- [ ] **Step 3: Add `onSortSelected`**

After the `onSearchQueryChange` function (around line 88-91), add:

```kotlin
    fun onSortSelected(field: SortField) {
        val current = _sortOption.value
        _sortOption.value = if (current.field == field) {
            current.copy(
                direction = if (current.direction == SortDirection.Descending) {
                    SortDirection.Ascending
                } else {
                    SortDirection.Descending
                }
            )
        } else {
            SortOption(field, field.defaultDirection())
        }
        applyFilter()
    }
```

- [ ] **Step 4: Replace the hard-coded sort in `applyFilter()`**

Find this block (currently around lines 169-171):

```kotlin
        val cardData = filtered
            .map { AnimeCardData(it.first, it.second) }
            .sortedByDescending { it.anime.mean }
```

Replace it with:

```kotlin
        val sort = _sortOption.value
        val sorted = when (sort.field) {
            SortField.Rating -> sortedWithOption(filtered, sort.direction) { it.first.mean }
            SortField.MyScore -> sortedWithOption(filtered, sort.direction) { it.second?.score }
            SortField.ReleaseDate -> sortedWithOption(filtered, sort.direction) { it.first.startDate }
            SortField.FinishDate -> sortedWithOption(filtered, sort.direction) { it.second?.finishDate }
        }
        val cardData = sorted.map { AnimeCardData(it.first, it.second) }
```

(`filtered` is `List<Pair<MalAnime, MalAnimeListStatus?>>`, so `it.first` is the anime and `it.second` is the nullable list status. The `Content(emptyList())` vs `Empty` logic below this block is unchanged.)

- [ ] **Step 5: Compile the module**

Run: `./gradlew :myanimelist:compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 6: Commit**

```bash
git add myanimelist/src/main/java/com/grig/myanimelist/ui/animelist/AnimeListViewModel.kt
git commit -m "feat: wire sort option into anime list view model"
```

---

## Task 6: Wire sort into MangaListViewModel

**Files:**
- Modify: `myanimelist/src/main/java/com/grig/myanimelist/ui/mangalist/MangaListViewModel.kt`

Identical structure to Task 5, with manga types.

- [ ] **Step 1: Add imports**

Add to the import block:

```kotlin
import com.grig.myanimelist.ui.home.SortField
import com.grig.myanimelist.ui.home.SortOption
import com.grig.myanimelist.ui.home.SortDirection
import com.grig.myanimelist.ui.home.defaultDirection
import com.grig.myanimelist.ui.home.sortedWithOption
```

- [ ] **Step 2: Add sort state**

After the `_searchQuery` / `searchQuery` declaration block (around line 34-35), add:

```kotlin
    private val _sortOption = MutableStateFlow(SortOption.Default)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()
```

- [ ] **Step 3: Add `onSortSelected`**

After the `onSearchQueryChange` function (around line 79-82), add:

```kotlin
    fun onSortSelected(field: SortField) {
        val current = _sortOption.value
        _sortOption.value = if (current.field == field) {
            current.copy(
                direction = if (current.direction == SortDirection.Descending) {
                    SortDirection.Ascending
                } else {
                    SortDirection.Descending
                }
            )
        } else {
            SortOption(field, field.defaultDirection())
        }
        applyFilter()
    }
```

- [ ] **Step 4: Replace the hard-coded sort in `applyFilter()`**

Find this block (currently around lines 160-162):

```kotlin
        val cardData = filtered
            .map { MangaCardData(it.first, it.second) }
            .sortedByDescending { it.manga.mean }
```

Replace it with:

```kotlin
        val sort = _sortOption.value
        val sorted = when (sort.field) {
            SortField.Rating -> sortedWithOption(filtered, sort.direction) { it.first.mean }
            SortField.MyScore -> sortedWithOption(filtered, sort.direction) { it.second?.score }
            SortField.ReleaseDate -> sortedWithOption(filtered, sort.direction) { it.first.startDate }
            SortField.FinishDate -> sortedWithOption(filtered, sort.direction) { it.second?.finishDate }
        }
        val cardData = sorted.map { MangaCardData(it.first, it.second) }
```

(`filtered` is `List<Pair<MalManga, MalMangaListStatus?>>`. The `Content(emptyList())` vs `Empty` logic below is unchanged.)

- [ ] **Step 5: Compile the module**

Run: `./gradlew :myanimelist:compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 6: Commit**

```bash
git add myanimelist/src/main/java/com/grig/myanimelist/ui/mangalist/MangaListViewModel.kt
git commit -m "feat: wire sort option into manga list view model"
```

---

## Task 7: Wire sort UI into AnimeListPage

**Files:**
- Modify: `myanimelist/src/main/java/com/grig/myanimelist/ui/animelist/AnimeListPage.kt`

- [ ] **Step 1: Add imports**

Add to the import block:

```kotlin
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.grig.myanimelist.ui.home.SortButton
import com.grig.myanimelist.ui.home.SortBottomSheet
import com.grig.myanimelist.ui.home.SortField
import com.grig.myanimelist.ui.home.SortOption
```

(`getValue` may already be imported — if so, the duplicate import line will fail compilation; remove the duplicate. The others are new.)

- [ ] **Step 2: Collect sort state and add local sheet visibility**

Inside `AnimeListPage`, after the existing `collectAsState()` lines (after `watchlistIds` around line 46), add:

```kotlin
    val sortOption by viewModel.sortOption.collectAsState()
    var showSortSheet by remember { mutableStateOf(false) }
```

- [ ] **Step 3: Pass sort params into the chip row**

Change the `AnimeFilterChipsRow(...)` call (around lines 49-54) to:

```kotlin
        AnimeFilterChipsRow(
            statusFilter = statusFilter,
            upcomingFilter = upcomingFilter,
            onFilterSelected = viewModel::selectFilter,
            onUpcomingToggle = viewModel::toggleUpcomingFilter,
            onSortClick = { showSortSheet = true }
        )
```

- [ ] **Step 4: Render the sort sheet**

After the closing brace of the `editSheetAnime?.let { ... }` block (around line 105), before the final closing brace of the function, add:

```kotlin
    if (showSortSheet) {
        SortBottomSheet(
            current = sortOption,
            onSortSelected = viewModel::onSortSelected,
            onDismiss = { showSortSheet = false }
        )
    }
```

- [ ] **Step 5: Update `AnimeFilterChipsRow` signature and body**

Change the private `AnimeFilterChipsRow` composable (lines 108-138) to accept and render the sort button as the first item:

```kotlin
@Composable
private fun AnimeFilterChipsRow(
    statusFilter: Set<MalAnimeWatchingStatus>,
    upcomingFilter: Boolean,
    onFilterSelected: (MalAnimeWatchingStatus) -> Unit,
    onUpcomingToggle: () -> Unit,
    onSortClick: () -> Unit
) {
    val colors = AppThemeExtended.colorScheme

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            SortButton(onClick = onSortClick)
        }
        item {
            UpcomingFilterButton(selected = upcomingFilter, onClick = onUpcomingToggle)
        }
        items(MalAnimeWatchingStatus.entries) { status ->
            FilterChip(
                selected = status in statusFilter,
                onClick = { onFilterSelected(status) },
                label = { Text(status.displayName) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = colors.malCardStart,
                    selectedLabelColor = colors.cardText
                )
            )
        }
    }
}
```

- [ ] **Step 6: Compile the module**

Run: `./gradlew :myanimelist:compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 7: Commit**

```bash
git add myanimelist/src/main/java/com/grig/myanimelist/ui/animelist/AnimeListPage.kt
git commit -m "feat: add sort button and sheet to anime list page"
```

---

## Task 8: Wire sort UI into MangaListPage

**Files:**
- Modify: `myanimelist/src/main/java/com/grig/myanimelist/ui/mangalist/MangaListPage.kt`

Identical structure to Task 7, with manga types.

- [ ] **Step 1: Add imports**

Add to the import block:

```kotlin
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.grig.myanimelist.ui.home.SortButton
import com.grig.myanimelist.ui.home.SortBottomSheet
import com.grig.myanimelist.ui.home.SortField
import com.grig.myanimelist.ui.home.SortOption
```

(`getValue` is already imported in this file. Add `setValue` for the `var by` delegate.)

- [ ] **Step 2: Collect sort state and add local sheet visibility**

Inside `MangaListPage`, after the existing `collectAsState()` lines (after `editSheetManga` around line 45), add:

```kotlin
    val sortOption by viewModel.sortOption.collectAsState()
    var showSortSheet by remember { mutableStateOf(false) }
```

- [ ] **Step 3: Pass sort params into the chip row**

Change the `MangaFilterChipsRow(...)` call (around lines 48-53) to:

```kotlin
        MangaFilterChipsRow(
            statusFilter = statusFilter,
            upcomingFilter = upcomingFilter,
            onFilterSelected = viewModel::selectFilter,
            onUpcomingToggle = viewModel::toggleUpcomingFilter,
            onSortClick = { showSortSheet = true }
        )
```

- [ ] **Step 4: Render the sort sheet**

After the closing brace of the `editSheetManga?.let { ... }` block (around line 103), before the final closing brace of the function, add:

```kotlin
    if (showSortSheet) {
        SortBottomSheet(
            current = sortOption,
            onSortSelected = viewModel::onSortSelected,
            onDismiss = { showSortSheet = false }
        )
    }
```

- [ ] **Step 5: Update `MangaFilterChipsRow` signature and body**

Change the private `MangaFilterChipsRow` composable (lines 106-136) to:

```kotlin
@Composable
private fun MangaFilterChipsRow(
    statusFilter: Set<MalMangaReadingStatus>,
    upcomingFilter: Boolean,
    onFilterSelected: (MalMangaReadingStatus) -> Unit,
    onUpcomingToggle: () -> Unit,
    onSortClick: () -> Unit
) {
    val colors = AppThemeExtended.colorScheme

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            SortButton(onClick = onSortClick)
        }
        item {
            UpcomingFilterButton(selected = upcomingFilter, onClick = onUpcomingToggle)
        }
        items(MalMangaReadingStatus.entries) { status ->
            FilterChip(
                selected = status in statusFilter,
                onClick = { onFilterSelected(status) },
                label = { Text(status.displayName) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = colors.malCardStart,
                    selectedLabelColor = colors.cardText
                )
            )
        }
    }
}
```

- [ ] **Step 6: Compile the module**

Run: `./gradlew :myanimelist:compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 7: Commit**

```bash
git add myanimelist/src/main/java/com/grig/myanimelist/ui/mangalist/MangaListPage.kt
git commit -m "feat: add sort button and sheet to manga list page"
```

---

## Task 9: Full build + manual verification

**Files:** none (verification only)

- [ ] **Step 1: Assemble the debug app**

Run: `./gradlew :app:assembleDebug`
Expected: BUILD SUCCESSFUL

- [ ] **Step 2: Manual checks (run the app on a device/emulator)**

Confirm each, on both the Anime and Manga tabs:

- On first launch the list is sorted by Rating descending (highest community score first) — matches prior behavior.
- Tapping the sort button (leftmost in the filter row) opens the bottom sheet titled "Sort by" with four rows: Rating, My score, Release date, Date finished.
- The active field is bold with a downward arrow; others have no arrow.
- Tapping a different field re-sorts the list to that field, descending, and moves the arrow to it.
- Tapping the already-active field flips the arrow to upward and reverses the order.
- Entries with no rating / no personal score / no date appear at the **bottom** in both directions.
- Sort still works combined with a status filter, the upcoming filter, and a search query.

- [ ] **Step 3: Commit (if any verification fix was needed)**

Only if Step 2 surfaced a fix. Otherwise this task adds no commit.

```bash
git add -A
git commit -m "fix: address sort verification findings"
```

---

## Self-Review Notes

- **Spec coverage:** All four sort fields (Task 1 enum + Tasks 5/6 selectors); single sort button + bottom sheet (Tasks 3/4, wired in 7/8); tap-to-toggle direction (Tasks 5/6 `onSortSelected`); in-memory only, default Rating desc (Task 1 `SortOption.Default`); nulls-last in both directions (Task 1 comparator). Drawable constraint honored (Task 2, local vectors).
- **Type consistency:** `SortField`, `SortDirection`, `SortOption`, `SortOption.Default`, `defaultDirection()`, `sortedWithOption(...)`, `onSortSelected(field)`, `SortButton(onClick)`, `SortBottomSheet(current, onSortSelected, onDismiss)` are used identically across all tasks.
- **No placeholders:** every code step contains complete code.
