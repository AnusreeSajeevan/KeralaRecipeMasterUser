package com.keralarecipemaster.user.presentation.ui.recipe

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.keralarecipemaster.user.presentation.ui.home.HomeItems
import com.keralarecipemaster.user.presentation.ui.home.RecipeNavHost
import com.keralarecipemaster.user.presentation.ui.theme.KeralaRecipeMasterUserTheme
import com.keralarecipemaster.user.presentation.viewmodel.RecipeListViewModel

@Composable
fun RecipeApp(viewModel: RecipeListViewModel) {
    KeralaRecipeMasterUserTheme {
        val items = HomeItems.values().toList()
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = HomeItems.fromRoute(backstackEntry.value?.destination?.route)
        val scaffoldState = rememberScaffoldState()
        Scaffold(
            modifier = Modifier.fillMaxWidth(),
            bottomBar = {
                BottomNavigationBar(items = items, onTabSelected = { screen ->
//                    bottomBarOffsetHeightPx.value = 0f
                    if (screen != currentScreen) {
                        navController.navigate(screen.name) {
                            // popUpTo(MainMenuItems.Home.name)
                            navController.popBackStack()
                        }
                    }
                }, currentScreen = currentScreen, modifier = Modifier.fillMaxWidth())
//                BottomNavigationBar(allScreens, navController)
            },
            scaffoldState = scaffoldState
        ) {
            RecipeNavHost(viewModel, navController)
        }
    }
}

@Composable
fun BottomNavigationBar(
    modifier: Modifier,
    items: List<HomeItems>,
    onTabSelected: (HomeItems) -> Unit,
    currentScreen: HomeItems
) {
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = navBackStackEntry?.destination?.route
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
        NavigationBar(
            tonalElevation = 4.dp,
            containerColor = Color.Black
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    icon = {
                        if (item == currentScreen) {
                            androidx.compose.material3.Icon(
                                imageVector = item.selectedIcon,
                                contentDescription = null
                            )
                        } else {
                            androidx.compose.material3.Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    label = {
                        Text(
                            text = item.name,
                            color = Color.White
                        )
                    },
                    selected = currentScreen == item,
                    onClick = {
                        onTabSelected(item)
                    }
                )
            }
        }
    }
}
