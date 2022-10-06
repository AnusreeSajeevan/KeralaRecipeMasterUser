package com.keralarecipemaster.user.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.keralarecipemaster.user.domain.model.RecipeRequestEntity
import com.keralarecipemaster.user.prefsstore.PrefsStore
import com.keralarecipemaster.user.repository.RecipeRequestRepository
import com.keralarecipemaster.user.utils.Constants
import com.keralarecipemaster.user.utils.DietFilter
import com.keralarecipemaster.user.utils.MealFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeRequestViewModel @Inject constructor(
    private val recipeRequestRepository: RecipeRequestRepository,
    prefsStore: PrefsStore,
    application: Application
) : AndroidViewModel(application) {
    private var _recipeRequests = MutableStateFlow<List<RecipeRequestEntity>>(emptyList())
    private val _dietTypeFilter = MutableStateFlow(DietFilter.ALL.name)
    private val _mealTypeFilter = MutableStateFlow(MealFilter.ALL.name)

    val recipeRequests: StateFlow<List<RecipeRequestEntity>>
        get() = _recipeRequests

    val dietTypeFilter: StateFlow<String>
        get() = _dietTypeFilter

    val mealTypeFilter: StateFlow<String>
        get() = _mealTypeFilter

    val userId: StateFlow<Int>
        get() = _userId

    private val _userId =
        MutableStateFlow(Constants.INVALID_USER_ID)

    val shouldFetchMyRecipes: StateFlow<Boolean>
        get() = _shouldFetchMyRecipes

    private val _shouldFetchMyRecipes =
        MutableStateFlow(false)

    val errorMessage: StateFlow<String>
        get() = _errorMessage

    private val _errorMessage =
        MutableStateFlow(Constants.EMPTY_STRING)

    init {
        viewModelScope.launch {
            prefsStore.getUserId().catch { }.collect {
                _userId.value = it
                _shouldFetchMyRecipes.value = true
            }
        }
//        fetchRecipeRequests()
        getApprovedRecipeRequests()
    }

    fun fetchRecipeRequests() {
        viewModelScope.launch {
            recipeRequestRepository.fetchAllMyRecipeRequests(userId.value)
        }
    }

    fun getApprovedRecipeRequests() {
        viewModelScope.launch {
            recipeRequestRepository.getAllRecipeRequests().catch { }.collect { recipeRequests ->
                var list = recipeRequests
                _recipeRequests.value = list
            }
        }
    }

    fun deleteRecipeRequest(recipeId: Int) {
        viewModelScope.launch {
            recipeRequestRepository.deleteRecipeRequest(recipeId).catch { }.collect {
                if (it == Constants.ERROR_CODE_SUCCESS) {
                    _errorMessage.value = "Recipe deleted successfully!"
                }
            }
        }
    }

    fun resetShouldFetch() {
        _shouldFetchMyRecipes.value = false
    }

    fun resetErrorMessage() {
        _errorMessage.value = Constants.EMPTY_STRING
    }
}
