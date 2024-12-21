package app.shopping.presentation.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.shopping.data.repository.OrderRepository
import app.shopping.domain.model.Order
import app.shopping.domain.model.OrderItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class OrdersUiState(
    val orders: List<Order> = emptyList(),
    val selectedOrderId: Int? = null,
    val orderItems: List<OrderItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class OrdersViewModel(private val orderRepository: OrderRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState: StateFlow<OrdersUiState> = _uiState

    fun loadUserOrders(userId: Int) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val orders = orderRepository.getUserOrders(userId)
                _uiState.value = _uiState.value.copy(orders = orders, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message, isLoading = false)
            }
        }
    }
}
