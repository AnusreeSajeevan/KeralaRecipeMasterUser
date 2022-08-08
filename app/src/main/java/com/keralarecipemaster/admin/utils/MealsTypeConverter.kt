package com.keralarecipemaster.admin.utils

import androidx.room.TypeConverter

class MealsTypeConverter {

    @TypeConverter
    fun fromMeals(meals: Meal): String {
        return meals.name
    }

    @TypeConverter
    fun toMeals(name: String): Meal {
        return Meal.valueOf(name)
    }
}