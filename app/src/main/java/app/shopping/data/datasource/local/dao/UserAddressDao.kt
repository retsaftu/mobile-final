package app.shopping.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import app.shopping.data.datasource.local.entity.UserAddressEntity

@Dao
interface UserAddressDao {
    @Insert
    suspend fun insertAddress(address: UserAddressEntity): Long

    @Query("SELECT * FROM user_addresses WHERE userId = :userId")
    suspend fun getAddressesForUser(userId: Int): List<UserAddressEntity>
}