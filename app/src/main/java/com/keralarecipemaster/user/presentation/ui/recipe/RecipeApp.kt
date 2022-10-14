package com.keralarecipemaster.user.presentation.ui.recipe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.keralarecipemaster.user.domain.model.util.FamousRestaurants
import com.keralarecipemaster.user.prefsstore.AuthenticationState
import com.keralarecipemaster.user.presentation.ui.SettingsActivity
import com.keralarecipemaster.user.presentation.ui.authentication.AuthenticationActivity
import com.keralarecipemaster.user.presentation.ui.authentication.logout
import com.keralarecipemaster.user.presentation.ui.home.RecipeNavHost
import com.keralarecipemaster.user.presentation.ui.theme.KeralaRecipeMasterUserTheme
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.presentation.viewmodel.LocationNotificationViewModel
import com.keralarecipemaster.user.presentation.viewmodel.RecipeListViewModel
import com.keralarecipemaster.user.presentation.viewmodel.RecipeRequestViewModel
import com.keralarecipemaster.user.service.GoogleService
import com.keralarecipemaster.user.utils.BottomNavigationScreens
import com.keralarecipemaster.user.utils.Constants

@Composable
fun RecipeApp(
    recipeListViewModel: RecipeListViewModel,
    authenticationViewModel: AuthenticationViewModel,
    recipeRequestViewModel: RecipeRequestViewModel,
    locationNotificationViewModel: LocationNotificationViewModel,
    authenticationState: AuthenticationState
) {
    KeralaRecipeMasterUserTheme(authenticationState = authenticationState, content = {
        val navController = rememberNavController()
        val scaffoldState = rememberScaffoldState()
        val lifeCycleOwner = LocalLifecycleOwner.current
        val context = LocalContext.current
        val activity = (context as? Activity)

        val nameValue = authenticationViewModel.name
        val nameValueLifeCycleAware =
            remember(nameValue, lifeCycleOwner) {
                nameValue.flowWithLifecycle(
                    lifeCycleOwner.lifecycle,
                    Lifecycle.State.STARTED
                )
            }
        val name by nameValueLifeCycleAware.collectAsState(initial = AuthenticationState.INITIAL_STATE)

        val isNotificationEnabledValue = locationNotificationViewModel.isNotificationEnabled
        val isNotificationEnabledValueLifeCycleAware =
            remember(isNotificationEnabledValue, lifeCycleOwner) {
                isNotificationEnabledValue.flowWithLifecycle(
                    lifeCycleOwner.lifecycle,
                    Lifecycle.State.STARTED
                )
            }
        val isNotificationEnabled by isNotificationEnabledValueLifeCycleAware.collectAsState(initial = false)

        val famousRestaurantsValue = locationNotificationViewModel.famousRestaurants
        val famousRestaurantsValueLifeCycleAware =
            remember(famousRestaurantsValue, lifeCycleOwner) {
                famousRestaurantsValue.flowWithLifecycle(
                    lifeCycleOwner.lifecycle,
                    Lifecycle.State.STARTED
                )
            }
        val famousRestaurants by famousRestaurantsValueLifeCycleAware.collectAsState(initial = emptyList())

        if (isNotificationEnabled) {
            if (famousRestaurants.isNotEmpty()) {
                val intent = Intent(
                    context.applicationContext,
                    GoogleService::class.java
                )
                val bundle = Bundle()
                val restaurants = FamousRestaurants(famousRestaurants)
                bundle.putString(Constants.KEY_FAMOUS_RESTAURANTS, Gson().toJson(restaurants))
                intent.putExtras(bundle)
                context.startService(intent)
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(
                        text = "Hi ${
                        if (authenticationState == AuthenticationState.AUTHENTICATED_USER || authenticationState == AuthenticationState.AUTHENTICATED_RESTAURANT_OWNER) {
                            name
                        } else {
                            "Guest user"
                        }
                        }!"
                    )
                }, actions = {
                        if (authenticationState == AuthenticationState.LOGGED_IN_AS_GUEST) {
                            IconButton(onClick = {
                                val intent = Intent(activity, AuthenticationActivity::class.java)
                                val bundle = Bundle()
                                bundle.putBoolean(Constants.IS_FROM_PROFILE_SCREEN, true)
                                intent.putExtras(bundle)
                                context.startActivity(intent)
                            }) {
                                Icon(Icons.Default.AccountCircle, "")
                            }
                        } else if (authenticationState == AuthenticationState.AUTHENTICATED_USER) {
                            Row {
                                IconButton(onClick = {
                                    context.startActivity(
                                        Intent(
                                            activity,
                                            SettingsActivity::class.java
                                        )
                                    )
                                }) {
                                    Icon(Icons.Default.Settings, "")
                                }

                                IconButton(onClick = {
                                    logout(authenticationViewModel, activity, context)
                                }) {
                                    Icon(Icons.Default.ExitToApp, "Logout")
                                }
                            }
                        }
                    })
            },
            modifier = Modifier.fillMaxWidth(),
            bottomBar = {
                if (authenticationState != AuthenticationState.LOGGED_IN_AS_GUEST) {
                    RecipeAppBottomNavigation(
                        navController,
                        getBottomNavigationItems(authenticationState)
                    )
                }
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
    })
}

fun getBottomNavigationItems(authenticationState: AuthenticationState): ArrayList<BottomNavigationScreens> {
    val bottomNavigationItems = arrayListOf<BottomNavigationScreens>()
    when (authenticationState) {
        AuthenticationState.LOGGED_IN_AS_GUEST -> {
            bottomNavigationItems.add(BottomNavigationScreens.FamousRecipes)
        }
        AuthenticationState.AUTHENTICATED_USER -> {
            bottomNavigationItems.add(BottomNavigationScreens.FamousRecipes)
            bottomNavigationItems.add(BottomNavigationScreens.MyRecipes)
//            bottomNavigationItems.add(BottomNavigationScreens.Profile)
        }
        AuthenticationState.AUTHENTICATED_RESTAURANT_OWNER -> {
            bottomNavigationItems.add(BottomNavigationScreens.ApprovedRecipes)
            bottomNavigationItems.add(BottomNavigationScreens.PendingRequests)
            bottomNavigationItems.add(BottomNavigationScreens.Profile)
        }
    }
    return bottomNavigationItems
}

@Composable
fun RecipeAppBottomNavigation(
    navController: NavHostController,
    bottomNavigationItems: List<BottomNavigationScreens>
) {
    BottomNavigation {
        val currentRoute = currentRute(navController)
        bottomNavigationItems.forEach { screen ->
            BottomNavigationItem(
                selected = currentRoute == screen.route,
                label = { Text(text = "${screen.resourceId}") },
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                icon = { Icon(screen.icon, contentDescription = null) }
            )
        }
    }
}

@Composable
private fun currentRute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
