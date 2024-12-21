package app.shopping.presentation.orderDetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.shopping.domain.model.Order
import app.shopping.domain.model.Payment
import app.shopping.domain.model.Product

@Composable
fun OrderDetailsScreen(
    viewModel: OrderDetailsViewModel,
    orderId: Int,
    userId: Int
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(orderId, userId) {
        viewModel.loadOrderDetails(orderId, userId)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Order Details", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        when {
            uiState.isLoading -> CircularProgressIndicator()
            uiState.errorMessage != null -> {
                Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
            }
            uiState.order != null -> {
                OrderSummary(uiState.order!!)
                Spacer(modifier = Modifier.height(16.dp))
                uiState.payment?.let {
                    PaymentSection(it)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                ProductsSection(uiState.products)
            }
        }
    }
}

@Composable
fun OrderSummary(order: Order) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Order #${order.orderId}", style = MaterialTheme.typography.titleMedium)
            Text("Date: ${order.orderDate}")
            Text("Status: ${order.status}")
            Text("Total: $${order.totalAmount}")
        }
    }
}


@Composable
fun PaymentSection(payment: Payment) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Payment Method", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Method: ${payment.paymentMethod}")
            Text("Amount: $${payment.amount}")
            Text("Date: ${payment.paymentDate}")
        }
    }
}

@Composable
fun ProductsSection(products: List<Product>) {
    Text("Products", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))
    LazyColumn {
        items(products) { product ->
            ProductItemRow(product)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ProductItemRow(product: Product) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(product.name, style = MaterialTheme.typography.bodyLarge)
            Text(product.description, style = MaterialTheme.typography.bodyMedium)
            Text("Price: $${product.price}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}