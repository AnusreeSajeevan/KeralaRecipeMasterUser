package com.keralarecipemaster.admin.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.keralarecipemaster.admin.domain.model.Recipe
import com.keralarecipemaster.admin.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    val recipeRepository: RecipeRepository,
    application: Application
) : AndroidViewModel(application) {
    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            recipeRepository.deleteRecipe(recipe)
        }
    }

    fun onQueryChanged() {
    }

    var defaultRecipes: LiveData<List<Recipe>> = recipeRepository.getDefaultRecipes
    var userAddedRecipes: LiveData<List<Recipe>> = recipeRepository.getUserAddedRecipes
}
