package com.keralarecipemaster.admin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keralarecipemaster.admin.domain.model.RecipeEntity
import com.keralarecipemaster.admin.presentation.ui.recipe.OnRatingBarCheck
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
        MutableStateFlow(0)

    fun getRecipeDetails(recipeId: Int) {
        viewModelScope.launch {
            repository.getRecipeDetails(recipeId).catch { }.collect {
                _recipe.value = it
                _rating.value = it.rating
            }
        }
    }

    override fun onChangeRating(rating: Int) {

    }
}
