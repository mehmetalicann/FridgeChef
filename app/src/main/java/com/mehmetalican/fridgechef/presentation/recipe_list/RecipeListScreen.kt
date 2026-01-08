package com.mehmetalican.fridgechef.presentation.recipe_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mehmetalican.fridgechef.common.Resource
import com.mehmetalican.fridgechef.domain.model.Recipe
import com.mehmetalican.fridgechef.presentation.ingredient.IngredientViewModel

@Composable
fun RecipeListScreen(
    viewModel: IngredientViewModel = hiltViewModel(),
    onRecipeClick: (Int) -> Unit
) {
    val state by viewModel.recipeSearchState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (val resource = state) {
            is Resource.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is Resource.Success -> {
                val recipes = resource.data ?: emptyList()
                if (recipes.isEmpty()) {
                    Text(
                        text = "Tarif bulunamadı.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    RecipeList(recipes = recipes, onRecipeClick = onRecipeClick)
                }
            }
            is Resource.Error -> {
                Text(
                    text = resource.message ?: "Bilinmeyen bir hata oluştu",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun RecipeList(
    recipes: List<Recipe>,
    onRecipeClick: (Int) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(recipes) { recipe ->
            RecipeItem(recipe = recipe, onClick = onRecipeClick)
        }
    }
}

@Composable
fun RecipeItem(
    recipe: Recipe,
    onClick: (Int) -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(recipe.id) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = recipe.image,
                contentDescription = recipe.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 16.dp)
            )
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Eksik Malzemeler: ${recipe.missedIngredientCount}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
