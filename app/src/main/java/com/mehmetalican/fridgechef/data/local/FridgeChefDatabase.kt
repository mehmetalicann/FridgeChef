package com.mehmetalican.fridgechef.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RecipeEntity::class, FavoriteRecipeEntity::class], version = 2)
abstract class FridgeChefDatabase : RoomDatabase() {
    abstract fun dao(): FridgeChefDao
    abstract fun recipeDao(): RecipeDao
}
