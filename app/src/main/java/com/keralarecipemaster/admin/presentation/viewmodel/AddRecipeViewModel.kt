package com.keralarecipemaster.admin.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keralarecipemaster.admin.domain.model.RecipeEntity
import com.keralarecipemaster.admin.repository.RecipeRepository
import com.keralarecipemaster.admin.utils.Diet
import com.keralarecipemaster.admin.utils.Meal
import com.keralarecipemaster.admin.utils.UserType
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

    val dietType: MutableState<String>
        get() = _dietType

    val mealType: MutableState<String>
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

    private var _recipeName = mutableStateOf(EMPTY_STRING)
    private var _dietType = mutableStateOf(Diet.VEG.name)
    private var _mealType = mutableStateOf(Meal.BREAKFAST.name)
    private var _ingredients = mutableStateOf(EMPTY_STRING)
    private var _description = mutableStateOf(EMPTY_STRING)
    private var _preparationMethod = mutableStateOf(EMPTY_STRING)
    private var _restaurantName = mutableStateOf(EMPTY_STRING)
    private var _latitude = mutableStateOf(EMPTY_STRING)
    private var _longitude = mutableStateOf(EMPTY_STRING)
    private var _state = mutableStateOf(EMPTY_STRING)

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

    fun onMealCategoryChange(mealType: String) {
        this._mealType.value = mealType
    }

    fun onDietCategoryChange(dietType: String) {
        this._dietType.value = dietType
    }

    fun addRecipe() {
        if (validateFields()) {
            viewModelScope.launch {
                repository.addRecipe(
                    RecipeEntity(
                        id = repository.count() + 1,
                        recipeName = recipeName.value,
                        description = description.value,
                        preparationMethod = preparationMethod.value,
                        ingredients = listOf(ingredients.value),
                        diet = Diet.valueOf(dietType.value),
                        mealType = Meal.valueOf(mealType.value),
                        restaurantState = state.value,
                        restaurantLatitude = latitude.value,
                        restaurantLongitude = longitude.value,
                        restaurantName = restaurantName.value,
                        addedBy = UserType.ADMIN.name
                    )
                )
            }
        }
    }

    fun validateFields(): Boolean {
       return validateRecipeDetails() && validateRestaurantDetails()
    }

    private fun validateRestaurantDetails(): Boolean {
        return true
    }

    private fun validateRecipeDetails(): Boolean {
        return !(recipeName.value == EMPTY_STRING || ingredients.value == EMPTY_STRING || preparationMethod.value == EMPTY_STRING)
    }
}
