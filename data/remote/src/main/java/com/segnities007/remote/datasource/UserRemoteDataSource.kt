package com.segnities007.remote.datasource

import com.segnities007.model.User

/**
 * ユーザー関連のリモートデータソースインターフェース
 * 
 * Supabase PostgREST APIとの通信を担当
 */
interface UserRemoteDataSource {
    
    /**
     * 自分のプロフィール取得
     */
    suspend fun getMyProfile(): Result<User>
    
    /**
     * UUIDでユーザー取得
     */
    suspend fun getUserByUuid(uuid: String): Result<User>
    
    /**
     * プロフィール更新
     */
    suspend fun updateProfile(
        nickname: String?,
        bio: String?,
        avatarUrl: String?
    ): Result<User>
    
    /**
     * アバター画像をアップロード
     */
    suspend fun uploadAvatar(imageData: ByteArray): Result<String>
    
    /**
     * ユーザー検索
     */
    suspend fun searchUsers(
        query: String,
        encounterUuids: List<String>
    ): Result<List<User>>
    
    /**
     * ユーザーをブロック
     */
    suspend fun blockUser(uuid: String): Result<Unit>
    
    /**
     * ブロック解除
     */
    suspend fun unblockUser(uuid: String): Result<Unit>
    
    /**
     * ブロックリスト取得
     */
    suspend fun getBlockedUsers(): Result<List<String>>
    
    /**
     * アカウント削除
     */
    suspend fun deleteAccount(): Result<Unit>
}
