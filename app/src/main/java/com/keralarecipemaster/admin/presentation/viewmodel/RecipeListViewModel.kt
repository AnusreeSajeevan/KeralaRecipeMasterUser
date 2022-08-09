package com.keralarecipemaster.admin.presentation.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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

    var defaultRecipes: LiveData<List<Recipe>> = recipeRepository.getAllRecipes

    /* private fun getRecipes() {
        viewModelScope.launch {
            val result = recipeRepository.getDefaultRecipes()

        }
    }*/
}
