package app.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.shopping.data.repository.OrderRepository
import app.shopping.data.repository.PaymentRepository
import app.shopping.presentation.orderDetails.OrderDetailsViewModel

class OrderDetailsViewModelFactory(
    private val orderRepository: OrderRepository,
    private val paymentRepository: PaymentRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderDetailsViewModel::class.java)) {
            return OrderDetailsViewModel(orderRepository, paymentRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}