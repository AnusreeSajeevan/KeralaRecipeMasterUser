package com.keralarecipemaster.admin.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.keralarecipemaster.admin.domain.model.Recipe
import com.keralarecipemaster.admin.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    val recipeRepository: RecipeRepository,
    application: Application
) : AndroidViewModel(application) {

    private var _defaultRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val defaultRecipes: StateFlow<List<Recipe>>
        get() = _defaultRecipes

    var userAddedRecipes: LiveData<List<Recipe>> = recipeRepository.getUserAddedRecipes

    var query = MutableStateFlow("")

    init {
        fetchAllRecipes()
        getDefaultRecipes()
        /*viewModelScope.launch {
            query
                .debounce(timeoutMillis = 1500)
                .distinctUntilChanged()
                .collect {
                    _defaultRecipes = recipeRepository.searchResults(it).asLiveData()
                }
        }*/
    }

    private fun fetchAllRecipes() {
        recipeRepository.fetchAllRecipes()
    }

    fun getDefaultRecipes() {
        viewModelScope.launch {
            recipeRepository.getDefaultRecipes().catch { }.collect {
                _defaultRecipes.value = it
            }
        }
    }

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            recipeRepository.deleteRecipe(recipe)
        }
    }

    /* fun onQueryChanged(query: String) {
         _defaultRecipes =*//*recipeRepository.searchResults(query).asLiveData()*//*
    }*/
}
