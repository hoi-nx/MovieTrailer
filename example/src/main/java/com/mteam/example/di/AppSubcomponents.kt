package com.mteam.example.di

import com.mteam.example.fragment.login.LoginComponent
import com.mteam.example.user.UserComponent
import dagger.Module

// This module tells a Component which are its subcomponents
@Module(
    subcomponents = [
        LoginComponent::class,
        UserComponent::class
    ]
)
class AppSubcomponents
