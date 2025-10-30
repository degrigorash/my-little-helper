package com.grig.mylittlehelper.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.mylittlehelper.R

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        navigateToMal = {},
        navigateToDanish = {}
    )
}

@Composable
fun HomeScreen(
    navigateToMal: () -> Unit,
    navigateToDanish: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                shape = RoundedCornerShape(4),
                onClick = {
                    navigateToMal()
                }
            ) {
                Text(
                    text = stringResource(R.string.mal_button)
                )
            }
            Spacer(
                modifier = Modifier.requiredHeight(16.dp)
            )
            Button(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                shape = RoundedCornerShape(4),
                onClick = {
                    navigateToDanish()
                }
            ) {
                Text(
                    text = stringResource(R.string.danish_button)
                )
            }
        }
    }
}