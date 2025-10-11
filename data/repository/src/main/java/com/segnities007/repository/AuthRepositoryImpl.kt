package com.segnities007.repository

import com.segnities007.model.User
import com.segnities007.repository.AuthRepository
import com.segnities007.remote.datasource.AuthRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    
    private val _authState = MutableStateFlow(false)
    
    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        return remoteDataSource.signInWithGoogle(idToken)
            .also { result -> updateAuthState(result.isSuccess) }
    }
    
    override suspend fun signUp(
        nickname: String,
        bio: String?,
        avatarData: ByteArray?
    ): Result<User> {
        return remoteDataSource.signUp(nickname, bio, avatarData)
            .also { result -> updateAuthState(result.isSuccess) }
    }
    
    override suspend fun signOut(): Result<Unit> {
        return remoteDataSource.signOut()
            .also { result -> if (result.isSuccess) _authState.value = false }
    }
    
    private fun updateAuthState(isAuthenticated: Boolean) {
        _authState.value = isAuthenticated
    }
    
    override suspend fun isSessionValid(): Result<Boolean> {
        return remoteDataSource.isSessionValid()
            .onSuccess { isValid -> _authState.value = isValid }
    }
    
    override suspend fun refreshToken(): Result<String> {
        return remoteDataSource.refreshToken()
    }
    
    override suspend fun getCurrentUserUuid(): Result<String?> {
        return remoteDataSource.getCurrentUserUuid()
    }
    
    override fun observeAuthState(): Flow<Boolean> {
        return _authState.asStateFlow()
    }
}
