package com.segnities007.di

import com.segnities007.signup.mvi.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    // Auth ViewModels
    viewModel { SignUpViewModel(signUpUseCase = get()) }
    
    // TODO: 他のViewModelを追加
    // viewModel { SignInViewModel(signInWithGoogleUseCase = get()) }
    // viewModel { TimelineViewModel(getTimelineUseCase = get()) }
    // viewModel { PostViewModel(createPostUseCase = get()) }
    // viewModel { ProfileViewModel(getMyProfileUseCase = get(), updateProfileUseCase = get()) }
    // viewModel { SearchViewModel(searchUsersUseCase = get()) }
    // viewModel { SettingsViewModel(signOutUseCase = get()) }
}
