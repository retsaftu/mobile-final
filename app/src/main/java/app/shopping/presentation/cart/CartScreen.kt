package app.shopping.presentation.cart

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.unit.dp
import app.shopping.domain.model.CartItem


@Composable
fun CartScreen(cartViewModel: CartViewModel, userId: Int) {
    val uiState by cartViewModel.uiState.collectAsState()

    LaunchedEffect(userId) {
        cartViewModel.loadCart(userId)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Your Cart", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.errorMessage != null) {
            Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
        } else {
            uiState.cartItems.forEach { item ->
                CartItemRow(item = item, onRemove = {
                    cartViewModel.removeItemFromCart(item.product.productId)
                })
            }
            if (uiState.cartItems.isNotEmpty()) {
                Button({ cartViewModel.placeOrder(userId) }) { Text("Place order") }
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, onRemove: () -> Unit) {
    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Product #${item.product.name} x${item.quantity}")
        TextButton(onClick = onRemove) {
            Text("Remove")
        }
    }
}