package com.example.dolegal.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FirestoreModule {

    @Provides
    @Singleton
    @Named("users")
    fun providesUsersDatabase(): CollectionReference {
        return Firebase.firestore.collection("users")
    }

    @Provides
    @Singleton
    @Named("story_category")
    fun providesCategoryDatabase(): CollectionReference {
        return Firebase.firestore.collection("story_categories")
    }

    @Provides
    @Singleton
    @Named("stories")
    fun providesStoriesDatabase(): CollectionReference {
        return Firebase.firestore.collection("stories")
    }

    @Provides
    @Singleton
    fun providesFirebaseAuth():FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun providesFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }


}