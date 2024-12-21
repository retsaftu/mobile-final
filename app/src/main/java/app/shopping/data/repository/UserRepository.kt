package app.shopping.data.repository

import app.shopping.data.datasource.local.dao.UserDao
import app.shopping.data.datasource.local.entity.UserEntity
import app.shopping.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {

    suspend fun signUp(username: String, email: String, password: String): Result<Unit> = withContext(Dispatchers.IO) {
        val existingUser = userDao.getUserByUsername(username)
        if (existingUser != null) {
            return@withContext Result.failure(Exception("User already exists"))
        }

        val hashedPassword = password.reversed()
        val userEntity = UserEntity(
            email = email,
            username = username,
            passwordHash = hashedPassword
        )
        userDao.insertUser(userEntity)
        Result.success(Unit)
    }

    suspend fun signIn(username: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        val userEntity = userDao.getUserByUsername(username)
        val hashedPassword = password.reversed()
        return@withContext if (userEntity != null && userEntity.passwordHash == hashedPassword) {
            Result.success(User(id = userEntity.id, username = userEntity.username))
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
    }
}
