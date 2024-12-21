package app.shopping.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import app.shopping.data.datasource.local.entity.ShoppingCartEntity

@Dao
interface ShoppingCartDao {
    @Insert
    suspend fun insertCart(cart: ShoppingCartEntity): Long

    @Query("SELECT * FROM carts WHERE userId = :userId ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLatestCartByUser(userId: Int): ShoppingCartEntity?

    @Query("DELETE FROM carts WHERE cartId = :cartId")
    suspend fun deleteById(cartId: Int)
}