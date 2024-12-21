package app.shopping.domain.model

data class Review(
    val reviewId: Int,
    val productId: Int,
    val userId: Int,
    val rating: Int,
    val comment: String
)