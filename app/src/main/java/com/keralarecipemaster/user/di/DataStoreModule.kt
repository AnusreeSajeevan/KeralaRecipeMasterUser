package com.keralarecipemaster.user.di

import com.keralarecipemaster.user.prefsstore.PrefsStore
import com.keralarecipemaster.user.prefsstore.PrefsStoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DataStoreModule {
    @Binds
    abstract fun bindsPrefsStore(prefsStoreImpl: PrefsStoreImpl): PrefsStore
}