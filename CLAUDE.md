# Project rules for Claude

This project's coding conventions are defined as Cursor rules in [`.cursor/rules/`](.cursor/rules/).
**Read and follow them** — they are the source of truth. This file mirrors the key points
so they are honored in Claude Code too. If a `.cursor/rules/*.mdc` file changes, update this file to match.

## Compose file organization — [`.cursor/rules/composable-file-organization.mdc`](.cursor/rules/composable-file-organization.mdc)

Split Composables into separate files by logical concern.

- **`*Screen.kt`**: only the top-level screen composable wiring ViewModel state to children. **No `private` child composables** — extract each into its own file.
- **Sub-components**: each logical UI section (card, list, detail content, loading state, empty state, etc.) gets its own file. No grab-bag files.
- **Shared components** used by 2+ screens go in a shared file (e.g. `MalLoading.kt`, `MalSearchBar.kt`), not duplicated.
- **Public by default**: extracted composables are `public`, not `private`, so previews and reuse work.
- **Previews are MANDATORY**: every public composable taking plain-data params (not a ViewModel) must have **light + dark** `@Preview`s, in the same file. Sample data lives in a per-package `PreviewData.kt` (create it when creating a new feature package).

## UI state pattern — [`.cursor/rules/ui-state-pattern.mdc`](.cursor/rules/ui-state-pattern.mdc)

- Model screen states as a **`sealed interface`** (not `sealed class`, not boolean flags), in a dedicated `*State.kt` file (never inline in the ViewModel).
- Standard substates: `Loading`, `Content`, `Error`, `Empty` (add more as needed).
- Shared fields → abstract property on the base interface, overridden per subtype.
- ViewModel: `_state.value = SomeState.Content(...)`. Screen: `when (state) { is …Loading -> … }`.
- Pure form/edit states without a Loading/Content/Error lifecycle may stay data classes.

## No material-icons-extended — [`.cursor/rules/no-material-icons-extended.mdc`](.cursor/rules/no-material-icons-extended.mdc)

`androidx.compose.material:material-icons-extended` is deprecated — never add it, never import from `androidx.compose.material.icons`. Use vector drawables in `res/drawable/` loaded via `painterResource(R.drawable.ic_*)`.

## Build / verify

Compile the library module after Compose changes:

```
./gradlew :myanimelist:compileDebugKotlin
```

## Other notes

- No TDD for this project — implement directly (see auto-memory).
- Screens receive the `ViewModel` directly (established convention), e.g. `fun XScreen(viewModel: XViewModel, ...)`.
- State is collected with `collectAsState()` (the codebase-wide idiom), not `collectAsStateWithLifecycle`.
