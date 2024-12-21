package app.shopping.data.repository

import app.shopping.data.datasource.local.dao.CategoryDao
import app.shopping.data.datasource.local.entity.CategoryEntity
import app.shopping.domain.model.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepository(
    private val categoryDao: CategoryDao,
) {
    suspend fun getAllCategories(): List<Category> = withContext(Dispatchers.IO) {
        categoryDao.getAllCategories().map { it.toDomain() }
    }

    private fun CategoryEntity.toDomain(): Category {
        return Category(
            categoryId = this.categoryId,
            categoryName = this.categoryName
        )
    }
}