package app.shopping.presentation.orders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OrdersScreen(ordersViewModel: OrdersViewModel, userId: Int, onOrder: (Int) -> Unit) {
    val uiState by ordersViewModel.uiState.collectAsState()

    LaunchedEffect(userId) {
        ordersViewModel.loadUserOrders(userId)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Your Orders", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.errorMessage != null) {
            Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
        } else {
            uiState.orders.forEach { order ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOrder(order.orderId) }) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Order #${order.orderId} - Total: $${order.totalAmount} - Status: ${order.status}",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
