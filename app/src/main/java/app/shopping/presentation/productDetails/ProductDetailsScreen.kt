package app.shopping.presentation.productDetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.shopping.domain.model.Review

@Composable
fun ProductDetailsScreen(
    viewModel: ProductDetailsViewModel,
    productId: Int,
    userId: Int
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(productId) {
        viewModel.loadProductDetails(productId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.errorMessage != null) {
            Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
        } else {
            uiState.product?.let { product ->
                Text(product.name, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(product.description, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))

                Text("Reviews", style = MaterialTheme.typography.titleMedium)
                if (uiState.reviews.isEmpty()) {
                    Text("No reviews yet. Be the first!")
                } else {
                    LazyColumn {
                        items(uiState.reviews) { review ->
                            ReviewItem(review)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                AddReviewSection(
                    onAddReview = { rating, comment ->
                        viewModel.addReview(productId, userId, rating, comment)
                    }
                )
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("User #${review.userId}", style = MaterialTheme.typography.labelLarge)
            Text("Rating: ${review.rating}/5", style = MaterialTheme.typography.bodyMedium)
            Text(review.comment, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun AddReviewSection(onAddReview: (Int, String) -> Unit) {
    var rating by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = rating,
            onValueChange = { rating = it },
            label = { Text("Rating (1-5)") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text("Comment") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            val ratingInt = rating.toIntOrNull()
            if (ratingInt != null && ratingInt in 1..5) {
                onAddReview(ratingInt, comment)
                rating = ""
                comment = ""
            } else {
                // Show some error message in a real app
            }
        }) {
            Text("Submit Review")
        }
    }
}