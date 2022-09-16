package com.keralarecipemaster.user.network.model.reciperequest

import com.keralarecipemaster.user.domain.model.RecipeRequestEntity
import com.keralarecipemaster.user.domain.model.Restaurant
import com.keralarecipemaster.user.domain.model.util.DomainMapper
import com.keralarecipemaster.user.utils.Constants
import com.keralarecipemaster.user.utils.Diet
import com.keralarecipemaster.user.utils.Meal
import javax.inject.Inject

class RecipeRequestDtoMapper @Inject constructor() :
    DomainMapper<RecipeRequestResponse, RecipeRequestEntity> {
    override fun toDomainModel(entity: RecipeRequestResponse): RecipeRequestEntity {
        return RecipeRequestEntity(
            requestId = entity.requestId,
            recipeName = entity.recipeName,
            description = entity.description,
            ingredients = entity.ingredients,
            image = entity.image,
            restaurantName = entity.restaurant.name,
            restaurantLatitude = entity.restaurant.latitude,
            restaurantLongitude = entity.restaurant.longitude,
            restaurantAddress = entity.restaurant.address,
            preparationMethod = entity.preparationMethod,
            mealType = Meal.valueOf(entity.mealType.toUpperCase()),
            diet = Diet.valueOf(entity.diet.toUpperCase()),
            rating = entity.rating
        )
    }

    override fun fromDomainModel(domainModel: RecipeRequestEntity): RecipeRequestResponse {
        return RecipeRequestResponse(
            requestId = domainModel.requestId,
            recipeName = domainModel.recipeName,
            description = domainModel.description,
            ingredients = domainModel.ingredients,
            image = domainModel.image ?: Constants.EMPTY_STRING,
            restaurant = Restaurant(
                name = domainModel.restaurantName,
                latitude = domainModel.restaurantLatitude,
                longitude = domainModel.restaurantLongitude,
                address = domainModel.restaurantAddress
            ),
            preparationMethod = domainModel.preparationMethod,
            mealType = domainModel.mealType.name,
            diet = domainModel.diet.name,
            rating = domainModel.rating
        )
    }

    fun toEntityList(list: List<RecipeRequestEntity>): List<RecipeRequestResponse> {
        return list.map { fromDomainModel(it) }
    }

    fun toRecipeRequestEntityList(list: List<RecipeRequestResponse>): List<RecipeRequestEntity> {
        return list.map { toDomainModel(it) }
    }
}