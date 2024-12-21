package app.shopping.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import app.shopping.data.datasource.local.entity.CartItemEntity

@Dao
interface CartItemDao {
    @Insert
    suspend fun insertCartItems(vararg items: CartItemEntity)

    @Query("SELECT * FROM cart_items WHERE cartId = :cartId")
    suspend fun getCartItemsByCart(cartId: Int): List<CartItemEntity>

    @Query("DELETE FROM cart_items WHERE cartId = :cartId AND productId = :productId")
    suspend fun removeCartItem(cartId: Int, productId: Int)

    @Query("SELECT * FROM cart_items WHERE cartId = :cartId AND productId = :productId")
    suspend fun getCartItemsByCartAndProduct(cartId: Int, productId: Int): CartItemEntity?

    @Query("UPDATE cart_items SET quantity = :quantity WHERE cartId = :cartId AND productId = :productId")
    suspend fun updateProductQuantity(quantity: Int, cartId: Int, productId: Int)

    @Query("DELETE FROM cart_items WHERE cartItemId = :cartItemId")
    suspend fun deleteById(cartItemId: Int)
}