package app.shopping.domain.model

data class Product(
    val productId: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String?,
    val categoryId: Int
)
