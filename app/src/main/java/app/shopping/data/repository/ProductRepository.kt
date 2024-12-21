package app.shopping.data.repository


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import app.shopping.data.datasource.local.dao.ProductDao
import app.shopping.data.datasource.local.entity.ProductEntity
import app.shopping.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepository(
    private val productDao: ProductDao
) {

    fun getProductsPaged(categoryId: Int?): Flow<PagingData<Product>> {
        val pagingSourceFactory = if (categoryId == null) {
            { productDao.getAllProductsPaging() }
        } else {
            { productDao.getProductsByCategoryPaging(categoryId) }
        }

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 3
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
            .map { pagingData -> pagingData.map { it.toDomain() } }
    }

   suspend fun getProductById(productId: Int): Product? {
       return productDao.getById(productId)?.toDomain()
   }

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
