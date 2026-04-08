package com.example.coursework_app.data.repository

import com.example.coursework_app.data.db.dao.UserDao
import com.example.coursework_app.data.mappers.users.UserDbToDomainMapper
import com.example.coursework_app.data.mappers.users.UserDomainToDbMapper
import com.example.coursework_app.domain.model.user.User
import com.example.coursework_app.domain.preferences.UserPreferences
import com.example.coursework_app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val dbToDomainMapper: UserDbToDomainMapper,
    private val domainToDbMapper: UserDomainToDbMapper,
    private val userPreferences: UserPreferences,
) : UserRepository {

    override suspend fun saveUser(user: User) {
        val dbModel= domainToDbMapper(user)
        userDao.insertUser(dbModel)
        userPreferences.setUserId(id = user.id)
    }

    override suspend fun getUser(): User? {
        val id = userPreferences.getUserId()
        val dbModel = id?.let { userDao.getUser(it) }
        return dbModel?.let {dbToDomainMapper(it)}
    }

    override fun observeUser(id: String): Flow<User?> {
        return userDao.observeUser(id).map { entity ->
            entity?.let { dbToDomainMapper(it) }
        }
    }
}
