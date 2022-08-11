package com.keralarecipemaster.admin.presentation.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.keralarecipemaster.admin.domain.model.Recipe
import com.keralarecipemaster.admin.presentation.ui.DefaultRecipesScreen
import com.keralarecipemaster.admin.presentation.ui.NavigationItems
import com.keralarecipemaster.admin.presentation.ui.ProfileScreen
import com.keralarecipemaster.admin.presentation.ui.UserAddedRecipesScreen
import com.keralarecipemaster.admin.presentation.ui.theme.KeralaRecipeMasterAdminTheme
import com.keralarecipemaster.admin.presentation.viewmodel.RecipeListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    private val recipeViewModel: RecipeListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val defaultRecipes = recipeViewModel.defaultRecipes.observeAsState(listOf()).value
            val userAddedRecipes = recipeViewModel.userAddedRecipes.observeAsState(listOf()).value
            HomeActivityView(defaultRecipes, userAddedRecipes)
        }
    }

    @Composable
    fun HomeActivityView(defaultRecipes: List<Recipe>, userAddedRecipes: List<Recipe>) {
        KeralaRecipeMasterAdminTheme {
            val allScreens = NavigationItems.values().toList()
            var currentScreen by rememberSaveable { mutableStateOf(NavigationItems.DefaultRecipes) }
            val navController = rememberNavController()
            val scaffoldState = rememberScaffoldState()

            Scaffold(
                scaffoldState = scaffoldState,
                bottomBar = {
                    BottomNavigationBar(allScreens, navController)
                } /*,
                topBar = {
                    TopAppBar(
                        // in below line we are
                        // adding title to our top bar.
                        title = {
                            OutlinedTextField(
                                label = { Text(text = "") },
                                onValueChange = {
                                },
                                text = "",
                                color = Color.Black
                            )
                        },
                        navigationIcon = {
                            // navigation icon is use
                            // for drawer icon.
                            IconButton(onClick = { }) {
                                // below line is use to
                                // specify navigation icon.
                                Icon(Icons.Filled.Search, "", tint = Color.Black)
                            }
                        },
                        // below line is use to give background color
                        backgroundColor = colorResource(id = R.color.white),

                        // content color is use to give
                        // color to our content in our toolbar.
                        contentColor = Color.White,

                        // below line is use to give
                        // elevation to our toolbar.
                        elevation = 12.dp
                    )
                }*/
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding),
                    startDestination = NavigationItems.DefaultRecipes.route
                ) {
                    composable(NavigationItems.DefaultRecipes.route) {
                        DefaultRecipesScreen(defaultRecipes, onFabClick = {
                            startActivity(Intent(this@HomeActivity, AddRecipeActivity::class.java))
                        }, recipeViewModel)
                    }
                    composable(NavigationItems.UserAddedRecipes.route) {
                        UserAddedRecipesScreen(userAddedRecipes, recipeViewModel)
                    }
                    composable(NavigationItems.Account.route) {
                        ProfileScreen()
                    }
                }
            }
        }
    }

    @Composable
    fun BottomNavigationBar(allScreens: List<NavigationItems>, navController: NavHostController) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        BottomNavigation(backgroundColor = Color.Black) {
            allScreens.forEach { item ->
                BottomNavigationItem(
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.White.copy(0.4f),
                    alwaysShowLabel = true,
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route)
                        navController.graph.startDestinationRoute?.let { route ->
                        }
                    },
                    icon = {
                        Icon(imageVector = item.icon, contentDescription = item.title)
                    },
                    label = { Text(text = item.name) }
                )
            }
        }
    }
}
