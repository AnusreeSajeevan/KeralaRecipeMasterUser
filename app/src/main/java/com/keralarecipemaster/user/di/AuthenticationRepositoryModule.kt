package com.keralarecipemaster.user.di

import com.keralarecipemaster.user.repository.AuthenticationRepository
import com.keralarecipemaster.user.repository.AuthenticationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class AuthenticationRepositoryModule {
    @Binds
    abstract fun provideAuthenticationRepository(repository: AuthenticationRepositoryImpl): AuthenticationRepository
}
