package com.mohalim.edokan.core.hilt.modules

import android.content.Context
import com.mohalim.edokan.core.datasource.preferences.UserPreferencesRepository
import com.mohalim.edokan.core.datasource.preferences.UserSelectionPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SharedPreferencesModules {

    @Singleton
    @Provides
    fun provideUserPreferencesRepository(@ApplicationContext context: Context): UserPreferencesRepository {
        return UserPreferencesRepository(context)
    }

    @Singleton
    @Provides
    fun provideUserSelectionPreferencesRepository(@ApplicationContext context: Context): UserSelectionPreferencesRepository {
        return UserSelectionPreferencesRepository(context)
    }

}