package com.segnities007.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.segnities007.db.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostById(postId: String): PostEntity?
    
    @Query("SELECT * FROM posts ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    suspend fun getPosts(limit: Int, offset: Int): List<PostEntity>
    
    @Query("SELECT * FROM posts WHERE author_uuid = :authorUuid ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    suspend fun getPostsByAuthor(authorUuid: String, limit: Int, offset: Int): List<PostEntity>
    
    @Query("SELECT * FROM posts WHERE author_uuid IN (:authorUuids) ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    suspend fun getPostsByAuthors(authorUuids: List<String>, limit: Int, offset: Int): List<PostEntity>
    
    @Query("SELECT * FROM posts WHERE content LIKE '%' || :query || '%' ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    suspend fun searchPosts(query: String, limit: Int, offset: Int): List<PostEntity>
    
    @Query("SELECT * FROM posts ORDER BY created_at DESC")
    fun observePosts(): Flow<List<PostEntity>>
    
    @Query("SELECT * FROM posts WHERE cached_at < :expiryTime")
    suspend fun getExpiredPosts(expiryTime: Long): List<PostEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)
    
    @Update
    suspend fun updatePost(post: PostEntity)
    
    @Delete
    suspend fun deletePost(post: PostEntity)
    
    @Query("DELETE FROM posts WHERE id = :postId")
    suspend fun deletePostById(postId: String)
    
    @Query("DELETE FROM posts WHERE cached_at < :expiryTime")
    suspend fun deleteExpiredPosts(expiryTime: Long): Int
    
    @Query("DELETE FROM posts")
    suspend fun deleteAllPosts()
}
