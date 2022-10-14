package com.keralarecipemaster.user.di

import com.keralarecipemaster.user.repository.FamousLocationRepository
import com.keralarecipemaster.user.repository.FamousLocationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class FamousLocationRepositoryModule {
    @Binds
    abstract fun provideFamousLocationRepository(famousLocationRepositoryImpl: FamousLocationRepositoryImpl): FamousLocationRepository
}
