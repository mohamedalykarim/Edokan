package com.mohalim.edokan.core.hilt.modules

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.mohalim.edokan.core.datasource.repository.SellerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideFirestoreInstance(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideUserRepository(firestore: FirebaseFirestore): SellerRepository {
        return SellerRepository(firestore)
    }



}