package com.segnities007.repository

import com.segnities007.model.User

interface UserRepository {

    suspend fun getMyProfile(): Result<User>

    suspend fun getUserByUuid(uuid: String): Result<User>

    suspend fun updateProfile(
        nickname: String? = null,
        bio: String? = null,
        avatarUrl: String? = null
    ): Result<User>

    suspend fun uploadAvatar(imageData: ByteArray): Result<String>

    suspend fun searchUsers(
        query: String,
        encounterUuids: List<String>
    ): Result<List<User>>

    suspend fun blockUser(uuid: String): Result<Unit>

    suspend fun unblockUser(uuid: String): Result<Unit>

    suspend fun getBlockedUsers(): Result<List<String>>

    suspend fun deleteAccount(): Result<Unit>
}
