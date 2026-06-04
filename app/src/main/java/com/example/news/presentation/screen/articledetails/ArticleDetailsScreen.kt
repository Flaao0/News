@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.news.presentation.screen.articledetails

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.news.R
import com.example.news.domain.entity.Article
import com.example.news.presentation.utils.formatDate

@Composable
fun ArticleDetailsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: ArticleDetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val topBarTitle = when (val currentState = state) {
        is ArticleDetailsState.Content -> currentState.article.title
        else -> stringResource(R.string.article_details)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = topBarTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable { onBackClick() },
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                    )
                },
            )
        },
    ) { innerPadding ->
        when (val currentState = state) {
            ArticleDetailsState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            ArticleDetailsState.NotFound -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.article_not_found),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }

            is ArticleDetailsState.Content -> {
                ArticleDetailsContent(
                    article = currentState.article,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                )
            }
        }
    }
}

@Composable
private fun ArticleDetailsContent(
    article: Article,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        article.imageUrl?.let { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = stringResource(R.string.image_for_article, article.title),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 280.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = article.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = article.sourceName,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = article.publishedAt.formatDate(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
            )
        }

        if (article.description.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = article.description,
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        ArticleActionsRow(article = article)

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ArticleActionsRow(article: Article) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, article.url.toUri())
                context.startActivity(intent)
            },
        ) {
            Icon(
                imageVector = Icons.Default.OpenInNew,
                contentDescription = stringResource(R.string.open_article, article.title),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.read))
        }

        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "${article.title}\n\n${article.url}")
                }
                context.startActivity(intent)
            },
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = stringResource(R.string.share_article),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.share))
        }
    }
}
