package app.shopping.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import androidx.room.ForeignKey


@Entity(
    tableName = "carts",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ShoppingCartEntity(
    @PrimaryKey(autoGenerate = true)
    val cartId: Int = 0,
    val userId: Int,
    val createdAt: Date
)