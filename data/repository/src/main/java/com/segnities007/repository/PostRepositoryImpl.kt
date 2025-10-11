package com.segnities007.repository

import com.segnities007.model.Post
import com.segnities007.model.exception.DomainException
import com.segnities007.repository.PostRepository
import com.segnities007.remote.datasource.PostRemoteDataSource
import com.segnities007.db.dao.PostDao

class PostRepositoryImpl(
    private val remoteDataSource: PostRemoteDataSource,
    private val postDao: PostDao? = null // オプショナル: キャッシュ無効化可能
) : PostRepository {
    
    override suspend fun getTimeline(
        encounterUuids: List<String>,
        encounterTimestamps: Map<String, Long>,
        limit: Int,
        offset: Int
    ): Result<List<Post>> {
        // Remote取得（キャッシュはここでは省略、将来的に実装）
        return remoteDataSource.getTimeline(
            encounterUuids = encounterUuids,
            encounterTimestamps = encounterTimestamps,
            limit = limit,
            offset = offset
        )
    }
    
    override suspend fun getUserPosts(
        userUuid: String,
        encounterTimestamp: Long?,
        limit: Int,
        offset: Int
    ): Result<List<Post>> {
        return remoteDataSource.getUserPosts(
            userUuid = userUuid,
            encounterTimestamp = encounterTimestamp,
            limit = limit,
            offset = offset
        )
    }
    
    override suspend fun createPost(
        content: String,
        mediaData: ByteArray?,
        mediaType: Post.MediaType?
    ): Result<Post> {
        // コンテンツバリデーション
        if (!Post.isContentValid(content)) {
            return Result.failure(
                DomainException.ValidationError(
                    field = "content",
                    message = "投稿は${Post.MIN_CONTENT_LENGTH}～${Post.MAX_CONTENT_LENGTH}文字で入力してください"
                )
            )
        }
        
        return remoteDataSource.createPost(content, mediaData, mediaType)
    }
    
    override suspend fun updatePost(
        postId: String,
        content: String
    ): Result<Post> {
        if (!Post.isContentValid(content)) {
            return Result.failure(
                DomainException.ValidationError(
                    field = "content",
                    message = "投稿は${Post.MIN_CONTENT_LENGTH}～${Post.MAX_CONTENT_LENGTH}文字で入力してください"
                )
            )
        }
        
        return remoteDataSource.updatePost(postId, content)
    }
    
    override suspend fun deletePost(postId: String): Result<Unit> {
        return remoteDataSource.deletePost(postId)
    }
    
    override suspend fun searchPosts(
        query: String,
        encounterUuids: List<String>,
        encounterTimestamps: Map<String, Long>
    ): Result<List<Post>> {
        return remoteDataSource.searchPosts(
            query = query,
            encounterUuids = encounterUuids,
            encounterTimestamps = encounterTimestamps
        )
    }
    
    override suspend fun likePost(postId: String): Result<Post> {
        return remoteDataSource.likePost(postId)
    }
    
    override suspend fun unlikePost(postId: String): Result<Post> {
        return remoteDataSource.unlikePost(postId)
    }
    
    override suspend fun reportPost(
        postId: String,
        reason: String,
        description: String?
    ): Result<Unit> {
        return remoteDataSource.reportPost(postId, reason, description)
    }
}
