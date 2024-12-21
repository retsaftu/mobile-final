package app.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.shopping.data.repository.CartRepository
import app.shopping.data.repository.OrderRepository
import app.shopping.presentation.cart.CartViewModel

class CartViewModelFactory(private val cartRepository: CartRepository, private val orderRepository: OrderRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            return CartViewModel(cartRepository, orderRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}