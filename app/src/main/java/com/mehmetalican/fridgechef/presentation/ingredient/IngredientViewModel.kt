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
    private val repository: RecipeRepository,
    private val translationManager: com.mehmetalican.fridgechef.domain.manager.TranslationManager
) : ViewModel() {

    data class IngredientUiState(
        val id: String,
        val name: String,
        @androidx.annotation.DrawableRes val iconRes: Int? = null,
        val isSelected: Boolean = false
    )

    private val _ingredients = MutableStateFlow<List<IngredientUiState>>(emptyList())
    val ingredients: StateFlow<List<IngredientUiState>> = _ingredients.asStateFlow()

    init {
        // Mock data
        _ingredients.value = listOf(
            IngredientUiState("1", "Yumurta"),
            IngredientUiState("2", "Süt"),
            IngredientUiState("3", "Tavuk"),
            IngredientUiState("4", "Peynir"),
            IngredientUiState("5", "Domates"),
            IngredientUiState("6", "Soğan"),
            IngredientUiState("7", "Patates"),
            IngredientUiState("8", "Pirinç"),
            IngredientUiState("9", "Makarna"),
            IngredientUiState("10", "Tereyağı")
        )
    }

    fun toggleIngredientSelection(id: String) {
        _ingredients.update { currentList ->
            currentList.map { item ->
                if (item.id == id) {
                    item.copy(isSelected = !item.isSelected)
                } else {
                    item
                }
            }
        }
    }

    private val _recipeSearchState = MutableStateFlow<Resource<List<Recipe>>>(Resource.Loading(null))
    val recipeSearchState: StateFlow<Resource<List<Recipe>>> = _recipeSearchState.asStateFlow()

    private val ingredientTranslation = mapOf(
        "Yumurta" to "eggs",
        "Süt" to "milk",
        "Tavuk" to "chicken",
        "Peynir" to "cheese",
        "Domates" to "tomato",
        "Soğan" to "onion",
        "Patates" to "potato",
        "Pirinç" to "rice",
        "Makarna" to "pasta",
        "Tereyağı" to "butter"
    )

    fun searchRecipes() {
        val selectedNames = _ingredients.value
            .filter { it.isSelected }
            .joinToString(separator = ",") {
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
                if (result is Resource.Success && result.data != null) {
                    val detail = result.data
                    // Translate instructions
                     val translatedInstructions = translationManager.translate(detail.instructions)
                    
                    // Translate steps (can be many, so simpler loop or join-split might be better to save calls, but loop is safer)
                    // Optimisation: Join steps with specific delimiter, translate once, split back.
                    // But for safety let's do one big block or item by item.
                    // Just 2 calls: Instructions and Title/Summary if needed.
                    val translatedSteps = detail.steps.map { step ->
                        translationManager.translate(step)
                    }

                    _recipeDetailState.value = Resource.Success(
                        detail.copy(
                            instructions = translatedInstructions,
                            steps = translatedSteps
                        )
                    )
                } else {
                    _recipeDetailState.value = result
                }
            }
        }
    }

    val favoriteRecipes: StateFlow<List<Recipe>> = repository.getFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleFavorite(detail: com.mehmetalican.fridgechef.domain.model.RecipeDetail) {
        viewModelScope.launch {
            val recipe = Recipe(
                id = detail.id,
                title = detail.title,
                image = detail.image,
                usedIngredientCount = 0,
                missedIngredientCount = 0,
                likes = 0
            )
            repository.toggleFavorite(recipe)
        }
    }

    fun isFavorite(id: Int): StateFlow<Boolean> {
         return repository.isFavorite(id)
             .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    }
}
