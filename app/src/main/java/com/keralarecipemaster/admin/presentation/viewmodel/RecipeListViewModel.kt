package com.keralarecipemaster.admin.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.keralarecipemaster.admin.domain.model.RecipeEntity
import com.keralarecipemaster.admin.repository.RecipeRepository
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
    val defaultRecipes: StateFlow<List<RecipeEntity>>
        get() = _defaultRecipes

    var userAddedRecipes: LiveData<List<RecipeEntity>> = recipeRepository.getUserAddedRecipes

    var query = MutableStateFlow("")

    init {
//        fetchAllRecipes()
        getDefaultRecipes()
    }

    private fun fetchAllRecipes() {
        viewModelScope.launch {
            recipeRepository.fetchAllRecipes()
        }
    }

    fun getDefaultRecipes() {
        viewModelScope.launch {
            recipeRepository.getDefaultRecipes().catch { }.collect {
                _defaultRecipes.value = it
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
            getDefaultRecipes()
        } else {
            viewModelScope.launch {
                recipeRepository.searchResults(query, addedBy).catch { }.collect {
                    _defaultRecipes.value = it
                }
            }
        }
    }
}
