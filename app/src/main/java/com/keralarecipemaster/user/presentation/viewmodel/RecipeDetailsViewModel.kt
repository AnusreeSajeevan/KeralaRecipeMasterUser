package com.keralarecipemaster.user.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keralarecipemaster.user.domain.model.RecipeEntity
import com.keralarecipemaster.user.presentation.ui.recipe.OnRatingBarCheck
import com.keralarecipemaster.user.repository.RecipeRepository
import com.keralarecipemaster.user.utils.Constants
import com.keralarecipemaster.user.utils.RecipeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(val repository: RecipeRepository) :
    ViewModel(), OnRatingBarCheck {

    companion object {
        const val EMPTY_STRING = ""
    }

    val recipe: StateFlow<RecipeEntity>
        get() = _recipe
    val rating: StateFlow<Int>
        get() = _rating

    private var _recipe: MutableStateFlow<RecipeEntity> =
        MutableStateFlow(RecipeUtil.provideRecipe())

    private var _rating: MutableStateFlow<Int> =
        MutableStateFlow(Constants.INVALID_RECIPE_ID)

    fun getRecipeDetails(recipeId: Int) {
        viewModelScope.launch {
            repository.getRecipeDetails(recipeId).catch { }.collect { recipeEntity ->
                recipeEntity?.let {
                    _recipe.value = it
                    _rating.value = it.rating
                }

            }
        }
    }

    override fun onChangeRating(rating: Int) {

    }
}
