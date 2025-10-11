package com.segnities007.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.segnities007.db.entity.EncounterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EncounterDao {
    @Query("SELECT * FROM encounters WHERE id = :id")
    suspend fun getEncounterById(id: String): EncounterEntity?
    
    @Query("SELECT * FROM encounters ORDER BY last_encounter_at DESC LIMIT :limit OFFSET :offset")
    suspend fun getEncounters(limit: Int, offset: Int): List<EncounterEntity>
    
    @Query("SELECT * FROM encounters WHERE user_uuid_a = :userUuid OR user_uuid_b = :userUuid ORDER BY last_encounter_at DESC")
    suspend fun getEncountersByUser(userUuid: String): List<EncounterEntity>
    
    @Query("SELECT * FROM encounters WHERE last_encounter_at >= :startTime AND last_encounter_at <= :endTime ORDER BY last_encounter_at DESC")
    suspend fun getEncountersInTimeRange(startTime: Long, endTime: Long): List<EncounterEntity>
    
    @Query("SELECT * FROM encounters ORDER BY last_encounter_at DESC")
    fun observeEncounters(): Flow<List<EncounterEntity>>
    
    @Query("SELECT COUNT(*) FROM encounters")
    suspend fun getEncounterCount(): Int
    
    @Query("SELECT COUNT(DISTINCT CASE WHEN user_uuid_a = :userUuid THEN user_uuid_b ELSE user_uuid_a END) FROM encounters WHERE user_uuid_a = :userUuid OR user_uuid_b = :userUuid")
    suspend fun getUniquePartnerCount(userUuid: String): Int
    
    @Query("SELECT * FROM encounters WHERE last_encounter_at >= :since ORDER BY last_encounter_at DESC")
    suspend fun getRecentEncounters(since: Long): List<EncounterEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEncounter(encounter: EncounterEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEncounters(encounters: List<EncounterEntity>)
    
    @Update
    suspend fun updateEncounter(encounter: EncounterEntity)
    
    @Delete
    suspend fun deleteEncounter(encounter: EncounterEntity)
    
    @Query("DELETE FROM encounters WHERE id = :id")
    suspend fun deleteEncounterById(id: String)
    
    @Query("DELETE FROM encounters WHERE last_encounter_at < :expiryTime")
    suspend fun deleteOldEncounters(expiryTime: Long): Int
    
    @Query("DELETE FROM encounters")
    suspend fun deleteAllEncounters()
}
