package com.segnities007.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.segnities007.db.converter.StringListConverter
import com.segnities007.model.User

@Entity(tableName = "users")
@TypeConverters(StringListConverter::class)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    val uuid: String,
    
    @ColumnInfo(name = "google_id")
    val googleId: String,
    
    @ColumnInfo(name = "nickname")
    val nickname: String,
    
    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String?,
    
    @ColumnInfo(name = "bio")
    val bio: String?,
    
    @ColumnInfo(name = "total_encounters")
    val totalEncounters: Int,
    
    @ColumnInfo(name = "achievements")
    val achievements: List<String>,
    
    @ColumnInfo(name = "is_active")
    val isActive: Boolean,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
) {
    fun toDomainModel(): User {
        return User(
            uuid = uuid,
            googleId = googleId,
            nickname = nickname,
            avatarUrl = avatarUrl,
            bio = bio,
            totalEncounters = totalEncounters,
            achievements = achievements,
            isActive = isActive,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
    
    companion object {
        fun fromDomainModel(user: User): UserEntity {
            return UserEntity(
                uuid = user.uuid,
                googleId = user.googleId,
                nickname = user.nickname,
                avatarUrl = user.avatarUrl,
                bio = user.bio,
                totalEncounters = user.totalEncounters,
                achievements = user.achievements,
                isActive = user.isActive,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt
            )
        }
    }
}
