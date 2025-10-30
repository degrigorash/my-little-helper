package com.grig.danish.ui.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun QuizRoute(
    modifier: Modifier = Modifier,
    viewModel: VocabularyQuizViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

//    LaunchedEffect(Unit) {
//        viewModel.event.collect {
//            when (it) {
//                is WordEvent.Success -> OneShotAudioPlayer(context).play(it.link)
//                is WordEvent.Error -> Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
//                WordEvent.Loading -> {}
//            }
//        }
//    }

    QuizScreen(
        state = state,
        onPronounce = viewModel::pronounce,
        onAnswerChanged = viewModel::updateAnswer,
        onCheck = viewModel::checkAnswer,
        onNext = viewModel::next,
        modifier = modifier
    )
}

@Preview
@Composable
fun QuizScreenPreview() {
    QuizScreen(
        state = QuizState.Loading,
        onPronounce = {},
        onAnswerChanged = {},
        onCheck = {},
        onNext = {}
    )
}

@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    state: QuizState,
    onPronounce: () -> Unit,
    onAnswerChanged: (String) -> Unit,
    onCheck: () -> Unit,
    onNext: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .blur(radius = 16.dp)
            .background(MaterialTheme.colorScheme.surfaceDim)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        state.word,
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = onPronounce,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD6ECFF))
                    ) {
                        Text("🔊  Pronunciation", color = Color(0xFF127AC1))
                    }
                }
            }
            OutlinedTextField(
                value = "",//state.userAnswer,
                onValueChange = onAnswerChanged,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Your answer…") }
            )
            Spacer(Modifier.height(16.dp))

            when (state) {
                is QuizState.Correct -> {
                    Text("Example: Correct Answer", color = Color(0xFF2F6C93))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE9F7EC)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = state.word,
                            modifier = Modifier.padding(16.dp),
                            color = Color(0xFF1E7E34)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("✔ Correct!", color = Color(0xFF1E7E34))
                }

                is QuizState.Wrong -> {
                    Text("Example: Incorrect Answer", color = Color(0xFF2F6C93))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEEEE)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = state.correct,
                            modifier = Modifier.padding(16.dp),
                            color = Color(0xFFC62828)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("✘ The correct answer is: ${state.correct}", color = Color(0xFFC62828))
                }

                else -> {}
            }

            Spacer(Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onCheck,
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text("Check Answer")
                }
                Button(
                    onClick = onNext,
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Text("Next Word")
                }
            }
        }
    }
}
