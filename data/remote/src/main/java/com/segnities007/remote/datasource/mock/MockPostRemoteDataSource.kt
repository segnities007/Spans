package com.segnities007.remote.datasource.mock

import com.segnities007.model.Post
import com.segnities007.remote.datasource.PostRemoteDataSource
import kotlinx.coroutines.delay
import java.time.Instant

/**
 * PostRemoteDataSourceのモック実装
 * 
 * Supabase実装前のテスト用
 */
class MockPostRemoteDataSource : PostRemoteDataSource {
    
    private val mockPosts = mutableListOf<Post>()
    private var nextId = 1
    
    init {
        // サンプルデータ
        mockPosts.add(
            Post(
                id = "mock-post-1",
                authorUuid = "mock-user-1",
                authorNickname = "Taro",
                authorAvatar = null,
                content = "すれ違いSNS、始めました！",
                mediaUrl = null,
                mediaType = null,
                thumbnailUrl = null,
                likeCount = 5,
                isLikedByMe = false,
                isDeleted = false,
                createdAt = Instant.now().minusSeconds(3600).toEpochMilli(),
                updatedAt = Instant.now().minusSeconds(3600).toEpochMilli()
            )
        )
        mockPosts.add(
            Post(
                id = "mock-post-2",
                authorUuid = "mock-user-2",
                authorNickname = "Hanako",
                authorAvatar = null,
                content = "今日は良い天気ですね",
                mediaUrl = null,
                mediaType = null,
                thumbnailUrl = null,
                likeCount = 3,
                isLikedByMe = false,
                isDeleted = false,
                createdAt = Instant.now().minusSeconds(1800).toEpochMilli(),
                updatedAt = Instant.now().minusSeconds(1800).toEpochMilli()
            )
        )
        nextId = 3
    }
    
    override suspend fun getTimeline(
        encounterUuids: List<String>,
        encounterTimestamps: Map<String, Long>,
        limit: Int,
        offset: Int
    ): Result<List<Post>> {
        delay(500)
        
        val filteredPosts = mockPosts
            .filter { !it.isDeleted }
            .filter { it.authorUuid in encounterUuids }
            .filter { post ->
                val encounterTimestamp = encounterTimestamps[post.authorUuid] ?: 0L
                post.createdAt >= encounterTimestamp
            }
            .sortedByDescending { it.createdAt }
            .drop(offset)
            .take(limit)
        
        return Result.success(filteredPosts)
    }
    
    override suspend fun getUserPosts(
        userUuid: String,
        encounterTimestamp: Long?,
        limit: Int,
        offset: Int
    ): Result<List<Post>> {
        delay(400)
        
        val filteredPosts = mockPosts
            .filter { !it.isDeleted }
            .filter { it.authorUuid == userUuid }
            .filter { encounterTimestamp == null || it.createdAt >= encounterTimestamp }
            .sortedByDescending { it.createdAt }
            .drop(offset)
            .take(limit)
        
        return Result.success(filteredPosts)
    }
    
    override suspend fun createPost(
        content: String,
        mediaData: ByteArray?,
        mediaType: Post.MediaType?
    ): Result<Post> {
        delay(600)
        
        val now = Instant.now().toEpochMilli()
        val post = Post(
            id = "mock-post-${nextId++}",
            authorUuid = "current-user-uuid",
            authorNickname = "MyUser",
            authorAvatar = null,
            content = content,
            mediaUrl = mediaData?.let { "mock-media-url" },
            mediaType = mediaType,
            thumbnailUrl = if (mediaType == Post.MediaType.VIDEO) "mock-thumbnail-url" else null,
            likeCount = 0,
            isLikedByMe = false,
            isDeleted = false,
            createdAt = now,
            updatedAt = now
        )
        
        mockPosts.add(post)
        
        return Result.success(post)
    }
    
    override suspend fun updatePost(
        postId: String,
        content: String
    ): Result<Post> {
        delay(500)
        
        val index = mockPosts.indexOfFirst { it.id == postId }
        if (index == -1) {
            return Result.failure(NoSuchElementException("投稿が見つかりません"))
        }
        
        val updatedPost = mockPosts[index].copy(
            content = content,
            updatedAt = Instant.now().toEpochMilli()
        )
        
        mockPosts[index] = updatedPost
        
        return Result.success(updatedPost)
    }
    
    override suspend fun deletePost(postId: String): Result<Unit> {
        delay(400)
        
        val index = mockPosts.indexOfFirst { it.id == postId }
        if (index == -1) {
            return Result.failure(NoSuchElementException("投稿が見つかりません"))
        }
        
        mockPosts[index] = mockPosts[index].copy(isDeleted = true)
        
        return Result.success(Unit)
    }
    
    override suspend fun searchPosts(
        query: String,
        encounterUuids: List<String>,
        encounterTimestamps: Map<String, Long>
    ): Result<List<Post>> {
        delay(600)
        
        val filteredPosts = mockPosts
            .filter { !it.isDeleted }
            .filter { it.authorUuid in encounterUuids }
            .filter { post ->
                val encounterTimestamp = encounterTimestamps[post.authorUuid] ?: 0L
                post.createdAt >= encounterTimestamp
            }
            .filter { it.content.contains(query, ignoreCase = true) }
            .sortedByDescending { it.createdAt }
        
        return Result.success(filteredPosts)
    }
    
    override suspend fun likePost(postId: String): Result<Post> {
        delay(300)
        
        val index = mockPosts.indexOfFirst { it.id == postId }
        if (index == -1) {
            return Result.failure(NoSuchElementException("投稿が見つかりません"))
        }
        
        val updatedPost = mockPosts[index].copy(
            isLikedByMe = true,
            likeCount = mockPosts[index].likeCount + 1
        )
        
        mockPosts[index] = updatedPost
        
        return Result.success(updatedPost)
    }
    
    override suspend fun unlikePost(postId: String): Result<Post> {
        delay(300)
        
        val index = mockPosts.indexOfFirst { it.id == postId }
        if (index == -1) {
            return Result.failure(NoSuchElementException("投稿が見つかりません"))
        }
        
        val updatedPost = mockPosts[index].copy(
            isLikedByMe = false,
            likeCount = maxOf(0, mockPosts[index].likeCount - 1)
        )
        
        mockPosts[index] = updatedPost
        
        return Result.success(updatedPost)
    }
    
    override suspend fun reportPost(
        postId: String,
        reason: String,
        description: String?
    ): Result<Unit> {
        delay(400)
        
        // モックなので何もしない
        return Result.success(Unit)
    }
}
