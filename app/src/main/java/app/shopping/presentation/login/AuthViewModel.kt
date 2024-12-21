package app.shopping.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.shopping.data.repository.UserRepository
import app.shopping.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun signIn(username: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = userRepository.signIn(username, password)
            _authState.value = result.fold(
                onSuccess = { user -> AuthState.Success(user) },
                onFailure = { throwable -> AuthState.Error(throwable.message ?: "Unknown error") }
            )
        }
    }

    fun signUp(username: String, email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = userRepository.signUp(username, email, password)
            result.fold(
                onSuccess = {
                    signIn(username, password)
                },
                onFailure = { throwable -> _authState.value = AuthState.Error(throwable.message ?: "Unknown error") }
            )
        }
    }
}
