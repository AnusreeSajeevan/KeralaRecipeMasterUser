package com.keralarecipemaster.admin.network.model

import com.keralarecipemaster.admin.domain.model.Recipe
import com.keralarecipemaster.admin.domain.model.util.DomainMapper
import com.keralarecipemaster.admin.utils.Diet
import com.keralarecipemaster.admin.utils.Meal

class RecipeDtoMapper :
    DomainMapper<RecipeDto, Recipe> {
    override fun toDomainModel(entity: RecipeDto): Recipe {
        return Recipe(
            recipeName = entity.recipeName,
            description = entity.description,
            ingredients = entity.ingredients,
            image = entity.image,
            restaurant = entity.restaurant,
            preparationMethod = entity.preparationMethod,
            mealType = Meal.valueOf(entity.mealType),
            diet = Diet.valueOf(entity.diet)
        )
    }

    override fun fromDomainModel(domainModel: Recipe): RecipeDto {
        return RecipeDto(
            recipeName = domainModel.recipeName,
            description = domainModel.description,
            ingredients = domainModel.ingredients,
            image = domainModel.image,
            restaurant = domainModel.restaurant,
            preparationMethod = domainModel.preparationMethod,
            mealType = domainModel.mealType.name,
            diet = domainModel.diet.name
        )
    }

    fun toEntityList(list: List<Recipe>): List<RecipeDto> {
        return list.map { fromDomainModel(it) }
    }

    fun toRecipeList(list: List<RecipeDto>): List<Recipe> {
        return list.map { toDomainModel(it) }
    }
}
