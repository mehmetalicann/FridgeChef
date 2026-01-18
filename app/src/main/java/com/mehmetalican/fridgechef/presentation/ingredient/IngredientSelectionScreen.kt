package com.mehmetalican.fridgechef.presentation.ingredient

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mehmetalican.fridgechef.domain.model.Ingredient
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon

@Composable
fun IngredientSelectionScreen(
    viewModel: IngredientViewModel = hiltViewModel(),
    onSearchClicked: () -> Unit
) {
    val ingredients by viewModel.ingredients.collectAsState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onSearchClicked,
                icon = { Icon(Icons.Filled.Search, contentDescription = "Ara") },
                text = { Text("Tarif Bul") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp + innerPadding.calculateTopPadding(),
                bottom = 16.dp + innerPadding.calculateBottomPadding() + 80.dp // Add extra padding for FAB
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(ingredients) { ingredient ->
                IngredientCard(
                    state = ingredient,
                    onClick = { viewModel.toggleIngredientSelection(ingredient.id) }
                )
            }
        }
    }
}

@Composable
fun IngredientCard(
    state: IngredientViewModel.IngredientUiState,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (state.isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        label = "selectionColor"
    )
    
    val contentColor by animateColorAsState(
        targetValue = if (state.isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "contentColor"
    )

    val borderColor by animateColorAsState(
        targetValue = if (state.isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        label = "borderColor"
    )

    Card(
        modifier = Modifier
            .size(120.dp) // Fixed size as requested implicitly by grid/design feel, or fillMaxWidth
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = androidx.compose.foundation.BorderStroke(2.dp, borderColor)
    ) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon logic: Use resource if available, else default vector
            if (state.iconRes != null) {
                Icon(
                    painter = androidx.compose.ui.res.painterResource(id = state.iconRes),
                    contentDescription = state.name,
                    modifier = Modifier.size(48.dp),
                    tint = contentColor
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Home, // Fallback to standard icon
                    contentDescription = state.name,
                    modifier = Modifier.size(48.dp),
                    tint = contentColor
                )
            }
            
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = state.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = contentColor,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
