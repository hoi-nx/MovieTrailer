package com.mteam.example.di

import com.mteam.example.storage.SharedPreferencesStorage
import com.mteam.example.storage.Storage
import dagger.Binds
import dagger.Module
import dagger.Provides

// Tells Dagger this is a Dagger module
@Module
abstract class StorageModule {

    // Makes Dagger provide SharedPreferencesStorage when a Storage type is requested
    @Binds
    abstract fun provideStorage(storage: SharedPreferencesStorage): Storage

    // @Provides tell Dagger how to create instances of the type that this function
    // returns (i.e. Storage).
    // Function parameters are the dependencies of this type (i.e. Context).
   // @Provides
   // fun provideStorage(context: Context): Storage {
        // Whenever Dagger needs to provide an instance of type Storage,
        // this code (the one inside the @Provides method) will be run.
     //   return SharedPreferencesStorage(context)
   // }
}
