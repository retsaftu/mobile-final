package app.shopping.presentation.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.shopping.data.repository.AddressRepository
import app.shopping.domain.model.UserAddress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AddressesUiState(
    val addresses: List<UserAddress> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class AddressesViewModel(private val addressRepository: AddressRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AddressesUiState())
    val uiState: StateFlow<AddressesUiState> = _uiState

    fun loadUserAddresses(userId: Int) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val addresses = addressRepository.getUserAddresses(userId)
                _uiState.value = _uiState.value.copy(addresses = addresses, isLoading = false, errorMessage = null)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }

    fun addAddress(userId: Int, street: String, city: String, state: String, zip: String) {
        viewModelScope.launch {
            val result = addressRepository.addAddress(userId, street, city, state, zip)
            result.onSuccess {
                loadUserAddresses(userId)
            }.onFailure {
                _uiState.value = _uiState.value.copy(errorMessage = it.message)
            }
        }
    }
}