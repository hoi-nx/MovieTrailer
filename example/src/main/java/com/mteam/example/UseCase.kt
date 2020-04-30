package com.mteam.example

import kotlinx.android.parcel.Parcelize

object UseCase{
    const val useCase1Description = "#1 Perform single network request"
    const val useCase2Description = "#2 Perform two sequential network requests"
    const val useCase3Description = "#3 Perform several network requests concurrently"
    const val useCase4Description = "#4 Perform variable amount of network requests"
    const val useCase5Description = "#5 Network request with TimeOut"
    const val useCase6Description = "#6 Retry Network request"
    const val useCase7Description = "#7 Network requests with timeout and retry"
    const val useCase7UsingCallbacksDescription = "#7 Using callbacks"
    const val useCase7UsingRxDescription = "#7 Using RxJava"
    const val useCase8Description = "#8 Room and Coroutines"
    const val useCase10Description = "#10 Offload expensive calculation to background thread"
    const val useCase12Description = "#12 Offload expensive calculation to several coroutines"
    const val useCase13Description = "#13 Exception Handling"
    const val useCase14Description = "#14 Continue Coroutine when User leaves screen"
    const val useCase15Description = "#15 Using WorkManager with Coroutines"
    const val useCase16Description =
        "#16 Performance Analysis of dispatchers, number of coroutines and yielding"
    fun getUserCase():List<String>{
        return listOf(useCase1Description, useCase2Description,useCase3Description,useCase4Description,
            useCase5Description,useCase6Description,useCase7Description,useCase7UsingRxDescription,
            useCase8Description,useCase10Description,useCase12Description,useCase13Description,useCase14Description,useCase15Description,useCase16Description)
    }
}

