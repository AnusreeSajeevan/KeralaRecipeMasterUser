package com.keralarecipemaster.admin.network.model

import com.keralarecipemaster.admin.domain.model.RecipeEntity
import com.keralarecipemaster.admin.domain.model.Restaurant
import com.keralarecipemaster.admin.domain.model.util.DomainMapper
import com.keralarecipemaster.admin.utils.Diet
import com.keralarecipemaster.admin.utils.Meal
import com.keralarecipemaster.admin.utils.UserType
import javax.inject.Inject

class RecipeDtoMapper @Inject constructor() :
    DomainMapper<RecipeResponse, RecipeEntity> {
    override fun toDomainModel(entity: RecipeResponse): RecipeEntity {
        return RecipeEntity(
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

    override fun fromDomainModel(domainModel: RecipeEntity): RecipeResponse {
        return RecipeResponse(
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

    fun toEntityList(list: List<RecipeEntity>): List<RecipeResponse> {
        return list.map { fromDomainModel(it) }
    }

    fun toRecipeEntityList(list: List<RecipeResponse>): List<RecipeEntity> {
        return list.map { toDomainModel(it) }
    }
}
