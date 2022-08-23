package com.keralarecipemaster.admin.presentation.ui.recipe

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.keralarecipemaster.admin.presentation.viewmodel.RecipeListViewModel
import com.keralarecipemaster.admin.utils.Diet
import com.keralarecipemaster.admin.utils.DietFilter
import com.keralarecipemaster.admin.utils.UserType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecipesScreen(
    onFabClick: (() -> Unit)? = null,
    recipeViewModel: RecipeListViewModel,
    userType: UserType,
    navController: NavController
) {
    val recipes =
        if (userType == UserType.ADMIN) recipeViewModel.defaultRecipes else recipeViewModel.userAddedRecipes

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

    BackdropScaffold(
        scaffoldState = scaffoldState,
        appBar = {
            TopAppBar(
                title = { Text("Filters") },
                navigationIcon = {
                    if (scaffoldState.isConcealed) {
                        IconButton(
                            onClick = {
                                scope.launch { scaffoldState.reveal() }
                            }
                        ) {
                            Icon(
                                Icons.Default.List,
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
            ShowDietFilter(recipeViewModel, dietFilter, userType)
        },
        frontLayerContent = {
            Scaffold(
                floatingActionButton = {
                    onFabClick?.let {
                        FloatingActionButton(
                            onClick = onFabClick!!,
                            modifier = Modifier.padding(bottom = 100.dp)
                        ) {
                            Icon(Icons.Filled.Add, "")
                        }
                    }
                }
            ) {
                val coroutineScope = rememberCoroutineScope()
                Column(
                    modifier = Modifier.padding(
                        bottom = 100.dp,
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

                    LazyColumn {
                        items(recipesList) { recipe ->
                            RecipeComponent(
                                recipe,
                                recipeViewModel,
                                navController
                            )
                            Spacer(modifier = Modifier.size(10.dp))
                        }
                    }
                }
            }
        }
    ) {
    }
}

@Composable
fun ShowDietFilter(recipeViewModel: RecipeListViewModel, selectedDietType: String, userType: UserType) {
    Column(Modifier.padding(16.dp)) {
        Spacer(Modifier.size(10.dp))
        Text(text = "Filter by diet")
        Spacer(Modifier.size(10.dp))
        Row {
            DietFilter.values().forEach {
                RadioButton(
                    selected = it.name == selectedDietType,
                    onClick = {
                        recipeViewModel.onDietCategoryChange(filter = it.name, userType = userType)
                    }
                )
                Text(text = it.type)
                Spacer(modifier = Modifier.size(10.dp))
            }
        }
    }
}
