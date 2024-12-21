package app.shopping.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.shopping.data.repository.CategoryRepository
import app.shopping.data.repository.ProductRepository
import app.shopping.domain.model.Category
import app.shopping.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

data class ProductsUiState(
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: Int? = null,
    val errorMessage: String? = null,
    val isLoadingCategories: Boolean = false
)

class ProductsViewModel(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState: StateFlow<ProductsUiState> = _uiState

    private val selectedCategoryIdFlow = MutableStateFlow<Int?>(null)

    val productsPagingFlow: Flow<PagingData<Product>> = selectedCategoryIdFlow
        .flatMapLatest { categoryId ->
            productRepository.getProductsPaged(categoryId)
                .catch { emitAll(emptyFlow()) }
                .cachedIn(viewModelScope)
        }

    init {
        loadCategories()
    }

    private fun loadCategories() {
        _uiState.value = _uiState.value.copy(isLoadingCategories = true)
        viewModelScope.launch {
            try {
                val categories = categoryRepository.getAllCategories()
                _uiState.value = _uiState.value.copy(
                    categories = categories,
                    isLoadingCategories = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingCategories = false,
                    errorMessage = e.message
                )
            }
        }
    }

    fun selectCategory(categoryId: Int) {
        _uiState.value = _uiState.value.copy(selectedCategoryId = categoryId)
        selectedCategoryIdFlow.value = categoryId
    }
}
