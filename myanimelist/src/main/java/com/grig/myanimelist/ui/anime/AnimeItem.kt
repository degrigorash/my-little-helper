package com.grig.myanimelist.ui.anime

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.grig.myanimelist.data.model.anime.MalAnime

@Composable
fun AnimeItem(anime: MalAnime) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 200.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
                .padding(8.dp),
            model = anime.pictures?.medium,
            contentDescription = null,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp, top = 8.dp)
        ) {
            Text(text = "${anime.title} (${anime.numEpisodes.toString()})")
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = anime.mean.toString()
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = "Rank: ${anime.rank}, Popularity: ${anime.popularity}"
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = anime.status.toString()
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.Black)
    )
}