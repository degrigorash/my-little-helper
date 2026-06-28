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
