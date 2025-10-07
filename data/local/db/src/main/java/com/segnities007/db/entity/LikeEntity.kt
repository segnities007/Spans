package com.segnities007.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.segnities007.model.Like

@Entity(
    tableName = "likes",
    indices = [
        Index(value = ["post_id"]),
        Index(value = ["user_uuid"])
    ]
)
data class LikeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    
    @ColumnInfo(name = "post_id")
    val postId: String,
    
    @ColumnInfo(name = "user_uuid")
    val userUuid: String,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long
) {
    fun toDomainModel(): Like {
        return Like(
            postId = postId,
            userUuid = userUuid,
            createdAt = createdAt
        )
    }
    
    companion object {
        fun fromDomainModel(like: Like, id: Long = 0): LikeEntity {
            return LikeEntity(
                id = id,
                postId = like.postId,
                userUuid = like.userUuid,
                createdAt = like.createdAt
            )
        }
    }
}
