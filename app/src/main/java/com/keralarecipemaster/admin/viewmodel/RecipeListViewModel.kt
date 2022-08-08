package com.keralarecipemaster.admin.viewmodel

import androidx.lifecycle.ViewModel
import com.keralarecipemaster.admin.network.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
