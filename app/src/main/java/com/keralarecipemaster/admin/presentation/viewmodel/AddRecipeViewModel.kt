package com.keralarecipemaster.admin.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keralarecipemaster.admin.domain.model.Ingredient
import com.keralarecipemaster.admin.domain.model.RecipeEntity
import com.keralarecipemaster.admin.presentation.ui.recipe.OnRatingBarCheck
import com.keralarecipemaster.admin.repository.RecipeRepository
import com.keralarecipemaster.admin.utils.Diet
import com.keralarecipemaster.admin.utils.Meal
import com.keralarecipemaster.admin.utils.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRecipeViewModel @Inject constructor(val repository: RecipeRepository) :
    ViewModel(),
    OnRatingBarCheck {

    companion object {
        const val EMPTY_STRING = ""
    }

    val recipeName: StateFlow<String>
        get() = _recipeName

    val description: StateFlow<String>
        get() = _description

    val dietType: StateFlow<String>
        get() = _dietType

    val mealType: StateFlow<String>
        get() = _mealType

    val ingredients: StateFlow<List<Ingredient>>
        get() = _ingredients

    val preparationMethod: StateFlow<String>
        get() = _preparationMethod

    val restaurantName: StateFlow<String>
        get() = _restaurantName

    val latitude: StateFlow<String>
        get() = _latitude

    val longitude: StateFlow<String>
        get() = _longitude

    val state: StateFlow<String>
        get() = _state

    val hasRestaurantDetails: MutableState<Boolean>
        get() = _hasRestaurantDetails

    val rating: StateFlow<Int>
        get() = _rating

//    val numberOfIngredients: StateFlow<Int>
//        get() = _numberOfIngredients

    private var _recipeName = MutableStateFlow(EMPTY_STRING)
    private var _dietType = MutableStateFlow(Diet.VEG.name)
    private var _mealType = MutableStateFlow(Meal.BREAKFAST.name)
    private var _ingredients = MutableStateFlow(listOf<Ingredient>())
    private var _description = MutableStateFlow(EMPTY_STRING)
    private var _preparationMethod = MutableStateFlow(EMPTY_STRING)
    private var _restaurantName = MutableStateFlow(EMPTY_STRING)
    private var _latitude = MutableStateFlow(EMPTY_STRING)
    private var _longitude = MutableStateFlow(EMPTY_STRING)
    private var _state = MutableStateFlow(EMPTY_STRING)
    private var _hasRestaurantDetails = mutableStateOf(false)
    private var _rating = MutableStateFlow(0)

    fun onRecipeNameChange(recipeName: String) {
        this._recipeName.value = recipeName
    }

    /*   fun onIngredientsChange(ingredients: String) {
           this._ingredients.value = ingredients
       }*/

    fun onDescriptionChange(description: String) {
        this._description.value = description
    }

    fun onPreparationMethodChange(preparationMethod: String) {
        this._preparationMethod.value = preparationMethod
    }

    fun onRestaurantNameChange(restaurantName: String) {
        this._restaurantName.value = restaurantName
    }

    fun onLatitudeChange(latitude: String) {
        this._latitude.value = latitude
    }

    fun onLongitudeChange(longitude: String) {
        this._longitude.value = longitude
    }

    fun onStateChange(state: String) {
        this._state.value = state
    }

    fun onMealCategoryChange(mealType: String) {
        this._mealType.value = mealType
    }

    fun onDietCategoryChange(dietType: String) {
        this._dietType.value = dietType
    }

    fun addRecipe() {
        if (validateRecipeDetails()) {
            if (_hasRestaurantDetails.value) {
                if (validateRestaurantDetails()) addRecipeToDb()
            } else {
                addRecipeToDb()
            }
        }
    }

    fun updateRecipe(recipeId: Int?) {
        recipeId?.let {
            if (validateRecipeDetails()) {
                if (_hasRestaurantDetails.value) {
                    if (validateRestaurantDetails()) updateRecipeInDb(recipeId = recipeId)
                } else {
                    updateRecipeInDb(recipeId = recipeId)
                }
            }
        }
    }

    private fun addRecipeToDb() {
        viewModelScope.launch {
            repository.addRecipe(
                RecipeEntity(
                    id = repository.count() + 1,
                    recipeName = _recipeName.value,
                    description = _description.value,
                    preparationMethod = _preparationMethod.value,
                    ingredients = _ingredients.value,
                    diet = Diet.valueOf(_dietType.value),
                    mealType = Meal.valueOf(mealType.value),
                    restaurantState = state.value,
                    restaurantLatitude = latitude.value,
                    restaurantLongitude = longitude.value,
                    restaurantName = restaurantName.value,
                    addedBy = UserType.ADMIN.name,
                    rating = _rating.value
                )
            )
        }
    }

    private fun updateRecipeInDb(recipeId: Int) {
        viewModelScope.launch {
            repository.updateRecipe(
                recipeName = recipeName.value,
                description = _description.value,
                recipeId = recipeId,
                diet = Diet.valueOf(_dietType.value),
                meal = Meal.valueOf(_mealType.value),
                ingredients = _ingredients.value,
                preparationMethod = _preparationMethod.value
            )
        }
    }

    fun validateRestaurantDetails(): Boolean {
        return !(restaurantName.value == EMPTY_STRING || latitude.value == EMPTY_STRING || longitude.value == EMPTY_STRING || state.value == EMPTY_STRING)
    }

    fun validateRecipeDetails(): Boolean {
        return !(recipeName.value == EMPTY_STRING || ingredients.value.isEmpty() || preparationMethod.value == EMPTY_STRING)
    }

    fun onRestaurantCheckChange(restaurantChecked: Boolean) {
        _hasRestaurantDetails.value = restaurantChecked
    }

    override fun onChangeRating(rating: Int) {
        _rating.value = rating
    }

    fun getRecipeDetails(recipeId: Int) {
        viewModelScope.launch {
            repository.getRecipeDetails(recipeId).catch { }.collect {
                _recipeName.value = it.recipeName
                _state.value = it.restaurantState
                _restaurantName.value = it.restaurantName
                _description.value = it.description
                _ingredients.value = it.ingredients
                _preparationMethod.value = it.preparationMethod
                _dietType.value = it.diet.name
                _mealType.value = it.mealType.name
                _rating.value = it.rating
            }
        }
    }

    private fun List<Ingredient>.getArrayListFromList(): ArrayList<Ingredient> {
        val arrayList = arrayListOf<Ingredient>()
        this.forEach {
            arrayList.add(Ingredient(name = it.name, quantity = it.quantity))
        }
        return arrayList
    }

    fun onAddRecipeClick(ingredientName: String, ingredientQuantity: String) {
        val list: ArrayList<Ingredient> = arrayListOf()
        _ingredients.value.forEach {
            list.add(it)
        }

        list.add(Ingredient(name = ingredientName, quantity = ingredientQuantity))
        _ingredients.value = list
    }

    fun clearIngredients() {
        _ingredients.value = emptyList()
    }

    /* fun onAddNewIngredient() {
         _numberOfIngredients.value = _numberOfIngredients.value + 1
     }*/
}
