package app.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.shopping.data.repository.ProductRepository
import app.shopping.data.repository.ReviewRepository
import app.shopping.presentation.productDetails.ProductDetailsViewModel

class ProductDetailsViewModelFactory(
    private val productRepository: ProductRepository,
    private val reviewRepository: ReviewRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductDetailsViewModel::class.java)) {
            return ProductDetailsViewModel(productRepository, reviewRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}