package com.keralarecipemaster.user.presentation.ui.recipe

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.keralarecipemaster.user.prefsstore.AuthenticationState
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.presentation.viewmodel.RecipeListViewModel
import com.keralarecipemaster.user.utils.Constants
import com.keralarecipemaster.user.utils.DietFilter
import com.keralarecipemaster.user.utils.MealFilter
import com.keralarecipemaster.user.utils.UserType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecipesScreen(
    onFabClick: (() -> Unit)? = null,
    recipeViewModel: RecipeListViewModel,
    authenticationViewModel: AuthenticationViewModel,
    userType: UserType,
    navController: NavController
) {
    val recipes =
        if (userType == UserType.OWNER) recipeViewModel.famousRecipes else recipeViewModel.userAddedRecipes

    val lifecycleOwner = LocalLifecycleOwner.current
    val recipesRecipesFlowLifecycleAware = remember(recipes, lifecycleOwner) {
        recipes.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
    }
    val recipesList by recipesRecipesFlowLifecycleAware.collectAsState(emptyList())

    var query by remember {
        mutableStateOf("")
    }

    val scaffoldState = rememberBackdropScaffoldState(
        BackdropValue.Concealed
    )
    val scope = rememberCoroutineScope()
    val lifeCycleOwner = LocalLifecycleOwner.current

    // Diet filter
    val dietFilterValue = recipeViewModel.dietTypeFilter
    val dietFlowLifeCycleAware = remember(dietFilterValue, lifeCycleOwner) {
        dietFilterValue.flowWithLifecycle(lifeCycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val dietFilter by dietFlowLifeCycleAware.collectAsState(DietFilter.ALL.name)

    // Meal filter
    val mealFilterValue = recipeViewModel.mealTypeFilter
    val mealFlowLifeCycleAware = remember(mealFilterValue, lifeCycleOwner) {
        mealFilterValue.flowWithLifecycle(lifeCycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val mealFilter by mealFlowLifeCycleAware.collectAsState(MealFilter.ALL.name)

    val authenticationStateValue = authenticationViewModel.authenticationState
    val authenticationStateLifeCycleAware = remember(authenticationStateValue, lifeCycleOwner) {
        authenticationStateValue.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }
    val authenticationState by authenticationStateLifeCycleAware.collectAsState(initial = AuthenticationState.INITIAL_STATE)

    val shouldFetchMyRecipesValue = recipeViewModel.shouldFetchMyRecipes
    val shouldFetchMyRecipesLifeCycleAware = remember(shouldFetchMyRecipesValue, lifeCycleOwner) {
        shouldFetchMyRecipesValue.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }
    val shouldFetchMyRecipes by shouldFetchMyRecipesLifeCycleAware.collectAsState(initial = false)

    if (shouldFetchMyRecipes) {
        recipeViewModel.fetchMyRecipes()
        recipeViewModel.resetShouldFetch()
    }

    val errorMessageValue = recipeViewModel.errorMessage
    val errorMessageValueLifeCycleAware =
        remember(errorMessageValue, lifeCycleOwner) {
            errorMessageValue.flowWithLifecycle(
                lifeCycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }
    val errorMessage by errorMessageValueLifeCycleAware.collectAsState(initial = Constants.EMPTY_STRING)

    val context = LocalContext.current

    if (errorMessage.isNotEmpty()) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        recipeViewModel.resetErrorMessage()
    }

    BackdropScaffold(
        scaffoldState = scaffoldState,
        appBar = {
            TopAppBar(
                title = { Text("Filter recipes") },
                navigationIcon = {
                    if (scaffoldState.isConcealed) {
                        IconButton(
                            onClick = {
                                scope.launch { scaffoldState.reveal() }
                            }
                        ) {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "filter"
                            )
                        }
                    } else {
                        IconButton(
                            onClick = {
                                scope.launch { scaffoldState.conceal() }
                            }
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close"
                            )
                        }
                    }
                },
                elevation = 0.dp,
                backgroundColor = Color.Transparent
            )
        },
        backLayerContent = {
            DietFilterComponent(recipeViewModel, dietFilter)
            MealFilterComponent(recipeViewModel, mealFilter)
        },
        frontLayerContent = {
            Scaffold(
                floatingActionButton = {
                    if ((authenticationState == AuthenticationState.AUTHENTICATED_USER && userType == UserType.USER) ||
                        authenticationState == AuthenticationState.AUTHENTICATED_RESTAURANT_OWNER && userType == UserType.USER
                    ) {
                        onFabClick?.let {
                            FloatingActionButton(
                                onClick = onFabClick!!,
                                modifier = Modifier.padding(bottom = 56.dp)
                            ) {
                                Icon(Icons.Filled.Add, "")
                            }
                        }
                    }
                }
            ) {
                val coroutineScope = rememberCoroutineScope()
                Column(
                    modifier = Modifier.padding(
                        bottom = 56.dp,
                        top = 8.dp,
                        start = 8.dp,
                        end = 8.dp
                    )
                ) {
                    OutlinedTextField(
                        value = query,
                        label = {
                            Text(text = "Search recipe")
                        },
                        onValueChange = {
                            query = it
                            recipeViewModel.onQueryChanged(query, userType)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(Icons.Filled.Search, null)
                        }
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    Spacer(modifier = Modifier.size(10.dp))
                    if (recipesList.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = if (userType == UserType.USER) "You haven't added any recipes!\n Click on + to add recipes!" else "No Famous Recipes available at this moment!",
                                modifier = Modifier.align(Alignment.Center),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn {
                            items(recipesList) { recipe ->
                                if (userType == UserType.OWNER) {
                                    RestaurantRecipeComponent(
                                        recipe,
                                        recipeViewModel,
                                        navController
                                    )
                                    Spacer(modifier = Modifier.size(10.dp))
                                } else {
                                    UserRecipeComponent(
                                        recipe,
                                        recipeViewModel
                                    )
                                    Spacer(modifier = Modifier.size(10.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    ) {
    }
}

@Composable
fun DietFilterComponent(recipeViewModel: RecipeListViewModel, selectedDietType: String) {
    Column(Modifier.padding(16.dp)) {
        Spacer(Modifier.size(10.dp))
        Text(text = "Diet", fontWeight = FontWeight.Bold)
        Spacer(Modifier.size(10.dp))
        Row {
            DietFilter.values().forEach {
                RadioButton(
                    selected = it.name == selectedDietType,
                    onClick = {
                        recipeViewModel.onDietFilterChange(diet = it.name)
                    }
                )
                Text(text = it.type, modifier = Modifier.align(Alignment.CenterVertically))
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }
}

@Composable
fun MealFilterComponent(recipeViewModel: RecipeListViewModel, selectedMealType: String) {
    Column(Modifier.padding(4.dp)) {
//        Spacer(Modifier.size(8.dp))
        Text(text = "Meal", modifier = Modifier.padding(start = 16.dp), fontWeight = FontWeight.Bold)
        Spacer(Modifier.size(8.dp))
        Column {
            MealFilter.values().forEach {
                Column {
                    Row {
                        RadioButton(
                            selected = it.name == selectedMealType,
                            onClick = {
                                recipeViewModel.onMealFilterChange(meal = it.name)
                            }
                        )
                        Text(text = it.type, modifier = Modifier.align(Alignment.CenterVertically))
                    }
                }
            }
        }
    }
}
