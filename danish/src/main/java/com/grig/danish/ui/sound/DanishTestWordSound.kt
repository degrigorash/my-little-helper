package com.grig.danish.ui.sound

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.grig.danish.R
import com.grig.danish.tools.OneShotAudioPlayer

@Composable
fun DanishTestWordSoundScreen(
    viewModel: DanishTestWordViewModel,
    modifier: Modifier = Modifier
) {

    DanishTestWordSound(
        modifier = modifier,
        onMicClick = viewModel::onMicClick
    )
}

@Preview
@Composable
fun DanishTestWordSoundPreview() {
    DanishTestWordSound {}
}

@Composable
fun DanishTestWordSound(
    modifier: Modifier = Modifier,
    onMicClick: (word: String) -> Unit
) {
    val context = LocalContext.current
    var word by remember { mutableStateOf("") }

//    LaunchedEffect(Unit) {
//        viewModel.event.collect {
//            when (it) {
//                LoginCharityEmailEvent.Exit -> {
//                    back()
//                }
//
//                LoginCharityEmailEvent.Next -> {
//                    next()
//                }
//
//                is LoginCharityEmailEvent.Partner -> {
//                    partner = it.partnerUrl to it.email
//                    showPartnerDialog = true
//                }
//            }
//        }
//    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TextField(
            value = word,
            onValueChange = {
                word = it
            }
        )

        Image(
            modifier = Modifier
                .wrapContentSize()
                .clickable {
                    onMicClick(word)
                    OneShotAudioPlayer(context).play("")
                },
            painter = painterResource(R.drawable.ic_talk),
            contentDescription = ""
        )
    }
}