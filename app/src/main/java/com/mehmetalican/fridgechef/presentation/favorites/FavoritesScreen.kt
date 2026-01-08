package com.mehmetalican.fridgechef.presentation.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mehmetalican.fridgechef.presentation.ingredient.IngredientViewModel
import com.mehmetalican.fridgechef.presentation.recipe_list.RecipeList

@Composable
fun FavoritesScreen(
    viewModel: IngredientViewModel = hiltViewModel(),
    onRecipeClick: (Int) -> Unit
) {
    val favoriteRecipes by viewModel.favoriteRecipes.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Favorilerim",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        
        if (favoriteRecipes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Henüz favori tarifiniz yok.")
            }
        } else {
            RecipeList(
                recipes = favoriteRecipes,
                onRecipeClick = onRecipeClick
            )
        }
    }
}
