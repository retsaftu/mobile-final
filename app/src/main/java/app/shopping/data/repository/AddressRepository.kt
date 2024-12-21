package app.shopping.data.repository

import app.shopping.data.datasource.local.dao.UserAddressDao
import app.shopping.data.datasource.local.entity.UserAddressEntity
import app.shopping.domain.model.UserAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddressRepository(private val addressDao: UserAddressDao) {

    suspend fun addAddress(userId: Int, street: String, city: String, state: String, zip: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            val entity = UserAddressEntity(
                userId = userId,
                street = street,
                city = city,
                state = state,
                zipCode = zip
            )
            val id = addressDao.insertAddress(entity)
            if (id > 0) Result.success(Unit) else Result.failure(Exception("Failed to insert address"))
        }

    suspend fun getUserAddresses(userId: Int): List<UserAddress> = withContext(Dispatchers.IO) {
        addressDao.getAddressesForUser(userId).map { it.toDomain() }
    }

    private fun UserAddressEntity.toDomain() = UserAddress(
        addressId = addressId,
        userId = userId,
        street = street,
        city = city,
        state = state,
        zipCode = zipCode
    )
}