package com.mehmetalican.fridgechef.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FridgeChefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)
    
    @Query("SELECT * FROM recipes")
    suspend fun getRecipes(): List<RecipeEntity>
}
