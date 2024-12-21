package app.shopping.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import app.shopping.data.datasource.local.entity.PaymentEntity

@Dao
interface PaymentDao {
    @Insert
    suspend fun insertPayment(payment: PaymentEntity): Long

    @Query("SELECT * FROM payments WHERE orderId = :orderId")
    suspend fun getPaymentsForOrder(orderId: Int): List<PaymentEntity>
}