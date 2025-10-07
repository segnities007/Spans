package com.segnities007.repository

import com.segnities007.model.User
import com.segnities007.model.exception.DomainException
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
        // バリデーション
        if (nickname != null && !User.isNicknameValid(nickname)) {
            return Result.failure(
                DomainException.ValidationError(
                    field = "nickname",
                    message = "ニックネームは${User.MIN_NICKNAME_LENGTH}～${User.MAX_NICKNAME_LENGTH}文字で入力してください"
                )
            )
        }
        
        if (bio != null && bio.length > User.MAX_BIO_LENGTH) {
            return Result.failure(
                DomainException.ValidationError(
                    field = "bio",
                    message = "自己紹介は${User.MAX_BIO_LENGTH}文字以内で入力してください"
                )
            )
        }
        
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
