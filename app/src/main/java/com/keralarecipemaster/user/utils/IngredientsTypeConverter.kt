package com.keralarecipemaster.user.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.keralarecipemaster.user.domain.model.Ingredient
import java.lang.reflect.Type


class IngredientsTypeConverter {
    @TypeConverter
    fun fromIngredientsList(ingredients: List<Ingredient> ): String? {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Ingredient?>?>() {}.type
        return gson.toJson(ingredients, type)
    }

    @TypeConverter
    fun toIngredientsList(ingredients: String): List<Ingredient> {
        val gson = Gson()
        val type = object : TypeToken<List<Ingredient?>?>() {}.type
        return gson.fromJson(ingredients, type)
    }
}
