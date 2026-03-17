package com.grig.myanimelist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.core.theme.AppThemeExtended
import com.grig.myanimelist.R

@Composable
fun MalSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit,
    placeholder: String
) {
    val colors = AppThemeExtended.colorScheme
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    listOf(colors.malCardStart, colors.malCardEnd)
                )
            )
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = "Back",
                tint = colors.cardText
            )
        }
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 48.dp),
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.cardText.copy(alpha = 0.5f)
                )
            },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = colors.cardText,
                fontWeight = FontWeight.SemiBold
            ),
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.cardText,
                unfocusedBorderColor = colors.cardText.copy(alpha = 0.5f),
                cursorColor = colors.cardText
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                keyboardController?.hide()
            }),
            trailingIcon = {
                IconButton(onClick = { keyboardController?.hide() }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = "Search",
                        tint = colors.cardText.copy(alpha = 0.7f)
                    )
                }
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Preview(name = "Search Bar - Empty")
@Composable
private fun MalSearchBarEmptyPreview() {
    AppTheme(darkTheme = false) {
        MalSearchBar(
            query = "",
            onQueryChange = {},
            onBack = {},
            placeholder = "Search anime..."
        )
    }
}

@Preview(name = "Search Bar - With Query")
@Composable
private fun MalSearchBarQueryPreview() {
    AppTheme(darkTheme = false) {
        MalSearchBar(
            query = "Demon Slayer",
            onQueryChange = {},
            onBack = {},
            placeholder = "Search anime..."
        )
    }
}

@Preview(name = "Search Bar - Dark")
@Composable
private fun MalSearchBarDarkPreview() {
    AppTheme(darkTheme = true) {
        MalSearchBar(
            query = "One Piece",
            onQueryChange = {},
            onBack = {},
            placeholder = "Search manga..."
        )
    }
}
