package com.keralarecipemaster.admin.network.model

import com.keralarecipemaster.admin.domain.model.Recipe
import com.keralarecipemaster.admin.domain.model.Restaurant
import com.keralarecipemaster.admin.domain.model.util.DomainMapper
import com.keralarecipemaster.admin.utils.Diet
import com.keralarecipemaster.admin.utils.Meal
import com.keralarecipemaster.admin.utils.UserType

class RecipeDtoMapper :
    DomainMapper<RecipeDto, Recipe> {
    override fun toDomainModel(entity: RecipeDto): Recipe {
        return Recipe(
            id = entity.id,
            recipeName = entity.recipeName,
            description = entity.description,
            ingredients = entity.ingredients,
            image = entity.image,
            restaurantName = entity.restaurant.name,
            restaurantLatitude = entity.restaurant.latitude,
            restaurantLongitude = entity.restaurant.longitude,
            restaurantState = entity.restaurant.state,
            preparationMethod = entity.preparationMethod,
            mealType = Meal.valueOf(entity.mealType),
            diet = Diet.valueOf(entity.diet),
            addedBy = UserType.ADMIN.name
        )
    }

    override fun fromDomainModel(domainModel: Recipe): RecipeDto {
        return RecipeDto(
            id = domainModel.id,
            recipeName = domainModel.recipeName,
            description = domainModel.description,
            ingredients = domainModel.ingredients,
            image = domainModel.image,
            restaurant = Restaurant(
                name = domainModel.restaurantName,
                latitude = domainModel.restaurantLatitude,
                longitude = domainModel.restaurantLongitude,
                state = domainModel.restaurantState
            ),
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
