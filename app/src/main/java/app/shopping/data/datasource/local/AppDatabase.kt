package app.shopping.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.shopping.data.datasource.local.dao.CartItemDao
import app.shopping.data.datasource.local.dao.CategoryDao
import app.shopping.data.datasource.local.dao.OrderDao
import app.shopping.data.datasource.local.dao.OrderItemDao
import app.shopping.data.datasource.local.dao.PaymentDao
import app.shopping.data.datasource.local.dao.ProductDao
import app.shopping.data.datasource.local.dao.ReviewDao
import app.shopping.data.datasource.local.dao.ShoppingCartDao
import app.shopping.data.datasource.local.dao.UserAddressDao
import app.shopping.data.datasource.local.dao.UserDao
import app.shopping.data.datasource.local.entity.CartItemEntity
import app.shopping.data.datasource.local.entity.CategoryEntity
import app.shopping.data.datasource.local.entity.OrderEntity
import app.shopping.data.datasource.local.entity.OrderItemEntity
import app.shopping.data.datasource.local.entity.PaymentEntity
import app.shopping.data.datasource.local.entity.ProductEntity
import app.shopping.data.datasource.local.entity.ReviewEntity
import app.shopping.data.datasource.local.entity.ShoppingCartEntity
import app.shopping.data.datasource.local.entity.UserAddressEntity
import app.shopping.data.datasource.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        CategoryEntity::class,
        ProductEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        ShoppingCartEntity::class,
        CartItemEntity::class,
        ReviewEntity::class,
        UserAddressEntity::class,
        PaymentEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao
    abstract fun shoppingCartDao(): ShoppingCartDao
    abstract fun cartItemDao(): CartItemDao
    abstract fun reviewDao(): ReviewDao
    abstract fun userAddressDao(): UserAddressDao
    abstract fun paymentDao(): PaymentDao
}