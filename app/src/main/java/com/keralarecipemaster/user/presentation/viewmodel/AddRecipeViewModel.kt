package com.keralarecipemaster.user.presentation.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.CountDownTimer
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keralarecipemaster.user.domain.model.Ingredient
import com.keralarecipemaster.user.domain.model.Restaurant
import com.keralarecipemaster.user.network.model.recipe.RecipeResponse
import com.keralarecipemaster.user.prefsstore.PrefsStore
import com.keralarecipemaster.user.presentation.ui.recipe.OnRatingBarCheck
import com.keralarecipemaster.user.repository.RecipeRepository
import com.keralarecipemaster.user.repository.RecipeRequestRepository
import com.keralarecipemaster.user.utils.Constants
import com.keralarecipemaster.user.utils.Constants.Companion.EMPTY_STRING
import com.keralarecipemaster.user.utils.Diet
import com.keralarecipemaster.user.utils.Meal
import com.keralarecipemaster.user.utils.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddRecipeViewModel @Inject constructor(
    val repository: RecipeRepository,
    private val recipeRequestRepository: RecipeRequestRepository,
    val prefsStore: PrefsStore
) :
    ViewModel(),
    OnRatingBarCheck {

    val recipeName: StateFlow<String>
        get() = _recipeName

    val description: StateFlow<String>
        get() = _description

    val dietType: StateFlow<String>
        get() = _dietType

    val mealType: StateFlow<String>
        get() = _mealType

    val ingredients: StateFlow<List<Ingredient>>
        get() = _ingredients

    val preparationMethod: StateFlow<String>
        get() = _preparationMethod

    val restaurantName: StateFlow<String>
        get() = _restaurantName

    val address: StateFlow<String>
        get() = _address

    val rating: StateFlow<Int>
        get() = _rating

    val errorMessage: StateFlow<String>
        get() = _errorMessage

    private val _errorMessage =
        MutableStateFlow(Constants.EMPTY_STRING)

    val location = MutableStateFlow(getInitialLocation())
    val addressText = mutableStateOf("")
    var isMapEditable = mutableStateOf(true)
    var timer: CountDownTimer? = null

    var image = Constants.EMPTY_STRING
    var imageName = Constants.EMPTY_STRING

    fun getInitialLocation(): Location {
        val initialLocation = Location("")
        initialLocation.latitude = 51.506874
        initialLocation.longitude = -0.139800
        return initialLocation
    }

    init {
        viewModelScope.launch {
            prefsStore.getUserId().catch { }.collect {
                _userId.value = it
            }
        }
    }

    private var _recipeName = MutableStateFlow(EMPTY_STRING)
    private var _dietType = MutableStateFlow(Diet.VEG.name)
    private var _mealType = MutableStateFlow(Meal.BREAKFAST.name)
    private var _ingredients = MutableStateFlow(listOf<Ingredient>())
    private var _description = MutableStateFlow(EMPTY_STRING)
    private var _preparationMethod = MutableStateFlow(EMPTY_STRING)
    private var _restaurantName = MutableStateFlow(EMPTY_STRING)
    private var _latitude = MutableStateFlow(EMPTY_STRING)
    private var _longitude = MutableStateFlow(EMPTY_STRING)
    private var _address = MutableStateFlow(EMPTY_STRING)
    private var _hasRestaurantDetails = MutableStateFlow(false)
    private var _rating = MutableStateFlow(0)

    private val _userId =
        MutableStateFlow(Constants.INVALID_USER_ID)

    fun onRecipeNameChange(recipeName: String) {
        this._recipeName.value = recipeName
    }

    fun onDescriptionChange(description: String) {
        this._description.value = description
    }

    fun onPreparationMethodChange(preparationMethod: String) {
        this._preparationMethod.value = preparationMethod
    }

    fun onRestaurantNameChange(restaurantName: String) {
        this._restaurantName.value = restaurantName
    }

    fun onStateChange(state: String) {
        this._address.value = state
    }

    fun onMealCategoryChange(mealType: String) {
        this._mealType.value = mealType
    }

    fun onDietCategoryChange(dietType: String) {
        this._dietType.value = dietType
    }

    fun addRecipe() {
        if (validateRecipeDetails()) {
            if (validateImage()) {
                viewModelScope.launch {
                    repository.addRecipe(
                        userId = _userId.value,
                        recipe =
                        RecipeResponse(
                            id = Constants.INVALID_RECIPE_ID,
                            recipeName = _recipeName.value,
                            description = _description.value,
                            preparationMethod = _preparationMethod.value,
                            ingredients = _ingredients.value,
                            diet = _dietType.value,
                            mealType = mealType.value,
                            addedBy = UserType.USER.name,
                            rating = _rating.value,
                            status = "",
                            restaurant = null,
                            image = image,
                            imageName = imageName
                        )
                    ).catch { }.collect {
                        if (it.first == Constants.ERROR_CODE_SUCCESS) {
                            _errorMessage.value = "Recipe added successfully"
                        } else {
                        _errorMessage.value = "Can't add recipe. Please try again later!"
                    }
                    }
                }
            } else {
                _errorMessage.value = "Add image!"
            }
        } else {
            _errorMessage.value = "Add mandatory fields!"
        }
    }


    fun addRecipeRequest() {
        if (validateRecipeDetails()) {
            if (validateImage()) {
                if (validateRestaurantDetails()) {
                    viewModelScope.launch {
                        recipeRequestRepository.addRecipeRequest(
                            userId = _userId.value,
                            recipe =
                            RecipeResponse(
                                id = Constants.INVALID_RECIPE_ID,
                                recipeName = _recipeName.value,
                                description = _description.value,
                                preparationMethod = _preparationMethod.value,
                                ingredients = _ingredients.value,
                                diet = _dietType.value,
                                mealType = mealType.value,
                                addedBy = UserType.OWNER.name,
                                image = image,
                                imageName = imageName,
                                rating = _rating.value,
                                status = "ApprovalPending",
                                restaurant = Restaurant(
                                    name = _restaurantName.value,
                                    latitude = location.value.latitude.toString(),
                                    longitude = location.value.longitude.toString(),
                                    address = _address.value
                                )
                            )
                        ).catch { }.collect {
                            if (it.first == Constants.ERROR_CODE_SUCCESS) {
                                _errorMessage.value = "Recipe Request submitted for approval!"
                            }
                        }
                    }
                } else {
                    _errorMessage.value = "Add restaurant details!"
                }
            } else {
                _errorMessage.value = "Add image!"
            }
        } else {
            _errorMessage.value = "Add mandatory fields!"
        }
    }

    fun updateRecipe(recipeId: Int?) {
        recipeId?.let {
            if (validateRecipeDetails()) {
                viewModelScope.launch {
                    repository.updateRecipe(
                        userId = _userId.value,
                        recipe =
                        RecipeResponse(
                            id = recipeId,
                            recipeName = _recipeName.value,
                            description = _description.value,
                            preparationMethod = _preparationMethod.value,
                            ingredients = _ingredients.value,
                            diet = _dietType.value,
                            mealType = mealType.value,
                            addedBy = UserType.USER.name,
                            rating = _rating.value,
                            status = "",
                            restaurant = null,
                            image = image,
                            imageName = imageName
                        )
                    ).catch { }.collect {
                        if (it == Constants.ERROR_CODE_SUCCESS) {
                            _errorMessage.value = "Recipe details updated successfully"
                        } else {
                            _errorMessage.value = "Can't add recipe! Try again!"
                        }
                    }
                }
            } else {
                _errorMessage.value = "Add mandatory fields"
            }
        }
    }

/*    private fun addRecipeToDb() {
        viewModelScope.launch {
            repository.addRecipe(
                RecipeEntity(
                    id = repository.count() + 1,
                    recipeName = _recipeName.value,
                    description = _description.value,
                    preparationMethod = _preparationMethod.value,
                    ingredients = _ingredients.value,
                    diet = Diet.valueOf(_dietType.value),
                    mealType = Meal.valueOf(mealType.value),
                    restaurantAddress = address.value,
                    restaurantLatitude = location.value.latitude.toString(),
                    restaurantLongitude = location.value.longitude.toString(),
                    restaurantName = restaurantName.value,
                    addedBy = UserType.valueOf(UserType.USER.name),
                    rating = _rating.value
                )
            )
        }
    }*/

/*    private fun updateRecipeInDb(recipeId: Int) {
        viewModelScope.launch {
            repository.updateRecipe(
                recipeName = recipeName.value,
                description = _description.value,
                recipeId = recipeId,
                diet = Diet.valueOf(_dietType.value),
                meal = Meal.valueOf(_mealType.value),
                ingredients = _ingredients.value,
                preparationMethod = _preparationMethod.value
            )
        }
    }*/

    private fun validateRestaurantDetails(): Boolean {
        return !(
            restaurantName.value.trim() == EMPTY_STRING || location.value.latitude.toString()
                .trim() == EMPTY_STRING || location.value.longitude.toString()
                .trim() == EMPTY_STRING || address.value.trim() == EMPTY_STRING
            )
    }

    private fun validateRecipeDetails(): Boolean {
        return !(recipeName.value.trim() == Constants.EMPTY_STRING || ingredients.value.isEmpty() || preparationMethod.value.trim() == Constants.EMPTY_STRING)
    }

    private fun validateImage(): Boolean {
        return !(image.trim() == Constants.EMPTY_STRING)
    }

    override fun onChangeRating(rating: Int) {
        _rating.value = rating
    }

    fun getRecipeDetails(recipeId: Int) {
        viewModelScope.launch {
            repository.getRecipeDetails(recipeId).catch { }.collect {
                _recipeName.value = it.recipeName
                _address.value = it.restaurantAddress
                _restaurantName.value = it.restaurantName
                _description.value = it.description
                _ingredients.value = it.ingredients
                _preparationMethod.value = it.preparationMethod
                _dietType.value = it.diet.name
                _mealType.value = it.mealType.name
                _rating.value = it.rating
                image = it.image
                imageName = it.imageName
            }
        }
    }

    fun onAddRecipeClick(ingredientName: String, ingredientQuantity: String) {
        val list: ArrayList<Ingredient> = arrayListOf()
        _ingredients.value.forEach {
            list.add(it)
        }

        list.add(Ingredient(name = ingredientName, quantity = ingredientQuantity))
        _ingredients.value = list
    }

    fun clearIngredients() {
        _ingredients.value = emptyList()
    }

    fun onTextChanged(context: Context, text: String) {
        if (text == "") {
            return
        }
        timer?.cancel()
        timer = object : CountDownTimer(1000, 1500) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                location.value = getLocationFromAddress(context, text)
                _restaurantName.value = text
            }
        }.start()
    }

    fun getLocationFromAddress(context: Context, strAddress: String): Location {
        val geocoder = Geocoder(context, Locale.getDefault())
        val address: Address?

        val addresses: List<Address>? = geocoder.getFromLocationName(strAddress, 1)

        addresses?.let {
            if (it.isNotEmpty()) {
                address = it[0]

                var loc = Location("")
                loc.latitude = address.latitude
                loc.longitude = address.longitude
                return loc
            }
        }

        return location.value
    }

    private fun setLocation(loc: Location) {
        location.value = loc
    }

    fun getAddressFromLocation(context: Context): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var addresses: List<Address>? = null
        var addressText = ""

        try {
            addresses =
                geocoder.getFromLocation(location.value.latitude, location.value.longitude, 1)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        val address: Address? = addresses?.get(0)
        addressText = address?.getAddressLine(0) ?: ""

        return addressText
    }

    fun onGetAddressFromLocation(text: String) {
        _address.value = text
    }

    fun resetErrorMessage() {
        _errorMessage.value = Constants.EMPTY_STRING
    }

    fun setImageBitmap(bitmap: Bitmap) {
        try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val image: String =
                Base64.encodeToString(
                    byteArrayOutputStream.toByteArray(),
                    Base64.DEFAULT
                )
            val name = java.lang.String.valueOf(Calendar.getInstance().timeInMillis)
            this.image = image
            this.imageName = name
        } catch (e: Exception) {
        }
    }
}
