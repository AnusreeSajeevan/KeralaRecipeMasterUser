package com.keralarecipemaster.admin.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.keralarecipemaster.admin.domain.model.Recipe
import com.keralarecipemaster.admin.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    val recipeRepository: RecipeRepository,
    application: Application
) : AndroidViewModel(application) {
    var defaultRecipes: LiveData<List<Recipe>> = recipeRepository.getDefaultRecipes
    var userAddedRecipes: LiveData<List<Recipe>> = recipeRepository.getUserAddedRecipes
}
