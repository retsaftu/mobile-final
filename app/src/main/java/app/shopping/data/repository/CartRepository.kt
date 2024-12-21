package app.shopping.data.repository


import app.shopping.data.datasource.local.dao.CartItemDao
import app.shopping.data.datasource.local.dao.ProductDao
import app.shopping.data.datasource.local.dao.ShoppingCartDao
import app.shopping.data.datasource.local.entity.CartItemEntity
import app.shopping.data.datasource.local.entity.ProductEntity
import app.shopping.data.datasource.local.entity.ShoppingCartEntity
import app.shopping.domain.model.CartItem
import app.shopping.domain.model.Product
import app.shopping.domain.model.ShoppingCart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class CartRepository(
    private val cartDao: ShoppingCartDao,
    private val cartItemDao: CartItemDao,
    private val productDao: ProductDao
) {
    suspend fun getOrCreateCartForUser(userId: Int): ShoppingCart = withContext(Dispatchers.IO) {
        val existingCart = cartDao.getLatestCartByUser(userId)
        if (existingCart == null) {
            val newCartId = cartDao.insertCart(
                ShoppingCartEntity(userId = userId, createdAt = Date())
            )
            ShoppingCart(newCartId.toInt(), userId, Date())
        } else {
            existingCart.toDomain()
        }
    }

    suspend fun getCartItems(cartId: Int): List<CartItem> = withContext(Dispatchers.IO) {
        cartItemDao.getCartItemsByCart(cartId).map {
            val productEntity = productDao.getById(it.productId) ?: throw Exception("Invalid product")
            it.toDomain(productEntity.toDomain())
        }
    }

    suspend fun addItemToCart(cartId: Int, productId: Int, quantity: Int) = withContext(Dispatchers.IO) {
        val cartItem = cartItemDao.getCartItemsByCartAndProduct(cartId, productId)
        if (cartItem == null) {
            cartItemDao.insertCartItems(
                CartItemEntity(cartId = cartId, productId = productId, quantity = quantity)
            )
            return@withContext
        }
        cartItemDao.updateProductQuantity(cartItem.quantity + quantity, cartId, productId)
    }

    suspend fun removeItemFromCart(cartId: Int, productId: Int) = withContext(Dispatchers.IO) {
        val cartItem = cartItemDao.getCartItemsByCartAndProduct(cartId, productId)
        if (((cartItem?.quantity ?: 0) - 1) <= 0) {
            cartItemDao.removeCartItem(cartId, productId)
            return@withContext
        }
        cartItemDao.updateProductQuantity(cartItem!!.quantity - 1, cartId, productId)
    }

    private fun ShoppingCartEntity.toDomain() = ShoppingCart(
        cartId = cartId,
        userId = userId,
        createdAt = createdAt
    )

    private fun CartItemEntity.toDomain(product: Product) = CartItem(
        cartItemId = cartItemId,
        cartId = cartId,
        product = product,
        quantity = quantity
    )

    private fun ProductEntity.toDomain() = Product(
        productId = this.productId,
        name = this.name,
        description = this.description,
        price = this.price,
        imageUrl = this.imageUrl,
        categoryId = this.categoryId
    )
}