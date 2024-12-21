package app.shopping.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import app.shopping.data.datasource.local.entity.OrderEntity

@Dao
interface OrderDao {
    @Insert
    suspend fun insertOrder(order: OrderEntity): Long

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY orderDate DESC")
    suspend fun getOrdersByUser(userId: Int): List<OrderEntity>

    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    suspend fun getById(orderId: Int): OrderEntity?
}