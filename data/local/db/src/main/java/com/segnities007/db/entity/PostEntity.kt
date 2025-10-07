package com.segnities007.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.segnities007.model.Post

@Entity(
    tableName = "posts",
    indices = [
        Index(value = ["author_uuid"]),
        Index(value = ["created_at"])
    ]
)
data class PostEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    
    @ColumnInfo(name = "author_uuid")
    val authorUuid: String,
    
    @ColumnInfo(name = "author_nickname")
    val authorNickname: String,
    
    @ColumnInfo(name = "author_avatar_url")
    val authorAvatarUrl: String?,
    
    @ColumnInfo(name = "content")
    val content: String,
    
    @ColumnInfo(name = "media_url")
    val mediaUrl: String?,
    
    @ColumnInfo(name = "media_type")
    val mediaType: String?,
    
    @ColumnInfo(name = "like_count")
    val likeCount: Int,
    
    @ColumnInfo(name = "is_liked")
    val isLiked: Boolean,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    
    @ColumnInfo(name = "cached_at")
    val cachedAt: Long
) {
    fun toDomainModel(): Post {
        val domainMediaType = mediaType?.let { type ->
            Post.MediaType.entries.find { it.name == type }
        }
        
        return Post(
            id = id,
            authorUuid = authorUuid,
            authorNickname = authorNickname,
            authorAvatar = authorAvatarUrl,
            content = content,
            mediaUrl = mediaUrl,
            mediaType = domainMediaType,
            thumbnailUrl = null,
            likeCount = likeCount,
            isLikedByMe = isLiked,
            isDeleted = false,
            createdAt = createdAt,
            updatedAt = createdAt
        )
    }
    
    companion object {
        fun fromDomainModel(post: Post, cachedAt: Long = System.currentTimeMillis()): PostEntity {
            return PostEntity(
                id = post.id,
                authorUuid = post.authorUuid,
                authorNickname = post.authorNickname,
                authorAvatarUrl = post.authorAvatar,
                content = post.content,
                mediaUrl = post.mediaUrl,
                mediaType = post.mediaType?.name,
                likeCount = post.likeCount,
                isLiked = post.isLikedByMe,
                createdAt = post.createdAt,
                cachedAt = cachedAt
            )
        }
    }
}
