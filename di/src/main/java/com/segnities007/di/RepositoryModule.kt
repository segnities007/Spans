package com.segnities007.di

import com.segnities007.repository.AuthRepository
import com.segnities007.repository.AuthRepositoryImpl
import com.segnities007.repository.EncounterRepository
import com.segnities007.repository.EncounterRepositoryImpl
import com.segnities007.repository.PostRepository
import com.segnities007.repository.PostRepositoryImpl
import com.segnities007.repository.UserRepository
import com.segnities007.repository.UserRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { 
        AuthRepositoryImpl(remoteDataSource = get())
    }
    
    single<UserRepository> { 
        UserRepositoryImpl(
            remoteDataSource = get(),
            userDao = null
        )
    }
    
    single<PostRepository> { 
        PostRepositoryImpl(
            remoteDataSource = get(),
            postDao = null
        )
    }
    
    single<EncounterRepository> { 
        EncounterRepositoryImpl(
            remoteDataSource = get(),
            encounterDao = null
        )
    }
}
