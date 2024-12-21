package app.shopping.data.repository

import app.shopping.data.datasource.local.dao.ReviewDao
import app.shopping.data.datasource.local.entity.ReviewEntity
import app.shopping.domain.model.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReviewRepository(private val reviewDao: ReviewDao) {

    suspend fun getReviewsForProduct(productId: Int): List<Review> = withContext(Dispatchers.IO) {
        reviewDao.getReviewsForProduct(productId).map { it.toDomain() }
    }

    suspend fun addReview(productId: Int, userId: Int, rating: Int, comment: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            val entity = ReviewEntity(
                productId = productId,
                userId = userId,
                rating = rating,
                comment = comment
            )
            val id = reviewDao.insertReview(entity)
            if (id > 0) Result.success(Unit) else Result.failure(Exception("Failed to insert review"))
        }

    private fun ReviewEntity.toDomain() = Review(
        reviewId = reviewId,
        productId = productId,
        userId = userId,
        rating = rating,
        comment = comment
    )
}