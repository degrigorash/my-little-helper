package com.grig.myanimelist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.grig.myanimelist.R

@Composable
fun MalEmpty() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.Center),
            painter = painterResource(R.drawable.mal_empty),
            contentDescription = null
        )
    }
}