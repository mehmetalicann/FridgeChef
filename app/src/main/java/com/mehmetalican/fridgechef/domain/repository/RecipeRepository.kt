package com.mehmetalican.fridgechef.domain.repository

import com.mehmetalican.fridgechef.common.Resource
import com.mehmetalican.fridgechef.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun searchRecipes(ingredients: String): Flow<Resource<List<Recipe>>>
    suspend fun getRecipeDetail(id: Int): Flow<Resource<com.mehmetalican.fridgechef.domain.model.RecipeDetail>>
    fun getFavorites(): Flow<List<Recipe>>
    suspend fun toggleFavorite(recipe: Recipe)
    fun isFavorite(id: Int): Flow<Boolean>
}
