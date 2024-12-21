package app.shopping.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_addresses",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserAddressEntity(
    @PrimaryKey(autoGenerate = true)
    val addressId: Int = 0,
    val userId: Int,
    val street: String,
    val city: String,
    val state: String,
    val zipCode: String
)