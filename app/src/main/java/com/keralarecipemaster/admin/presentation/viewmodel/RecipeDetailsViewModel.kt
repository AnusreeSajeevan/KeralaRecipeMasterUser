package com.keralarecipemaster.admin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keralarecipemaster.admin.domain.model.RecipeEntity
import com.keralarecipemaster.admin.repository.RecipeRepository
import com.keralarecipemaster.admin.utils.RecipeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(val repository: RecipeRepository) :
    ViewModel() {

    companion object {
        const val EMPTY_STRING = ""
    }

    val recipe: StateFlow<RecipeEntity>
        get() = _recipe

    private var _recipe: MutableStateFlow<RecipeEntity> =
        MutableStateFlow(RecipeUtil.provideRecipe())

    fun getRecipeDetails(recipeId: Int) {
        viewModelScope.launch {
            repository.getRecipeDetails(recipeId).catch { }.collect {
                _recipe.value = it
            }
        }
    }
}
