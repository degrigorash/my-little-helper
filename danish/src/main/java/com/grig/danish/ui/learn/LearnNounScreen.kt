package com.grig.danish.ui.learn

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grig.danish.tools.OneShotAudioPlayer
import com.grig.danish.ui.sound.WordEvent

@Composable
fun LearnNounRoute(
    modifier: Modifier = Modifier,
    viewModel: LearnNounViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                is WordEvent.Success -> OneShotAudioPlayer(context).play(it.link)
                is WordEvent.Error -> Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                WordEvent.Loading -> {}
            }
        }
    }

    LearnNounScreen(
        state = uiState,
        onPronounce = viewModel::pronounce,
        onNext = viewModel::nextWord,
        modifier = modifier
    )
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun LearnNounScreen(
    state: LearnNounUiState,
    onPronounce: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = state.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = state.word,
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 28.dp, vertical = 16.dp)
        ) {
            Text(text = "🔊", color = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.align(Alignment.Center))
        }
        LaunchedEffect(Unit) { /* purely decorative; pronounce via button below*/ }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onPronounce, contentPadding = PaddingValues(12.dp)) {
            Text("Pronunciation")
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = state.translation,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                SectionTitle("Bøjning")
                Spacer(Modifier.height(16.dp))
                Text("Singular definite", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(state.forms.singularDefinite, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(12.dp))
                Text("Plural", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(state.forms.plural, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(12.dp))
                Text("Plural definite", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(state.forms.pluralDefinite, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium)
            }
        }
        Spacer(Modifier.weight(1f))
        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Next Word")
        }
    }
}
