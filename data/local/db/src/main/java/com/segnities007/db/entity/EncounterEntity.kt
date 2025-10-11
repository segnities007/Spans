package com.segnities007.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.segnities007.db.converter.IntListConverter
import com.segnities007.model.Encounter

@Entity(
    tableName = "encounters",
    indices = [
        Index(value = ["user_uuid_a"]),
        Index(value = ["user_uuid_b"]),
        Index(value = ["last_encounter_at"])
    ]
)
@TypeConverters(IntListConverter::class)
data class EncounterEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    
    @ColumnInfo(name = "user_uuid_a")
    val userUuidA: String,
    
    @ColumnInfo(name = "user_uuid_b")
    val userUuidB: String,
    
    @ColumnInfo(name = "user_a_nickname")
    val userANickname: String?,
    
    @ColumnInfo(name = "user_b_nickname")
    val userBNickname: String?,
    
    @ColumnInfo(name = "user_a_avatar")
    val userAAvatar: String?,
    
    @ColumnInfo(name = "user_b_avatar")
    val userBAvatar: String?,
    
    @ColumnInfo(name = "last_encounter_at")
    val lastEncounterAt: Long,
    
    @ColumnInfo(name = "encounter_count")
    val encounterCount: Int,
    
    @ColumnInfo(name = "average_rssi")
    val averageRssi: Int?,
    
    @ColumnInfo(name = "rssi_history")
    val rssiHistory: List<Int>,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
) {
    fun toDomainModel(): Encounter {
        return Encounter(
            id = id,
            userUuidA = userUuidA,
            userUuidB = userUuidB,
            userANickname = userANickname,
            userBNickname = userBNickname,
            userAAvatar = userAAvatar,
            userBAvatar = userBAvatar,
            lastEncounterAt = lastEncounterAt,
            encounterCount = encounterCount,
            averageRssi = averageRssi,
            rssiHistory = rssiHistory,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
    
    companion object {
        fun fromDomainModel(encounter: Encounter): EncounterEntity {
            return EncounterEntity(
                id = encounter.id,
                userUuidA = encounter.userUuidA,
                userUuidB = encounter.userUuidB,
                userANickname = encounter.userANickname,
                userBNickname = encounter.userBNickname,
                userAAvatar = encounter.userAAvatar,
                userBAvatar = encounter.userBAvatar,
                lastEncounterAt = encounter.lastEncounterAt,
                encounterCount = encounter.encounterCount,
                averageRssi = encounter.averageRssi,
                rssiHistory = encounter.rssiHistory,
                createdAt = encounter.createdAt,
                updatedAt = encounter.updatedAt
            )
        }
    }
}
