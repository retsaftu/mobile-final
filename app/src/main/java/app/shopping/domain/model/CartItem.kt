package app.shopping.domain.model

data class CartItem(
    val cartItemId: Int,
    val cartId: Int,
    val product: Product,
    val quantity: Int
)