package com.mteam.movie_trailer.di

import com.mteam.movie_trailer.fragment.login.LoginComponent
import com.mteam.movie_trailer.user.UserComponent
import dagger.Module

// This module tells a Component which are its subcomponents
@Module(
    subcomponents = [
        LoginComponent::class,
        UserComponent::class
    ]
)
class AppSubcomponents
