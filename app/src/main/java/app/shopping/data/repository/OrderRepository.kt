package app.shopping.data.repository


import app.shopping.data.datasource.local.dao.CartItemDao
import app.shopping.data.datasource.local.dao.OrderDao
import app.shopping.data.datasource.local.dao.OrderItemDao
import app.shopping.data.datasource.local.dao.PaymentDao
import app.shopping.data.datasource.local.dao.ShoppingCartDao
import app.shopping.data.datasource.local.entity.OrderEntity
import app.shopping.data.datasource.local.entity.OrderItemEntity
import app.shopping.data.datasource.local.entity.PaymentEntity
import app.shopping.data.datasource.local.entity.ProductEntity
import app.shopping.domain.model.CartItem
import app.shopping.domain.model.Order
import app.shopping.domain.model.OrderItem
import app.shopping.domain.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class OrderRepository(
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao,
    private val paymentDao: PaymentDao,
    private val cartDao: ShoppingCartDao,
    private val cartItemDao: CartItemDao
) {
    suspend fun getUserOrders(userId: Int): List<Order> = withContext(Dispatchers.IO) {
        orderDao.getOrdersByUser(userId).map { it.toDomain() }
    }

    suspend fun getOrderById(orderId: Int): Order? {
        return orderDao.getById(orderId)?.toDomain();
    }

    suspend fun getProductsByOrderId(orderId: Int): List<Product> = withContext(Dispatchers.IO) {
        orderItemDao.getProductsByOrderId(orderId).map { it.toDomain() }
    }

    suspend fun placeOrder(userId: Int, items: List<CartItem>): Long = withContext(Dispatchers.IO) {
        val price = items.sumOf { it.product.price * it.quantity }

        val orderEntity = OrderEntity(
            userId = userId,
            orderDate = Date(),
            totalAmount = price,
            status = "Success"
        )
        val orderId = orderDao.insertOrder(orderEntity)
        val orderItemEntities = items.map {
            OrderItemEntity(
                orderId = orderId.toInt(),
                productId = it.product.productId,
                quantity = it.quantity,
                price = it.product.price
            )
        }.toTypedArray()
        orderItemDao.insertOrderItems(*orderItemEntities)
        paymentDao.insertPayment(
            PaymentEntity(
                orderId = orderId.toInt(),
                amount = price,
                paymentDate = Date(),
                paymentMethod = "CARD"
            )
        )

        val cartId = items.first().cartId
        items.forEach {
            cartItemDao.deleteById(it.cartItemId)
        }
        cartDao.deleteById(cartId)

        orderId
    }

    private fun OrderEntity.toDomain() = Order(
        orderId = orderId,
        userId = userId,
        orderDate = orderDate,
        totalAmount = totalAmount,
        status = status
    )

    private fun OrderItemEntity.toDomain() = OrderItem(
        orderItemId = orderItemId,
        orderId = orderId,
        productId = productId,
        quantity = quantity,
        price = price
    )

    private fun ProductEntity.toDomain(): Product {
        return Product(
            productId = this.productId,
            name = this.name,
            description = this.description,
            price = this.price,
            imageUrl = this.imageUrl,
            categoryId = this.categoryId
        )
    }
}
