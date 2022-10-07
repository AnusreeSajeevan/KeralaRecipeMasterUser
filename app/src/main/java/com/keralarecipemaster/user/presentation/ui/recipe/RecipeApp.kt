package com.keralarecipemaster.user.presentation.ui.recipe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.keralarecipemaster.user.prefsstore.AuthenticationState
import com.keralarecipemaster.user.presentation.ui.authentication.AuthenticationActivity
import com.keralarecipemaster.user.presentation.ui.home.HomeItems
import com.keralarecipemaster.user.presentation.ui.home.RecipeNavHost
import com.keralarecipemaster.user.presentation.ui.theme.KeralaRecipeMasterUserTheme
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.presentation.viewmodel.LocationNotificationViewModel
import com.keralarecipemaster.user.presentation.viewmodel.RecipeListViewModel
import com.keralarecipemaster.user.presentation.viewmodel.RecipeRequestViewModel
import com.keralarecipemaster.user.utils.Constants

@Composable
fun RecipeApp(
    recipeListViewModel: RecipeListViewModel,
    authenticationViewModel: AuthenticationViewModel,
    recipeRequestViewModel: RecipeRequestViewModel,
    locationNotificationViewModel: LocationNotificationViewModel,
    authenticationState: AuthenticationState
) {
    KeralaRecipeMasterUserTheme {
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = HomeItems.fromRoute(backstackEntry.value?.destination?.route)
        val scaffoldState = rememberScaffoldState()
        val lifeCycleOwner = LocalLifecycleOwner.current

        /* val authenticationStateValue = authenticationViewModel.authenticationState
         val authenticationStateValueLifeCycleAware =
             remember(authenticationStateValue, lifeCycleOwner) {
                 authenticationStateValue.flowWithLifecycle(
                     lifeCycleOwner.lifecycle,
                     Lifecycle.State.STARTED
                 )
             }
         val authenticationState by authenticationStateValueLifeCycleAware.collectAsState(initial = AuthenticationState.INITIAL_STATE)*/

//        if (authenticationState == AuthenticationState.INITIAL_STATE) {
//            ShowLoginScreen(authenticationViewModel)
//        } else {
        val context = LocalContext.current
        val activity = (context as? Activity)

        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = "Welcome Guest user!") }, actions = {
                    IconButton(onClick = {
                        val intent = Intent(activity, AuthenticationActivity::class.java)
                        val bundle = Bundle()
                        bundle.putBoolean(Constants.IS_FROM_PROFILE_SCREEN, true)
                        intent.putExtras(bundle)
                        context.startActivity(intent)
                    }) {
                        Icon(Icons.Default.AccountCircle, "")
                    }
                })
            },
            modifier = Modifier.fillMaxWidth(),
            bottomBar = {
//                if (authenticationState == AuthenticationState.AUTHENTICATED_USER || authenticationState == AuthenticationState.AUTHENTICATED_RESTAURANT_OWNER) {
                val items = arrayListOf<HomeItems>()
                HomeItems.values().forEach {
                    if (it == HomeItems.FamousRecipes) {
                        if (authenticationState == AuthenticationState.AUTHENTICATED_USER || authenticationState == AuthenticationState.LOGGED_IN_AS_GUEST) {
                            items.add(it)
                        }
                    } else if (it == HomeItems.MyRecipes) {
                        if (authenticationState == AuthenticationState.AUTHENTICATED_USER) {
                            items.add(it)
                        }
                    } else if (it == HomeItems.MyRequests) {
                        if (authenticationState == AuthenticationState.AUTHENTICATED_RESTAURANT_OWNER) {
                            items.add(it)
                        }
                    } else if (it == HomeItems.Account) {
                        if (authenticationState == AuthenticationState.AUTHENTICATED_USER || authenticationState == AuthenticationState.AUTHENTICATED_RESTAURANT_OWNER) {
                            items.add(it)
                        }
                    } else {
                        items.add(it)
                    }
                }

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
//                }
            },
            scaffoldState = scaffoldState
        ) {
            RecipeNavHost(
                recipeListViewModel = recipeListViewModel,
                authenticationViewModel = authenticationViewModel,
                recipeRequestViewModel = recipeRequestViewModel,
                navController = navController,
                authenticationState = authenticationState,
                locationNotificationViewModel = locationNotificationViewModel
            )
        }
    }
//    }
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
