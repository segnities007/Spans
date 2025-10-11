package com.segnities007.remote.datasource.mock

import com.segnities007.model.User
import com.segnities007.remote.datasource.AuthRemoteDataSource
import kotlinx.coroutines.delay

/**
 * AuthRemoteDataSourceのモック実装
 * 
 * Supabase実装前のテスト用
 */
class MockAuthRemoteDataSource : AuthRemoteDataSource {
    
    private var currentUserUuid: String? = null
    private val mockUsers = mutableMapOf<String, User>()
    
    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        delay(500) // ネットワーク遅延をシミュレート
        
        // モックユーザーを作成
        val uuid = "mock-user-${idToken.hashCode()}"
        val now = System.currentTimeMillis()
        val user = User(
            uuid = uuid,
            googleId = "google-id-${idToken.hashCode()}",
            nickname = "GoogleUser",
            bio = "Google Sign-In user",
            avatarUrl = null,
            totalEncounters = 0,
            achievements = emptyList(),
            isActive = true,
            createdAt = now,
            updatedAt = now
        )
        
        mockUsers[uuid] = user
        currentUserUuid = uuid
        
        return Result.success(user)
    }
    
    override suspend fun signUp(
        nickname: String,
        bio: String?,
        avatarData: ByteArray?
    ): Result<User> {
        delay(500)
        
        val uuid = currentUserUuid ?: return Result.failure(
            IllegalStateException("サインインが必要です")
        )
        
        val existingUser = mockUsers[uuid] ?: return Result.failure(
            IllegalStateException("ユーザーが見つかりません")
        )
        
        val updatedUser = existingUser.copy(
            nickname = nickname,
            bio = bio
        )
        
        mockUsers[uuid] = updatedUser
        
        return Result.success(updatedUser)
    }
    
    override suspend fun signOut(): Result<Unit> {
        delay(300)
        currentUserUuid = null
        return Result.success(Unit)
    }
    
    override suspend fun isSessionValid(): Result<Boolean> {
        delay(100)
        return Result.success(currentUserUuid != null)
    }
    
    override suspend fun refreshToken(): Result<String> {
        delay(200)
        return if (currentUserUuid != null) {
            Result.success("mock-token-${System.currentTimeMillis()}")
        } else {
            Result.failure(IllegalStateException("セッションが無効です"))
        }
    }
    
    override suspend fun getCurrentUserUuid(): Result<String?> {
        delay(50)
        return Result.success(currentUserUuid)
    }
}
