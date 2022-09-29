package com.keralarecipemaster.user.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.keralarecipemaster.user.domain.model.RecipeEntity
import com.keralarecipemaster.user.prefsstore.AuthenticationState
import com.keralarecipemaster.user.prefsstore.PrefsStore
import com.keralarecipemaster.user.repository.RecipeRepository
import com.keralarecipemaster.user.utils.Constants
import com.keralarecipemaster.user.utils.DietFilter
import com.keralarecipemaster.user.utils.MealFilter
import com.keralarecipemaster.user.utils.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    val recipeRepository: RecipeRepository,
    val prefsStore: PrefsStore,
    application: Application
) : AndroidViewModel(application) {
    private var _famousRecipes = MutableStateFlow<List<RecipeEntity>>(emptyList())
    private var _userAddedRecipes = MutableStateFlow<List<RecipeEntity>>(emptyList())
    private val _dietTypeFilter = MutableStateFlow(DietFilter.ALL.name)
    private val _mealTypeFilter = MutableStateFlow(MealFilter.ALL.name)

    val famousRecipes: StateFlow<List<RecipeEntity>>
        get() = _famousRecipes

    val userAddedRecipes: StateFlow<List<RecipeEntity>>
        get() = _userAddedRecipes

    val dietTypeFilter: StateFlow<String>
        get() = _dietTypeFilter

    val mealTypeFilter: StateFlow<String>
        get() = _mealTypeFilter

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
        fetchFamousRecipes()
        getFamousRecipesFromDb()
        getUserAddedeRecipes()
    }

    fun fetchMyRecipes() {
        viewModelScope.launch {
            recipeRepository.fetchMyRecipes(userId = _userId.value)
        }
    }

    private fun fetchFamousRecipes() {
        viewModelScope.launch {
            recipeRepository.fetchFamousRecipes()
        }
    }

    fun getFamousRecipesFromDb() {
        viewModelScope.launch {
            recipeRepository.getFamousRecipes().catch { }.collect { recipes ->
                var list = recipes
                if (_dietTypeFilter.value != DietFilter.ALL.name) {
                    list = recipes.filter {
                        it.diet.name == _dietTypeFilter.value
                    }
                }

                if (_mealTypeFilter.value != MealFilter.ALL.name) {
                    list = list.filter {
                        it.mealType.name == _mealTypeFilter.value
                    }
                }
                _famousRecipes.value = list
            }
        }
    }

    fun getUserAddedeRecipes() {
        viewModelScope.launch {
            recipeRepository.getUserAddedRecipes().catch { }.collect { recipes ->
                var list = recipes
                if (_dietTypeFilter.value != DietFilter.ALL.name) {
                    list = recipes.filter {
                        it.diet.name == _dietTypeFilter.value
                    }
                }

                if (_mealTypeFilter.value != MealFilter.ALL.name) {
                    list = list.filter {
                        it.mealType.name == _mealTypeFilter.value
                    }
                }
                _userAddedRecipes.value = list
            }
        }
    }

    fun deleteRecipe(recipeId: Int) {
        viewModelScope.launch {
            recipeRepository.deleteRecipe(recipeId).catch {  }.collect {
                if (it == Constants.ERROR_CODE_SUCCESS) {
                    _errorMessage.value = "Recipe deleted successfully!"
                }
            }
        }
    }

    fun onQueryChanged(query: String, addedBy: UserType) {
        if (query.isEmpty()) {
            if (addedBy == UserType.OWNER) getFamousRecipesFromDb() else getUserAddedeRecipes()
        } else {
            viewModelScope.launch {
                recipeRepository.searchResults(query, addedBy).catch { }.collect {
                    if (addedBy == UserType.OWNER) _famousRecipes.value = it
                    else _userAddedRecipes.value = it
                }
            }
        }
    }

    fun onDietFilterChange(diet: String) {
        _dietTypeFilter.value = diet
        getFamousRecipesFromDb()
        getUserAddedeRecipes()
    }

    fun onMealFilterChange(meal: String) {
        _mealTypeFilter.value = meal
        getFamousRecipesFromDb()
        getUserAddedeRecipes()
    }

    fun resetShouldFetch() {
        _shouldFetchMyRecipes.value = false
    }

    fun resetErrorMessage() {
        _errorMessage.value = Constants.EMPTY_STRING
    }
}
