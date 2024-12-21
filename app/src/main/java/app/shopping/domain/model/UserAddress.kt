package app.shopping.domain.model

data class UserAddress(
    val addressId: Int,
    val userId: Int,
    val street: String,
    val city: String,
    val state: String,
    val zipCode: String
)