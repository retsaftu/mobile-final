package app.shopping.presentation.productDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.shopping.data.repository.ProductRepository
import app.shopping.data.repository.ReviewRepository
import app.shopping.domain.model.Product
import app.shopping.domain.model.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProductDetailsUiState(
    val product: Product? = null,
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ProductDetailsViewModel(
    private val productRepository: ProductRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState

    fun loadProductDetails(productId: Int) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val product = productRepository.getProductById(productId)
                if (product == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Product not found"
                    )
                    return@launch
                }
                val reviews = reviewRepository.getReviewsForProduct(productId)
                _uiState.value = _uiState.value.copy(product = product, reviews = reviews, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }

    fun addReview(productId: Int, userId: Int, rating: Int, comment: String) {
        viewModelScope.launch {
            val result = reviewRepository.addReview(productId, userId, rating, comment)
            result.onSuccess {
                val reviews = reviewRepository.getReviewsForProduct(productId)
                _uiState.value = _uiState.value.copy(reviews = reviews)
            }.onFailure {
                _uiState.value = _uiState.value.copy(errorMessage = it.message)
            }
        }
    }
}