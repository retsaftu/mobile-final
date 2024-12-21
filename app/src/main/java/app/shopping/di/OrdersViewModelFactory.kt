package app.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.shopping.data.repository.OrderRepository
import app.shopping.presentation.orders.OrdersViewModel

class OrdersViewModelFactory(private val repo: OrderRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrdersViewModel::class.java)) {
            return OrdersViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}