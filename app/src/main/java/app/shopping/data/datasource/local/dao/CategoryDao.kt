package app.shopping.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import app.shopping.data.datasource.local.entity.CategoryEntity

@Dao
interface CategoryDao {
    @Insert
    suspend fun insertAll(vararg categories: CategoryEntity): List<Long>

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<CategoryEntity>
}