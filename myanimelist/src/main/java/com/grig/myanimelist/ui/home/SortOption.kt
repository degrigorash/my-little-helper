package com.grig.myanimelist.ui.home

enum class SortField(val displayName: String) {
    Rating("Rating"),
    MyScore("My score"),
    ReleaseDate("Release date"),
    FinishDate("Date completed"),
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
