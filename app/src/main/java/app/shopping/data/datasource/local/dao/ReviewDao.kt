package app.shopping.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import app.shopping.data.datasource.local.entity.ReviewEntity

@Dao
interface ReviewDao {
    @Insert
    suspend fun insertReview(review: ReviewEntity): Long

    @Query("SELECT * FROM reviews WHERE productId = :productId ORDER BY reviewId DESC")
    suspend fun getReviewsForProduct(productId: Int): List<ReviewEntity>
}