package com.segnities007.remote.datasource.mock

import com.segnities007.model.User
import com.segnities007.remote.datasource.UserRemoteDataSource
import kotlinx.coroutines.delay

/**
 * UserRemoteDataSourceのモック実装
 * 
 * Supabase実装前のテスト用
 */
class MockUserRemoteDataSource : UserRemoteDataSource {
    
    private val mockUsers = mutableMapOf<String, User>()
    private val blockedUsers = mutableSetOf<String>()
    private var currentUserUuid: String = "current-user-uuid"
    
    init {
        // サンプルデータ
        val now = System.currentTimeMillis()
        mockUsers["current-user-uuid"] = User(
            uuid = "current-user-uuid",
            googleId = "google-id-current",
            nickname = "MyUser",
            bio = "よろしくお願いします",
            avatarUrl = null,
            totalEncounters = 0,
            achievements = emptyList(),
            isActive = true,
            createdAt = now,
            updatedAt = now
        )
        mockUsers["mock-user-1"] = User(
            uuid = "mock-user-1",
            googleId = "google-id-1",
            nickname = "Taro",
            bio = "カメラが趣味です",
            avatarUrl = null,
            totalEncounters = 0,
            achievements = emptyList(),
            isActive = true,
            createdAt = now,
            updatedAt = now
        )
        mockUsers["mock-user-2"] = User(
            uuid = "mock-user-2",
            googleId = "google-id-2",
            nickname = "Hanako",
            bio = "音楽が好きです",
            avatarUrl = null,
            totalEncounters = 0,
            achievements = emptyList(),
            isActive = true,
            createdAt = now,
            updatedAt = now
        )
    }
    
    override suspend fun getMyProfile(): Result<User> {
        delay(300)
        
        val user = mockUsers[currentUserUuid]
        return if (user != null) {
            Result.success(user)
        } else {
            Result.failure(NoSuchElementException("ユーザーが見つかりません"))
        }
    }
    
    override suspend fun getUserByUuid(uuid: String): Result<User> {
        delay(300)
        
        val user = mockUsers[uuid]
        return if (user != null && user.isActive) {
            Result.success(user)
        } else {
            Result.failure(NoSuchElementException("ユーザーが見つかりません"))
        }
    }
    
    override suspend fun updateProfile(
        nickname: String?,
        bio: String?,
        avatarUrl: String?
    ): Result<User> {
        delay(500)
        
        val currentUser = mockUsers[currentUserUuid]
            ?: return Result.failure(NoSuchElementException("ユーザーが見つかりません"))
        
        val updatedUser = currentUser.copy(
            nickname = nickname ?: currentUser.nickname,
            bio = bio ?: currentUser.bio,
            avatarUrl = avatarUrl ?: currentUser.avatarUrl
        )
        
        mockUsers[currentUserUuid] = updatedUser
        
        return Result.success(updatedUser)
    }
    
    override suspend fun uploadAvatar(imageData: ByteArray): Result<String> {
        delay(1000) // アップロードの遅延をシミュレート
        
        // モックURLを返す
        return Result.success("mock-avatar-url-${System.currentTimeMillis()}")
    }
    
    override suspend fun searchUsers(
        query: String,
        encounterUuids: List<String>
    ): Result<List<User>> {
        delay(500)
        
        val results = mockUsers.values
            .filter { it.isActive }
            .filter { it.uuid in encounterUuids }
            .filter { it.nickname.contains(query, ignoreCase = true) || 
                     (it.bio?.contains(query, ignoreCase = true) == true) }
            .toList()
        
        return Result.success(results)
    }
    
    override suspend fun blockUser(uuid: String): Result<Unit> {
        delay(400)
        
        blockedUsers.add(uuid)
        return Result.success(Unit)
    }
    
    override suspend fun unblockUser(uuid: String): Result<Unit> {
        delay(400)
        
        blockedUsers.remove(uuid)
        return Result.success(Unit)
    }
    
    override suspend fun getBlockedUsers(): Result<List<String>> {
        delay(300)
        
        return Result.success(blockedUsers.toList())
    }
    
    override suspend fun deleteAccount(): Result<Unit> {
        delay(500)
        
        mockUsers[currentUserUuid]?.let {
            mockUsers[currentUserUuid] = it.copy(isActive = false)
        }
        
        return Result.success(Unit)
    }
}
