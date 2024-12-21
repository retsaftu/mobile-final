package app.shopping.data.datasource.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import app.shopping.data.datasource.local.entity.ProductEntity
import app.shopping.data.datasource.local.entity.UserEntity

@Dao
interface ProductDao {
    @Insert
    suspend fun insertProduct(user: ProductEntity): Long

    @Insert
    suspend fun insertAll(vararg products: ProductEntity): List<Long>

    @Query("SELECT * FROM products ORDER BY productId ASC")
    fun getAllProductsPaging(): PagingSource<Int, ProductEntity>

    @Query("SELECT * FROM products WHERE categoryId = :categoryId ORDER BY productId ASC")
    fun getProductsByCategoryPaging(categoryId: Int): PagingSource<Int, ProductEntity>

    @Query("SELECT * FROM products WHERE productId = :productId")
    suspend fun getById(productId: Int): ProductEntity?

}