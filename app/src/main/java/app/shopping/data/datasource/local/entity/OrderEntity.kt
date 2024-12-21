package app.shopping.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val orderId: Int = 0,
    val userId: Int,
    val orderDate: Date,
    val totalAmount: Double,
    val status: String
)