package com.segnities007.db.converter

import androidx.room.TypeConverter

class StringListConverter {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Json.encodeToString(value)
    }
    
    @TypeConverter
    fun toStringList(value: String): List<String> {
        if (value.isBlank()) return emptyList()
        return value.split(",")
    }
}
