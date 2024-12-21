package app.shopping.domain.model

import java.util.Date

data class Order(
    val orderId: Int,
    val userId: Int,
    val orderDate: Date,
    val totalAmount: Double,
    val status: String
)