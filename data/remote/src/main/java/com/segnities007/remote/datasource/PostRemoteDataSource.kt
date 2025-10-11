package com.segnities007.remote.datasource

import com.segnities007.model.Post

/**
 * 投稿関連のリモートデータソースインターフェース
 * 
 * Supabase PostgREST APIとの通信を担当
 */
interface PostRemoteDataSource {
    
    /**
     * タイムライン取得
     */
    suspend fun getTimeline(
        encounterUuids: List<String>,
        encounterTimestamps: Map<String, Long>,
        limit: Int,
        offset: Int
    ): Result<List<Post>>
    
    /**
     * ユーザーの投稿一覧取得
     */
    suspend fun getUserPosts(
        userUuid: String,
        encounterTimestamp: Long?,
        limit: Int,
        offset: Int
    ): Result<List<Post>>
    
    /**
     * 投稿作成
     */
    suspend fun createPost(
        content: String,
        mediaData: ByteArray?,
        mediaType: Post.MediaType?
    ): Result<Post>
    
    /**
     * 投稿更新
     */
    suspend fun updatePost(
        postId: String,
        content: String
    ): Result<Post>
    
    /**
     * 投稿削除（論理削除）
     */
    suspend fun deletePost(postId: String): Result<Unit>
    
    /**
     * 投稿検索
     */
    suspend fun searchPosts(
        query: String,
        encounterUuids: List<String>,
        encounterTimestamps: Map<String, Long>
    ): Result<List<Post>>
    
    /**
     * いいね追加
     */
    suspend fun likePost(postId: String): Result<Post>
    
    /**
     * いいね削除
     */
    suspend fun unlikePost(postId: String): Result<Post>
    
    /**
     * 投稿を通報
     */
    suspend fun reportPost(
        postId: String,
        reason: String,
        description: String?
    ): Result<Unit>
}
