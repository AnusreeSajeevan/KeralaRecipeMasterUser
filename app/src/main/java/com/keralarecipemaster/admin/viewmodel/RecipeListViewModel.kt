package com.keralarecipemaster.admin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keralarecipemaster.admin.model.Recipe
import com.keralarecipemaster.admin.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(val recipeRepository: RecipeRepository) :
    ViewModel() {

   /* private var _defaultRecipes = MutableLiveData<List<Recipe>>()

    val defaultRecipes: LiveData<List<Recipe>>
        get() = _defaultRecipes

    fun getRecipes() {
        viewModelScope.launch {
            recipeRepository.getDefaultRecipes()
        }
    }*/
}
