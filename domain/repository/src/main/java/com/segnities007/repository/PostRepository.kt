package com.segnities007.repository

import com.segnities007.model.Post

interface PostRepository {

    suspend fun getTimeline(
        encounterUuids: List<String>,
        encounterTimestamps: Map<String, Long>,
        limit: Int = 20,
        offset: Int = 0
    ): Result<List<Post>>

    suspend fun getUserPosts(
        userUuid: String,
        encounterTimestamp: Long? = null,
        limit: Int = 20,
        offset: Int = 0
    ): Result<List<Post>>

    suspend fun createPost(
        content: String,
        mediaData: ByteArray? = null,
        mediaType: Post.MediaType? = null
    ): Result<Post>

    suspend fun updatePost(
        postId: String,
        content: String
    ): Result<Post>

    suspend fun deletePost(postId: String): Result<Unit>

    suspend fun searchPosts(
        query: String,
        encounterUuids: List<String>,
        encounterTimestamps: Map<String, Long>
    ): Result<List<Post>>

    suspend fun likePost(postId: String): Result<Post>

    suspend fun unlikePost(postId: String): Result<Post>

    suspend fun reportPost(
        postId: String,
        reason: String,
        description: String? = null
    ): Result<Unit>
}
