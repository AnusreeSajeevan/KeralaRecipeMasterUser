package com.keralarecipemaster.user.network.model.recipe

import com.keralarecipemaster.user.domain.model.RecipeEntity
import com.keralarecipemaster.user.domain.model.Restaurant
import com.keralarecipemaster.user.domain.model.util.DomainMapper
import com.keralarecipemaster.user.utils.Constants
import com.keralarecipemaster.user.utils.Diet
import com.keralarecipemaster.user.utils.Meal
import com.keralarecipemaster.user.utils.UserType
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
            restaurantName = entity.restaurant?.name ?: Constants.EMPTY_STRING,
            restaurantLatitude = entity.restaurant?.latitude ?: Constants.EMPTY_STRING,
            restaurantLongitude = entity.restaurant?.longitude ?: Constants.EMPTY_STRING,
            restaurantAddress = entity.restaurant?.address ?: Constants.EMPTY_STRING,
            preparationMethod = entity.preparationMethod,
            mealType = Meal.valueOf(entity.mealType.toUpperCase()),
            diet = Diet.valueOf(entity.diet.toUpperCase()),
            addedBy = UserType.valueOf(entity.addedBy.toUpperCase()),
            rating = entity.rating
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
                address = domainModel.restaurantAddress
            ),
            preparationMethod = domainModel.preparationMethod,
            mealType = domainModel.mealType.name,
            diet = domainModel.diet.name,
            rating = domainModel.rating,
            addedBy = domainModel.addedBy.name
        )
    }

    fun toEntityList(list: List<RecipeEntity>): List<RecipeResponse> {
        return list.map { fromDomainModel(it) }
    }

    fun toRecipeEntityList(list: List<RecipeResponse>): List<RecipeEntity> {
        return list.map { toDomainModel(it) }
    }
}
