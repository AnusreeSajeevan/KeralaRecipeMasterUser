package com.keralarecipemaster.admin.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.keralarecipemaster.admin.domain.model.RecipeEntity
import com.keralarecipemaster.admin.repository.RecipeRepository
import com.keralarecipemaster.admin.utils.DietFilter
import com.keralarecipemaster.admin.utils.MealFilter
import com.keralarecipemaster.admin.utils.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    val recipeRepository: RecipeRepository,
    application: Application
) : AndroidViewModel(application) {
    private var _defaultRecipes = MutableStateFlow<List<RecipeEntity>>(emptyList())
    private var _userAddedRecipes = MutableStateFlow<List<RecipeEntity>>(emptyList())
    private val _dietTypeFilter = MutableStateFlow(DietFilter.ALL.name)
    private val _mealTypeFilter = MutableStateFlow(MealFilter.ALL.name)

    val defaultRecipes: StateFlow<List<RecipeEntity>>
        get() = _defaultRecipes

    val userAddedRecipes: StateFlow<List<RecipeEntity>>
        get() = _userAddedRecipes

    val dietTypeFilter: StateFlow<String>
        get() = _dietTypeFilter

    val mealTypeFilter: StateFlow<String>
        get() = _mealTypeFilter

    init {
//        fetchAllRecipes()
        getDefaultRecipes()
        getUserAddedeRecipes()
    }

    private fun fetchAllRecipes() {
        viewModelScope.launch {
            recipeRepository.fetchAllRecipes()
        }
    }

    fun getDefaultRecipes() {
        viewModelScope.launch {
            recipeRepository.getDefaultRecipes().catch { }.collect { recipes ->
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
                _defaultRecipes.value = list
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
            if (addedBy == UserType.ADMIN) getDefaultRecipes() else getUserAddedeRecipes()
        } else {
            viewModelScope.launch {
                recipeRepository.searchResults(query, addedBy).catch { }.collect {
                    if (addedBy == UserType.ADMIN) _defaultRecipes.value = it
                    else _userAddedRecipes.value = it
                }
            }
        }
    }

    fun onDietFilterChange(diet: String) {
        _dietTypeFilter.value = diet
        getDefaultRecipes()
        getUserAddedeRecipes()
    }

    fun onMealFilterChange(meal: String) {
        _mealTypeFilter.value = meal
        getDefaultRecipes()
        getUserAddedeRecipes()
    }
}
