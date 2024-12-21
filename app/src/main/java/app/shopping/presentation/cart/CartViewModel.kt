package app.shopping.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.shopping.data.repository.CartRepository
import app.shopping.data.repository.OrderRepository
import app.shopping.domain.model.CartItem
import app.shopping.domain.model.OrderItem
import app.shopping.domain.model.ShoppingCart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CartUiState(
    val cart: ShoppingCart? = null,
    val cartItems: List<CartItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class CartViewModel(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState

    /**
     * Load or create a cart for the user and fetch its items.
     */
    fun loadCart(userId: Int) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val cart = cartRepository.getOrCreateCartForUser(userId)
                val items = cartRepository.getCartItems(cart.cartId)
                _uiState.value = _uiState.value.copy(
                    cart = cart,
                    cartItems = items,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }

    /**
     * Add an item to the current cart. Assumes cart is already loaded.
     */
    fun addItemToCart(productId: Int, quantity: Int) {
        val currentCart = _uiState.value.cart ?: return
        viewModelScope.launch {
            try {
                cartRepository.addItemToCart(currentCart.cartId, productId, quantity)
                val updatedItems = cartRepository.getCartItems(currentCart.cartId)
                _uiState.value = _uiState.value.copy(cartItems = updatedItems)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    /**
     * Remove an item from the current cart.
     */
    fun removeItemFromCart(productId: Int) {
        val currentCart = _uiState.value.cart ?: return
        viewModelScope.launch {
            try {
                cartRepository.removeItemFromCart(currentCart.cartId, productId)
                val updatedItems = cartRepository.getCartItems(currentCart.cartId)
                _uiState.value = _uiState.value.copy(cartItems = updatedItems)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    fun placeOrder(userId: Int) {
        val cartItems = _uiState.value.cartItems
        viewModelScope.launch {
            try {
                orderRepository.placeOrder(userId, cartItems)
                loadCart(userId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }
}