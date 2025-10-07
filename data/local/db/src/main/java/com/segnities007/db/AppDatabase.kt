package com.segnities007.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.segnities007.db.converter.IntListConverter
import com.segnities007.db.converter.StringListConverter
import com.segnities007.db.dao.EncounterDao
import com.segnities007.db.dao.LikeDao
import com.segnities007.db.dao.PostDao
import com.segnities007.db.dao.UserDao
import com.segnities007.db.entity.EncounterEntity
import com.segnities007.db.entity.LikeEntity
import com.segnities007.db.entity.PostEntity
import com.segnities007.db.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        PostEntity::class,
        EncounterEntity::class,
        LikeEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    IntListConverter::class,
    StringListConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun encounterDao(): EncounterDao
    abstract fun likeDao(): LikeDao
    
    companion object {
        const val DATABASE_NAME = "spans_database"
    }
}
