package app.shopping.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import app.shopping.data.datasource.local.entity.OrderItemEntity
import app.shopping.data.datasource.local.entity.ProductEntity

@Dao
interface OrderItemDao {
    @Insert
    suspend fun insertOrderItems(vararg items: OrderItemEntity)

    @Insert
    suspend fun insertOrderItem(item: OrderItemEntity)

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    suspend fun getOrderItemsByOrder(orderId: Int): List<OrderItemEntity>

    @Query("SELECT p.productId, p.name, p.description, p.price, p.imageUrl, p.categoryId FROM order_items as oi INNER JOIN products as p ON oi.productId = p.productId WHERE oi.orderId = :orderId")
    suspend fun getProductsByOrderId(orderId: Int): List<ProductEntity>

}