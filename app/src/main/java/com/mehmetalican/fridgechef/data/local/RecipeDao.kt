package com.mehmetalican.fridgechef.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(recipe: FavoriteRecipeEntity)

    @Delete
    suspend fun deleteFavorite(recipe: FavoriteRecipeEntity)

    @Query("SELECT * FROM favorite_recipes")
    fun getAllFavorites(): Flow<List<FavoriteRecipeEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_recipes WHERE id = :id)")
    fun isFavorite(id: Int): Flow<Boolean>
    
    @Query("SELECT * FROM favorite_recipes WHERE id = :id LIMIT 1")
    suspend fun getFavoriteById(id: Int): FavoriteRecipeEntity?
}
