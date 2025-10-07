package com.segnities007.db.converter

import androidx.room.TypeConverter

class IntListConverter {
    @TypeConverter
    fun fromIntList(value: List<Int>): String {
        return value.joinToString(",")
    }
    
    @TypeConverter
    fun toIntList(value: String): List<Int> {
        if (value.isBlank()) return emptyList()
        return value.split(",").mapNotNull { it.toIntOrNull() }
    }
}
