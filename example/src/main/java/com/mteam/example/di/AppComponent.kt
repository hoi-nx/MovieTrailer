package com.mteam.example.di

import android.content.Context
import com.mteam.example.fragment.login.LoginComponent
import com.mteam.example.user.UserManager
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

// Scope annotation that the AppComponent uses
// Classes annotated with @Singleton will have a unique instance in this Component
@Singleton
// Definition of a Dagger component that adds info from the different modules to the graph
@Component(modules = [StorageModule::class, AppSubcomponents::class])
interface AppComponent {

    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

    // Types that can be retrieved from the graph
    fun loginComponent(): LoginComponent.Factory
    fun userManager(): UserManager
}
