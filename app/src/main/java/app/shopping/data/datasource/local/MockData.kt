package app.shopping.data.datasource.local

import app.shopping.data.datasource.local.dao.CategoryDao
import app.shopping.data.datasource.local.dao.OrderDao
import app.shopping.data.datasource.local.dao.OrderItemDao
import app.shopping.data.datasource.local.dao.PaymentDao
import app.shopping.data.datasource.local.dao.ProductDao
import app.shopping.data.datasource.local.dao.ReviewDao
import app.shopping.data.datasource.local.dao.UserDao
import app.shopping.data.datasource.local.entity.CategoryEntity
import app.shopping.data.datasource.local.entity.OrderEntity
import app.shopping.data.datasource.local.entity.OrderItemEntity
import app.shopping.data.datasource.local.entity.PaymentEntity
import app.shopping.data.datasource.local.entity.ProductEntity
import app.shopping.data.datasource.local.entity.ReviewEntity
import app.shopping.data.datasource.local.entity.UserEntity
import app.shopping.domain.model.OrderItem
import java.time.Instant
import java.util.Date

class MockData {
    companion object {
        suspend fun setup(
            userDao: UserDao,
            categoryDao: CategoryDao,
            productDao: ProductDao,
            reviewDao: ReviewDao,
            orderDao: OrderDao,
            orderItemDao: OrderItemDao,
            paymentDao: PaymentDao
        ) {

            val categoryIds = categoryDao.insertAll(
                CategoryEntity(categoryName = "Electronics"),
                CategoryEntity(categoryName = "Books"),
                CategoryEntity(categoryName = "Groceries")
            )

            val electronicsCategoryId = categoryIds[0].toInt()
            val booksCategoryId = categoryIds[1].toInt()
            val groceriesCategoryId = categoryIds[2].toInt()

            productDao.insertAll(
                ProductEntity(
                    name = "Smartphone",
                    description = "Android smartphone",
                    price = 599.99,
                    imageUrl = "https://example.com/smartphone.jpg",
                    categoryId = electronicsCategoryId
                ),
                ProductEntity(
                    name = "Laptop",
                    description = "High-performance laptop",
                    price = 1299.99,
                    imageUrl = "https://example.com/laptop.jpg",
                    categoryId = electronicsCategoryId
                ),
                ProductEntity(
                    name = "Fantasy Novel",
                    description = "A thrilling fantasy story",
                    price = 14.99,
                    imageUrl = "https://example.com/novel.jpg",
                    categoryId = booksCategoryId
                ),
                ProductEntity(
                    name = "Coffee Beans",
                    description = "Premium coffee beans",
                    price = 9.99,
                    imageUrl = "https://example.com/coffee.jpg",
                    categoryId = groceriesCategoryId
                )
            )

            val userId = userDao.insertUser(
                UserEntity(username = "mock", email = "mock@mail.com", passwordHash = "123".reversed())
            ).toInt()

            val productId = productDao.insertProduct(
                ProductEntity(
                    name = "Coffee Beans with review",
                    description = "Premium coffee beans",
                    price = 9.99,
                    imageUrl = "https://example.com/coffee.jpg",
                    categoryId = groceriesCategoryId
                )
            ).toInt()

            reviewDao.insertReview(
                ReviewEntity(
                    productId = productId,
                    userId = userId,
                    rating = 4,
                    comment = "Cool stuff!"
                )
            )

            val orderId = orderDao.insertOrder(
                OrderEntity(
                    userId = userId,
                    orderDate =  Date(),
                    totalAmount =  131.0,
                    status = "Finished"
                )
            ).toInt()

            orderItemDao.insertOrderItem(
                OrderItemEntity(
                    orderId = orderId,
                    productId = productId,
                    quantity = 32,
                    price = 333.0
                )
            )

            paymentDao.insertPayment(
                PaymentEntity(
                    orderId = orderId,
                    amount = 333.0 * 32.0,
                    paymentMethod = "CARD",
                    paymentDate = Date()
                )
            )
        }
    }
}