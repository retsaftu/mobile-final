package app.shopping.domain.model

data class OrderItem(
    val orderItemId: Int,
    val orderId: Int,
    val productId: Int,
    val quantity: Int,
    val price: Double
)