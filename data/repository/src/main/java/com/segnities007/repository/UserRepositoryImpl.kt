package com.segnities007.repository

import com.segnities007.model.User
import com.segnities007.repository.UserRepository
import com.segnities007.remote.datasource.UserRemoteDataSource
import com.segnities007.db.dao.UserDao

class UserRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource,
    private val userDao: UserDao? = null
) : UserRepository {
    
    override suspend fun getMyProfile(): Result<User> {
        return remoteDataSource.getMyProfile()
    }
    
    override suspend fun getUserByUuid(uuid: String): Result<User> {
        return remoteDataSource.getUserByUuid(uuid)
    }
    
    override suspend fun updateProfile(
        nickname: String?,
        bio: String?,
        avatarUrl: String?
    ): Result<User> {
        return remoteDataSource.updateProfile(nickname, bio, avatarUrl)
    }
    
    override suspend fun uploadAvatar(imageData: ByteArray): Result<String> {
        return remoteDataSource.uploadAvatar(imageData)
    }
    
    override suspend fun searchUsers(
        query: String,
        encounterUuids: List<String>
    ): Result<List<User>> {
        return remoteDataSource.searchUsers(query, encounterUuids)
    }
    
    override suspend fun blockUser(uuid: String): Result<Unit> {
        return remoteDataSource.blockUser(uuid)
    }
    
    override suspend fun unblockUser(uuid: String): Result<Unit> {
        return remoteDataSource.unblockUser(uuid)
    }
    
    override suspend fun getBlockedUsers(): Result<List<String>> {
        return remoteDataSource.getBlockedUsers()
    }
    
    override suspend fun deleteAccount(): Result<Unit> {
        return remoteDataSource.deleteAccount()
    }
}
