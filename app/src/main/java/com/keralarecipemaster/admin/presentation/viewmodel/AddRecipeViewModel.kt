package com.keralarecipemaster.admin.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keralarecipemaster.admin.domain.model.Recipe
import com.keralarecipemaster.admin.repository.RecipeRepository
import com.keralarecipemaster.admin.utils.Diet
import com.keralarecipemaster.admin.utils.Meal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRecipeViewModel @Inject constructor(val repository: RecipeRepository) : ViewModel() {

    companion object {
        const val EMPTY_STRING = ""
    }

    val recipeName: MutableState<String>
        get() = _recipeName

    val description: MutableState<String>
        get() = _description

    val dietType: MutableState<Diet>
        get() = _dietType

    val mealType: MutableState<Meal>
        get() = _mealType

    val ingredients: MutableState<String>
        get() = _ingredients

    val preparationMethod: MutableState<String>
        get() = _preparationMethod

    val restaurantName: MutableState<String>
        get() = _restaurantName

    val latitude: MutableState<String>
        get() = _latitude

    val longitude: MutableState<String>
        get() = _longitude

    val state: MutableState<String>
        get() = _state

    private var _recipeName = mutableStateOf("")
    private var _dietType = mutableStateOf(Diet.INVALID)
    private var _mealType = mutableStateOf(Meal.INVALID)
    private var _ingredients = mutableStateOf("")
    private var _description = mutableStateOf("")
    private var _preparationMethod = mutableStateOf("")
    private var _restaurantName = mutableStateOf("")
    private var _latitude = mutableStateOf("")
    private var _longitude = mutableStateOf("")
    private var _state = mutableStateOf("")

    fun onRecipeNameChange(recipeName: String) {
        this._recipeName.value = recipeName
    }

    fun onIngredientsChange(ingredients: String) {
        this._ingredients.value = ingredients
    }

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

    fun onMealTypeChange(mealType: Meal) {
        this._mealType.value = mealType
    }

    fun onDietTypeChange(dietType: Diet) {
        this._dietType.value = dietType
    }

    fun addRecipe() {
        viewModelScope.launch {
            repository.addRecipe(
                Recipe(
                    id = repository.count() + 1,
                    recipeName = recipeName.value,
                    description = description.value,
                    preparationMethod = preparationMethod.value,
                    ingredients = listOf(ingredients.value),
                    diet = Diet.VEG,
                    mealType = Meal.BREAKFAST,
                    restaurantState = state.value,
                    restaurantLatitude = latitude.value,
                    restaurantLongitude = longitude.value,
                    restaurantName = restaurantName.value
                )
            )
        }
    }
}
