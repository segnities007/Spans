package com.segnities007.di

import com.segnities007.remote.datasource.AuthRemoteDataSource
import com.segnities007.remote.datasource.EncounterRemoteDataSource
import com.segnities007.remote.datasource.PostRemoteDataSource
import com.segnities007.remote.datasource.UserRemoteDataSource
import com.segnities007.remote.datasource.mock.MockAuthRemoteDataSource
import com.segnities007.remote.datasource.mock.MockEncounterRemoteDataSource
import com.segnities007.remote.datasource.mock.MockPostRemoteDataSource
import com.segnities007.remote.datasource.mock.MockUserRemoteDataSource
import org.koin.dsl.module

val dataSourceModule = module {
    single<AuthRemoteDataSource> { MockAuthRemoteDataSource() }
    single<UserRemoteDataSource> { MockUserRemoteDataSource() }
    single<PostRemoteDataSource> { MockPostRemoteDataSource() }
    single<EncounterRemoteDataSource> { MockEncounterRemoteDataSource() }
}
