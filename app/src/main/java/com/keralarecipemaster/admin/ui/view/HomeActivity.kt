package com.keralarecipemaster.admin.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.keralarecipemaster.admin.ui.DefaultRecipesScreen
import com.keralarecipemaster.admin.ui.NavigationItems
import com.keralarecipemaster.admin.ui.ProfileScreen
import com.keralarecipemaster.admin.ui.UserAddedRecipesScreen
import com.keralarecipemaster.admin.ui.theme.KeralaRecipeMasterAdminTheme
import com.keralarecipemaster.admin.viewmodel.RecipeListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    private val recipeViewModel: RecipeListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeActivityView(recipeViewModel)
        }
    }

    @Composable
    fun HomeActivityView(recipeViewModel: RecipeListViewModel = viewModel()) {
        KeralaRecipeMasterAdminTheme {
            val allScreens = NavigationItems.values().toList()
            var currentScreen by rememberSaveable { mutableStateOf(NavigationItems.DefaultRecipes) }
            val navController = rememberNavController()
            val scaffoldState = rememberScaffoldState()

            Scaffold(
                scaffoldState = scaffoldState,
                bottomBar = {
                    BottomNavigationBar(allScreens, navController)
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding),
                    startDestination = NavigationItems.DefaultRecipes.route
                ) {
                    composable(NavigationItems.DefaultRecipes.route) {
                        DefaultRecipesScreen()
                    }
                    composable(NavigationItems.UserAddedRecipes.route) {
                        UserAddedRecipesScreen()
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
