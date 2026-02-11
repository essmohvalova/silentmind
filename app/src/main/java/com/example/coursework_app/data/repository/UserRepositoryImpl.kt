package com.example.coursework_app.data.repository

import com.example.coursework_app.data.db.dao.UserDao
import com.example.coursework_app.data.mappers.users.DbToDomainUserMapper
import com.example.coursework_app.data.mappers.users.DomainToDbUserMapper
import com.example.coursework_app.domain.model.user.User
import com.example.coursework_app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val mapperDbToDomain: DbToDomainUserMapper,
    private val mapperDomainToDb: DomainToDbUserMapper
) : UserRepository {

    override suspend fun saveUser(user: User) {
        val dbModel= mapperDomainToDb(user)
        userDao.insertUser(dbModel)
    }

    override suspend fun getUser(id: String): User? {
        val dbModel = userDao.getUser(id)
        return dbModel?.let {mapperDbToDomain(it)}
    }

    override fun observeUser(id: String): Flow<User?> {
        return userDao.observeUser(id).map { entity ->
            entity?.let { mapperDbToDomain(it) }
        }
    }

    override suspend fun clearUserData() {
        userDao.deleteAllUsers()
    }
}