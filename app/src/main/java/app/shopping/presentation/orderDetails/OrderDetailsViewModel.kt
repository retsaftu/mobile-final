package app.shopping.presentation.orderDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.shopping.data.repository.OrderRepository
import app.shopping.data.repository.PaymentRepository
import app.shopping.domain.model.Order
import app.shopping.domain.model.Payment
import app.shopping.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class OrderDetailsUiState(
    val order: Order? = null,
    val products: List<Product> = emptyList(),
    val payment: Payment? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class OrderDetailsViewModel(
    private val orderRepository: OrderRepository,
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderDetailsUiState())
    val uiState: StateFlow<OrderDetailsUiState> = _uiState

    fun loadOrderDetails(orderId: Int, userId: Int) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val order = orderRepository.getOrderById(userId)
                if (order == null) {
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = "Order not found.")
                    return@launch
                }

                val products = orderRepository.getProductsByOrderId(orderId)

                val payments = paymentRepository.getPaymentsForOrder(orderId)
                val payment = payments.firstOrNull()

                _uiState.value = _uiState.value.copy(
                    order = order,
                    products = products,
                    payment = payment,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }
}