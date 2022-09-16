package com.keralarecipemaster.user.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keralarecipemaster.user.domain.model.RecipeRequestEntity
import com.keralarecipemaster.user.presentation.ui.recipe.OnRatingBarCheck
import com.keralarecipemaster.user.repository.RecipeRequestRepository
import com.keralarecipemaster.user.utils.RecipeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeRequestDetailsViewModel @Inject constructor(
    val repository: RecipeRequestRepository,
    private val recipeRequestRepository: RecipeRequestRepository
) :
    ViewModel(), OnRatingBarCheck {

    companion object {
        const val EMPTY_STRING = ""
    }

    val recipeRequest: StateFlow<RecipeRequestEntity>
        get() = _recipeRequest
    val rating: StateFlow<Int>
        get() = _rating

    private var _recipeRequest: MutableStateFlow<RecipeRequestEntity> =
        MutableStateFlow(RecipeUtil.provideRecipeRequest())

    private var _rating: MutableStateFlow<Int> =
        MutableStateFlow(0)

    fun getRecipeRequestDetails(requestId: Int) {
        viewModelScope.launch {
            repository.getRecipeRequestDetails(requestId = requestId).catch { }.collect {
                it?.let {
                    _recipeRequest.value = it
                    _rating.value = it.rating
                }
            }
        }
    }

    override fun onChangeRating(rating: Int) {
    }
}
