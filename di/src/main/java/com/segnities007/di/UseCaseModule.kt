package com.segnities007.di

import com.segnities007.usecase.auth.SignInWithGoogleUseCase
import com.segnities007.usecase.auth.SignOutUseCase
import com.segnities007.usecase.auth.SignUpUseCase
import com.segnities007.usecase.encounter.GetEncounterStatsUseCase
import com.segnities007.usecase.encounter.GetEncountersUseCase
import com.segnities007.usecase.post.CreatePostUseCase
import com.segnities007.usecase.post.DeletePostUseCase
import com.segnities007.usecase.post.LikePostUseCase
import com.segnities007.usecase.post.UnlikePostUseCase
import com.segnities007.usecase.timeline.GetTimelineUseCase
import com.segnities007.usecase.user.GetMyProfileUseCase
import com.segnities007.usecase.user.UpdateProfileUseCase
import org.koin.dsl.module

val useCaseModule = module {
    // Auth UseCases
    factory { SignInWithGoogleUseCase(authRepository = get()) }
    factory { SignUpUseCase(authRepository = get()) }
    factory { SignOutUseCase(authRepository = get()) }
    
    // Timeline UseCases
    factory { 
        GetTimelineUseCase(
            postRepository = get(),
            encounterRepository = get()
        )
    }
    
    // Post UseCases
    factory { CreatePostUseCase(postRepository = get()) }
    factory { DeletePostUseCase(postRepository = get()) }
    factory { LikePostUseCase(postRepository = get()) }
    factory { UnlikePostUseCase(postRepository = get()) }
    
    // User UseCases
    factory { GetMyProfileUseCase(userRepository = get()) }
    factory { UpdateProfileUseCase(userRepository = get()) }
    
    // Encounter UseCases
    factory { GetEncountersUseCase(encounterRepository = get()) }
    factory { GetEncounterStatsUseCase(encounterRepository = get()) }
}
