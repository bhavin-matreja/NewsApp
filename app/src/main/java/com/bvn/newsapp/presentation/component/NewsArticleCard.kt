package com.bvn.newsapp.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bvn.newsapp.domain.model.Article
import com.bvn.newsapp.domain.model.Source
import com.bvn.newsapp.util.dateFormatter

@Composable
fun NewsArticleCard(
    modifier: Modifier = Modifier,
    article: Article,
    onCardClicked: (Article) -> Unit
) {
    Card(modifier = modifier.clickable { onCardClicked(article) }) {
        Column(modifier = Modifier.padding(12.dp)) {
            ImageHolder(imageUrl = article.urlToImage)
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = article.source.name ?: "",
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    text = dateFormatter(article.publishedAt),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewNewsArticle() {
    NewsArticleCard(article = Article(
        author = "abcd",
        content = "abcd",
        description = "abcd",
        publishedAt = "abcd",
        source = Source("1" , "name"),
        title = "abcd",
        url = "abcd",
        urlToImage = "abcd",
    ), onCardClicked = {})
}