package com.segnities007.remote.datasource

import com.segnities007.model.User

/**
 * 認証関連のリモートデータソースインターフェース
 * 
 * Supabase Auth APIとの通信を担当
 */
interface AuthRemoteDataSource {
    
    /**
     * Google IDトークンでサインイン
     */
    suspend fun signInWithGoogle(idToken: String): Result<User>
    
    /**
     * 新規ユーザー登録
     */
    suspend fun signUp(
        nickname: String,
        bio: String?,
        avatarData: ByteArray?
    ): Result<User>
    
    /**
     * サインアウト
     */
    suspend fun signOut(): Result<Unit>
    
    /**
     * セッションの有効性確認
     */
    suspend fun isSessionValid(): Result<Boolean>
    
    /**
     * トークンのリフレッシュ
     */
    suspend fun refreshToken(): Result<String>
    
    /**
     * 現在のユーザーUUIDを取得
     */
    suspend fun getCurrentUserUuid(): Result<String?>
}
