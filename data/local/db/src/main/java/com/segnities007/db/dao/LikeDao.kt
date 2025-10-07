package com.segnities007.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.segnities007.db.entity.LikeEntity

@Dao
interface LikeDao {
    @Query("SELECT * FROM likes WHERE post_id = :postId AND user_uuid = :userUuid")
    suspend fun getLike(postId: String, userUuid: String): LikeEntity?
    
    @Query("SELECT * FROM likes WHERE post_id = :postId")
    suspend fun getLikesByPost(postId: String): List<LikeEntity>
    
    @Query("SELECT * FROM likes WHERE user_uuid = :userUuid ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    suspend fun getLikesByUser(userUuid: String, limit: Int, offset: Int): List<LikeEntity>
    
    @Query("SELECT COUNT(*) FROM likes WHERE post_id = :postId")
    suspend fun getLikeCount(postId: String): Int
    
    @Query("SELECT EXISTS(SELECT 1 FROM likes WHERE post_id = :postId AND user_uuid = :userUuid)")
    suspend fun isLiked(postId: String, userUuid: String): Boolean
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLike(like: LikeEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikes(likes: List<LikeEntity>)
    
    @Delete
    suspend fun deleteLike(like: LikeEntity)
    
    @Query("DELETE FROM likes WHERE post_id = :postId AND user_uuid = :userUuid")
    suspend fun deleteLike(postId: String, userUuid: String)
    
    @Query("DELETE FROM likes WHERE post_id = :postId")
    suspend fun deleteLikesByPost(postId: String)
    
    @Query("DELETE FROM likes")
    suspend fun deleteAllLikes()
}
