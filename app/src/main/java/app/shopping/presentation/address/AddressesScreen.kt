package app.shopping.presentation.address

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddressesScreen(addressesViewModel: AddressesViewModel, userId: Int) {
    val uiState by addressesViewModel.uiState.collectAsState()

    LaunchedEffect(userId) {
        addressesViewModel.loadUserAddresses(userId)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("User Addresses", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.errorMessage != null) {
            Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
        } else {
            uiState.addresses.forEach { address ->
                AddressCard(
                    city = address.city,
                    state = address.state,
                    zipCode = address.zipCode,
                    street = address.street,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        AddAddressSection { street, city, state, zip ->
            addressesViewModel.addAddress(userId, street, city, state, zip)
        }
    }
}

@Composable
fun AddressCard(street: String, city: String, state: String, zipCode: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("City: $city", style = MaterialTheme.typography.bodyMedium)
            Text("Street: $street", style = MaterialTheme.typography.bodyMedium)
            Text("State: $state", style = MaterialTheme.typography.bodyMedium)
            Text("ZipCode: $zipCode", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun AddAddressSection(onAdd: (String, String, String, String) -> Unit) {
    var street by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var zip by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(value = street, onValueChange = { street = it }, label = { Text("Street") })
        OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City") })
        OutlinedTextField(value = state, onValueChange = { state = it }, label = { Text("State") })
        OutlinedTextField(value = zip, onValueChange = { zip = it }, label = { Text("Zip Code") })
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onAdd(street, city, state, zip) }) {
            Text("Add Address")
        }
    }
}