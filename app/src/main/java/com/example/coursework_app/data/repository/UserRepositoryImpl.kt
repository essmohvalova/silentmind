package com.example.coursework_app.data.repository

import com.example.coursework_app.data.db.dao.UserDao
import com.example.coursework_app.data.mappers.users.UserMapper
import com.example.coursework_app.domain.model.User
import com.example.coursework_app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val mapper: UserMapper
) : UserRepository {

    override suspend fun saveUser(user: User) {
        val entity = mapper.toEntity(user)
        userDao.insertUser(entity)
    }

    override suspend fun getUser(): User? {
        return mapper.toDomain(userDao.getUser())
    }

    override fun observeUser(): Flow<User?> {
        return userDao.observeUser().map { entity ->
            mapper.toDomain(entity)
        }
    }

    override suspend fun clearUserData() {
        userDao.deleteAllUsers()
    }
}