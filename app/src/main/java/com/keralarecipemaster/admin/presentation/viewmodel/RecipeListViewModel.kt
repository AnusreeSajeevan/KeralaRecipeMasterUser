package com.keralarecipemaster.admin.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keralarecipemaster.admin.domain.model.Recipe
import com.keralarecipemaster.admin.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    val recipeRepository: RecipeRepository
) : ViewModel() {

    val defaultRecipes: MutableState<List<Recipe>> = mutableStateOf(ArrayList())

    init {
        getRecipes()
    }

    private fun getRecipes() {
        viewModelScope.launch {
            val result = recipeRepository.getDefaultRecipes()
            defaultRecipes.value = result
        }
    }
}
