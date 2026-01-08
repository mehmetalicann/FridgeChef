package com.mehmetalican.fridgechef.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_recipes")
data class FavoriteRecipeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val image: String,
    val missedIngredientCount: Int = 0,
    val usedIngredientCount: Int = 0
)
