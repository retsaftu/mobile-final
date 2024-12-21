package app.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.shopping.data.repository.AddressRepository
import app.shopping.presentation.address.AddressesViewModel

class AddressesViewModelFactory(private val repo: AddressRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddressesViewModel::class.java)) {
            return AddressesViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}