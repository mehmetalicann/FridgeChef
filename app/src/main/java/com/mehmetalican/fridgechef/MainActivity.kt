package com.mehmetalican.fridgechef

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.padding
import com.mehmetalican.fridgechef.presentation.ingredient.IngredientViewModel
import com.mehmetalican.fridgechef.ui.theme.FridgeChefTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FridgeChefTheme {
                val navController = rememberNavController()
                val sharedViewModel: IngredientViewModel = hiltViewModel()

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination

                            NavigationBarItem(
                                icon = { 
                                    Icon(
                                        Icons.Default.Home, 
                                        contentDescription = "Ana Sayfa"
                                    ) 
                                },
                                label = { Text("Tarif Bul") },
                                selected = currentDestination?.route == "ingredient_selection",
                                onClick = {
                                    navController.navigate("ingredient_selection") {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                            NavigationBarItem(
                                icon = { 
                                    Icon(
                                        Icons.Default.Favorite, 
                                        contentDescription = "Favoriler"
                                    ) 
                                },
                                label = { Text("Favoriler") },
                                selected = currentDestination?.route == "favorites",
                                onClick = {
                                    navController.navigate("favorites") {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "ingredient_selection",
                        modifier = androidx.compose.ui.Modifier.fillMaxSize().padding(innerPadding)
                    ) {
                        composable("ingredient_selection") {
                            com.mehmetalican.fridgechef.presentation.ingredient.IngredientSelectionScreen(
                                viewModel = sharedViewModel,
                                onSearchClicked = {
                                    sharedViewModel.searchRecipes()
                                    navController.navigate("recipe_list")
                                }
                            )
                        }
                        composable("recipe_list") {
                            com.mehmetalican.fridgechef.presentation.recipe_list.RecipeListScreen(
                                viewModel = sharedViewModel,
                                onRecipeClick = { recipeId ->
                                    navController.navigate("recipe_detail/$recipeId")
                                }
                            )
                        }
                        composable(
                            route = "recipe_detail/{recipeId}",
                            arguments = listOf(
                                androidx.navigation.navArgument("recipeId") {
                                    type = androidx.navigation.NavType.IntType
                                }
                            )
                        ) { backStackEntry ->
                            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: 0
                            com.mehmetalican.fridgechef.presentation.recipe_detail.RecipeDetailScreen(
                                recipeId = recipeId,
                                viewModel = sharedViewModel
                            )
                        }
                        composable("favorites") {
                            com.mehmetalican.fridgechef.presentation.favorites.FavoritesScreen(
                                viewModel = sharedViewModel,
                                onRecipeClick = { recipeId ->
                                    navController.navigate("recipe_detail/$recipeId")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FridgeChefTheme {
        // Preview placeholder
    }
}
