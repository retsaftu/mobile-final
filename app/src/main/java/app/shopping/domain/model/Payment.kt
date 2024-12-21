package app.shopping.domain.model

import java.util.Date

data class Payment(
    val paymentId: Int,
    val orderId: Int,
    val amount: Double,
    val paymentDate: Date,
    val paymentMethod: String
)