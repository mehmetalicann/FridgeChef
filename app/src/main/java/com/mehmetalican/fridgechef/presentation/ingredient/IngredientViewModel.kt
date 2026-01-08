package com.mehmetalican.fridgechef.presentation.ingredient

import androidx.lifecycle.ViewModel
import com.mehmetalican.fridgechef.domain.model.Ingredient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

import androidx.lifecycle.viewModelScope
import com.mehmetalican.fridgechef.common.Resource
import com.mehmetalican.fridgechef.domain.model.Recipe
import com.mehmetalican.fridgechef.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

@HiltViewModel
class IngredientViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _ingredients = MutableStateFlow<List<Ingredient>>(emptyList())
    val ingredients: StateFlow<List<Ingredient>> = _ingredients.asStateFlow()

    private val _selectedIngredients = MutableStateFlow<Set<Ingredient>>(emptySet())
    val selectedIngredients: StateFlow<Set<Ingredient>> = _selectedIngredients.asStateFlow()

    init {
        // Mock data
        _ingredients.value = listOf(
            Ingredient(1, "Yumurta"),
            Ingredient(2, "Süt"),
            Ingredient(3, "Tavuk"),
            Ingredient(4, "Peynir"),
            Ingredient(5, "Domates"),
            Ingredient(6, "Soğan"),
            Ingredient(7, "Patates"),
            Ingredient(8, "Prinç"),
            Ingredient(9, "Makarna"),
            Ingredient(10, "Tereyağı")
        )
    }

    fun toggleIngredientSelection(ingredient: Ingredient) {
        _selectedIngredients.update { currentSelection ->
            if (currentSelection.contains(ingredient)) {
                currentSelection - ingredient
            } else {
                currentSelection + ingredient
            }
        }
    }

    private val _recipeSearchState = MutableStateFlow<Resource<List<Recipe>>>(Resource.Loading(null)) // Start with null or initial state
    val recipeSearchState: StateFlow<Resource<List<Recipe>>> = _recipeSearchState.asStateFlow()

    private val ingredientTranslation = mapOf(
        "Yumurta" to "eggs",
        "Süt" to "milk",
        "Tavuk" to "chicken",
        "Peynir" to "cheese",
        "Domates" to "tomato",
        "Soğan" to "onion",
        "Patates" to "potato",
        "Prinç" to "rice",
        "Makarna" to "pasta",
        "Tereyağı" to "butter"
    )

    fun searchRecipes() {
        val selectedNames = _selectedIngredients.value.joinToString(separator = ",") { 
            ingredientTranslation[it.name] ?: it.name 
        }
        if (selectedNames.isBlank()) return

        viewModelScope.launch {
            repository.searchRecipes(selectedNames).collect { result ->
                _recipeSearchState.value = result
            }
        }
    }

    private val _recipeDetailState = MutableStateFlow<Resource<com.mehmetalican.fridgechef.domain.model.RecipeDetail>>(Resource.Loading(null))
    val recipeDetailState: StateFlow<Resource<com.mehmetalican.fridgechef.domain.model.RecipeDetail>> = _recipeDetailState.asStateFlow()

    fun getRecipeDetails(id: Int) {
        viewModelScope.launch {
            repository.getRecipeDetail(id).collect { result ->
                _recipeDetailState.value = result
            }
        }
    }

    val favoriteRecipes: StateFlow<List<Recipe>> = repository.getFavorites()
        .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleFavorite(detail: com.mehmetalican.fridgechef.domain.model.RecipeDetail) {
        viewModelScope.launch {
            // Reconstruct Recipe object from Detail for saving
            val recipe = Recipe(
                id = detail.id,
                title = detail.title,
                image = detail.image,
                usedIngredientCount = 0,
                missedIngredientCount = 0, // Details endpoint doesn't return these counts directly corresponding to search, default 0
                likes = 0
            )
            repository.toggleFavorite(recipe)
        }
    }

    fun isFavorite(id: Int): StateFlow<Boolean> {
         return repository.isFavorite(id)
             .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), false)
    }
}
