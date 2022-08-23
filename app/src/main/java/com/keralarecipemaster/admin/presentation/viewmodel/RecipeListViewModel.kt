package com.keralarecipemaster.admin.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.keralarecipemaster.admin.domain.model.RecipeEntity
import com.keralarecipemaster.admin.repository.RecipeRepository
import com.keralarecipemaster.admin.utils.DietFilter
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
    private val _dietTypeFilter = MutableStateFlow<String>(DietFilter.ALL.name)

    val defaultRecipes: StateFlow<List<RecipeEntity>>
        get() = _defaultRecipes

    val userAddedRecipes: StateFlow<List<RecipeEntity>>
        get() = _userAddedRecipes

    val dietTypeFilter: StateFlow<String>
        get() = _dietTypeFilter

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
            recipeRepository.getDefaultRecipes().catch { }.collect { defaultRecipes ->
                if (_dietTypeFilter.value != DietFilter.ALL.name) {
                    _defaultRecipes.value = defaultRecipes.filter {
                        it.diet.name == _dietTypeFilter.value
                    }
                } else {
                    _defaultRecipes.value = defaultRecipes
                }
            }
        }
    }

    fun getUserAddedeRecipes() {
        viewModelScope.launch {
            recipeRepository.getUserAddedRecipes().catch { }.collect { userAddedRecipes ->
                if (_dietTypeFilter.value != DietFilter.ALL.name) {
                    _userAddedRecipes.value = userAddedRecipes.filter {
                        it.diet.name == _dietTypeFilter.value
                    }
                } else {
                    _userAddedRecipes.value = userAddedRecipes
                }
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

    fun onDietCategoryChange(filter: String, userType: UserType) {
        _dietTypeFilter.value = filter
        getDefaultRecipes()
        getUserAddedeRecipes()
    }
}
