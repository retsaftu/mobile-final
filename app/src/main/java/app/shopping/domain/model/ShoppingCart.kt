package app.shopping.domain.model

import java.util.Date

data class ShoppingCart(
    val cartId: Int,
    val userId: Int,
    val createdAt: Date
)