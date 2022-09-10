package com.keralarecipemaster.user.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.keralarecipemaster.user.domain.model.RecipeEntity
import com.keralarecipemaster.user.repository.RecipeRepository
import com.keralarecipemaster.user.utils.DietFilter
import com.keralarecipemaster.user.utils.MealFilter
import com.keralarecipemaster.user.utils.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    val recipeRepository: RecipeRepository,
    application: Application
) : AndroidViewModel(application) {
    private var _famousRecipes = MutableStateFlow<List<RecipeEntity>>(emptyList())
    private var _userAddedRecipes = MutableStateFlow<List<RecipeEntity>>(emptyList())
    private val _dietTypeFilter = MutableStateFlow(DietFilter.ALL.name)
    private val _mealTypeFilter = MutableStateFlow(MealFilter.ALL.name)

    val famousRecipes: StateFlow<List<RecipeEntity>>
        get() = _famousRecipes

    val userAddedRecipes: StateFlow<List<RecipeEntity>>
        get() = _userAddedRecipes

    val dietTypeFilter: StateFlow<String>
        get() = _dietTypeFilter

    val mealTypeFilter: StateFlow<String>
        get() = _mealTypeFilter

    init {
//        fetchAllRecipes()
        getRestaurantAddedRecipes()
        getUserAddedeRecipes()
    }

    private fun fetchAllRecipes() {
        viewModelScope.launch {
            recipeRepository.fetchAllRecipes()
        }
    }

    fun getRestaurantAddedRecipes() {
        viewModelScope.launch {
            recipeRepository.getFamousRecipes().catch { }.collect { recipes ->
                var list = recipes
                if (_dietTypeFilter.value != DietFilter.ALL.name) {
                    list = recipes.filter {
                        it.diet.name == _dietTypeFilter.value
                    }
                }

                if (_mealTypeFilter.value != MealFilter.ALL.name) {
                    list = list.filter {
                        it.mealType.name == _mealTypeFilter.value
                    }
                }
                _famousRecipes.value = list
            }
        }
    }

    fun getUserAddedeRecipes() {
        viewModelScope.launch {
            recipeRepository.getUserAddedRecipes().catch { }.collect { recipes ->
                var list = recipes
                if (_dietTypeFilter.value != DietFilter.ALL.name) {
                    list = recipes.filter {
                        it.diet.name == _dietTypeFilter.value
                    }
                }

                if (_mealTypeFilter.value != MealFilter.ALL.name) {
                    list = list.filter {
                        it.mealType.name == _mealTypeFilter.value
                    }
                }
                _userAddedRecipes.value = list
            }
        }
    }

    fun deleteRecipe(recipe: RecipeEntity) {
        viewModelScope.launch {
            recipeRepository.deleteRecipe(recipe)
        }
    }

    fun onQueryChanged(query: String, addedBy: UserType) {
        if (query.isEmpty()) {
            if (addedBy == UserType.RESTAURANT) getRestaurantAddedRecipes() else getUserAddedeRecipes()
        } else {
            viewModelScope.launch {
                recipeRepository.searchResults(query, addedBy).catch { }.collect {
                    if (addedBy == UserType.RESTAURANT) _famousRecipes.value = it
                    else _userAddedRecipes.value = it
                }
            }
        }
    }

    fun onDietFilterChange(diet: String) {
        _dietTypeFilter.value = diet
        getRestaurantAddedRecipes()
        getUserAddedeRecipes()
    }

    fun onMealFilterChange(meal: String) {
        _mealTypeFilter.value = meal
        getRestaurantAddedRecipes()
        getUserAddedeRecipes()
    }
}
