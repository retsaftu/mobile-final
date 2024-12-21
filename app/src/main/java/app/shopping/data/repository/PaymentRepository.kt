package app.shopping.data.repository

import app.shopping.data.datasource.local.dao.PaymentDao
import app.shopping.data.datasource.local.entity.PaymentEntity
import app.shopping.domain.model.Payment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class PaymentRepository(private val paymentDao: PaymentDao) {

    suspend fun addPayment(orderId: Int, amount: Double, paymentDate: Date, method: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            val entity = PaymentEntity(
                orderId = orderId,
                amount = amount,
                paymentDate = paymentDate,
                paymentMethod = method
            )
            val id = paymentDao.insertPayment(entity)
            if (id > 0) Result.success(Unit) else Result.failure(Exception("Failed to insert payment"))
        }

    suspend fun getPaymentsForOrder(orderId: Int): List<Payment> = withContext(Dispatchers.IO) {
        paymentDao.getPaymentsForOrder(orderId).map { it.toDomain() }
    }

    private fun PaymentEntity.toDomain() = Payment(
        paymentId = paymentId,
        orderId = orderId,
        amount = amount,
        paymentDate = paymentDate,
        paymentMethod = paymentMethod
    )
}