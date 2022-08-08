package com.keralarecipemaster.admin.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.keralarecipemaster.admin.utils.Diet
import com.keralarecipemaster.admin.utils.Meal
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddRecipeViewModel @Inject constructor() : ViewModel() {

    companion object {
        const val EMPTY_STRING = ""
    }

    val recipeName: MutableState<String>
        get() = _recipeName

    val dietType: MutableState<Diet>
        get() = _dietType

    val mealType: MutableState<Meal>
        get() = _mealType

    val ingredients: MutableState<String>
        get() = _ingredients

    val preparationMethod: MutableState<String>
    get() = _preparationMethod

    private var _recipeName = mutableStateOf("")
    private var _dietType = mutableStateOf(Diet.INVALID)
    private var _mealType = mutableStateOf(Meal.INVALID)
    private var _ingredients = mutableStateOf("")
    private var _preparationMethod = mutableStateOf("")

    fun onRecipeNameChange(recipeName: String) {
        this._recipeName.value = recipeName
    }

    fun onIngredientsChange(ingredients: String) {
        this._ingredients.value = ingredients
    }

    fun onPreparationMethodChange(preparationMethod: String) {
        this._preparationMethod.value = preparationMethod
    }

    fun onMealTypeChange(mealType: Meal) {
        this._mealType.value = mealType
    }

    fun onDietTypeChange(dietType: Diet) {
        this._dietType.value = dietType
    }
}
