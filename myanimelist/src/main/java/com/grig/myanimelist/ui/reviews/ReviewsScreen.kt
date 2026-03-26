package com.grig.myanimelist.ui.reviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppThemeExtended
import com.grig.myanimelist.R

@Composable
fun ReviewsScreen(
    viewModel: ReviewsViewModel,
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val activeFilter by viewModel.activeFilter.collectAsState()
    val colors = AppThemeExtended.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        listOf(colors.malCardStart, colors.malCardEnd)
                    )
                )
                .statusBarsPadding()
                .padding(horizontal = 4.dp, vertical = 8.dp)
        ) {
            IconButton(onClick = navigateBack) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    tint = colors.cardText
                )
            }
            Text(
                text = "Reviews",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.cardText,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        ReviewFilterChipsRow(
            activeFilter = activeFilter,
            onToggleFilter = viewModel::toggleFilter
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            when (val currentState = state) {
                is ReviewsState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center)
                    )
                }
                is ReviewsState.Error -> {
                    Text(
                        text = currentState.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp)
                    )
                }
                is ReviewsState.Empty -> {
                    Text(
                        text = "No reviews yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp)
                    )
                }
                is ReviewsState.Content -> {
                    val filteredReviews = viewModel.filteredReviews(currentState.reviews)
                    if (filteredReviews.isEmpty() && activeFilter != null) {
                        Text(
                            text = "No reviews match the selected filters",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(32.dp)
                        )
                    } else {
                        ReviewsList(
                            reviews = filteredReviews,
                            hasNextPage = currentState.hasNextPage,
                            isLoadingMore = currentState.isLoadingMore,
                            onLoadMore = viewModel::loadMore
                        )
                    }
                }
            }
        }
    }
}
