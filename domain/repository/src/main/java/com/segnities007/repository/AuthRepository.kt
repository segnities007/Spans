package com.segnities007.repository

import com.segnities007.model.User

interface AuthRepository {

    suspend fun signInWithGoogle(idToken: String): Result<User>

    suspend fun signUp(
        nickname: String,
        bio: String? = null,
        avatarData: ByteArray? = null
    ): Result<User>

    suspend fun signOut(): Result<Unit>

    suspend fun isSessionValid(): Result<Boolean>

    suspend fun refreshToken(): Result<String>

    suspend fun getCurrentUserUuid(): Result<String?>

    fun observeAuthState(): kotlinx.coroutines.flow.Flow<Boolean>
}
