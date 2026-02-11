package com.example.coursework_app.di

import com.example.coursework_app.data.db.dao.UserDao
import com.example.coursework_app.data.mappers.users.DbToDomainUserMapper
import com.example.coursework_app.data.mappers.users.DomainToDbUserMapper
import com.example.coursework_app.data.repository.UserRepositoryImpl
import com.example.coursework_app.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        userDao: UserDao,
        dbToDomainMapper: DbToDomainUserMapper,
        domainToDbMapper: DomainToDbUserMapper
    ): UserRepository {
        return UserRepositoryImpl(
            userDao = userDao,
            mapperDbToDomain = dbToDomainMapper,
            mapperDomainToDb = domainToDbMapper
        )
    }
}