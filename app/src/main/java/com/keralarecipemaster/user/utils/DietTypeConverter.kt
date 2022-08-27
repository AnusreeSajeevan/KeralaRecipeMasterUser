package com.keralarecipemaster.user.utils

import androidx.room.TypeConverter

class DietTypeConverter {

    @TypeConverter
    fun fromDiet(diet: Diet): String {
        return diet.name
    }

    @TypeConverter
    fun toDiet(name: String): Diet {
        return Diet.valueOf(name)
    }
}
