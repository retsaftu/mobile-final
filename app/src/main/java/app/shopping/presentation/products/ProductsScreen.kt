package app.shopping.presentation.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import app.shopping.domain.model.Category
import app.shopping.domain.model.Product

@Composable
fun ProductsScreen(viewModel: ProductsViewModel, onAddToCart: (Int) -> Unit, onProduct: (Int) -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val productsPagingItems = viewModel.productsPagingFlow.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize()) {
        CategoryRow(
            categories = uiState.categories,
            selectedCategoryId = uiState.selectedCategoryId,
            onCategorySelected = { categoryId -> viewModel.selectCategory(categoryId) }
        )

        if (uiState.isLoadingCategories) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else if (uiState.errorMessage != null) {
            Text(uiState.errorMessage ?: "Unknown Error", color = MaterialTheme.colorScheme.error)
        } else {
            ProductList(productsPagingItems, onAddToCart,  onProduct)
        }
    }
}

@Composable
fun CategoryRow(
    categories: List<Category>,
    selectedCategoryId: Int?,
    onCategorySelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val isSelected = category.categoryId == selectedCategoryId
            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(category.categoryId) },
                label = { Text(category.categoryName) }
            )
        }
    }
}

@Composable
fun ProductList(
    productsPagingItems: androidx.paging.compose.LazyPagingItems<Product>,
    onAddToCart: (Int) -> Unit,
    onProduct: (Int) -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(8.dp)) {
        items(productsPagingItems.itemCount) { productIndex ->
            productsPagingItems[productIndex]?.let {
                ProductItem(it, onAddToCart, onProduct)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        productsPagingItems.apply {
            when {
                loadState.refresh is androidx.paging.LoadState.Loading -> {
                    item { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
                }

                loadState.append is androidx.paging.LoadState.Loading -> {
                    item { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
                }

                loadState.append is androidx.paging.LoadState.Error -> {
                    // Show retry if needed
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onAddToCart: (Int) -> Unit, onProduct: (Int) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable { onProduct(product.productId) }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(product.name, style = MaterialTheme.typography.titleMedium)
            Text(product.description, style = MaterialTheme.typography.bodyMedium)
            Text("Price: $${product.price}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { onAddToCart(product.productId) }) {
                Text("Add to Cart")
            }
        }
    }
}