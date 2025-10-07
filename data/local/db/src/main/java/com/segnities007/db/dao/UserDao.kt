package com.segnities007.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.segnities007.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE uuid = :uuid")
    suspend fun getUserByUuid(uuid: String): UserEntity?
    
    @Query("SELECT * FROM users WHERE uuid = :uuid")
    fun observeUserByUuid(uuid: String): Flow<UserEntity?>
    
    @Query("SELECT * FROM users WHERE nickname LIKE '%' || :query || '%' LIMIT :limit OFFSET :offset")
    suspend fun searchUsers(query: String, limit: Int, offset: Int): List<UserEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)
    
    @Update
    suspend fun updateUser(user: UserEntity)
    
    @Delete
    suspend fun deleteUser(user: UserEntity)
    
    @Query("DELETE FROM users WHERE uuid = :uuid")
    suspend fun deleteUserByUuid(uuid: String)
    
    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}
