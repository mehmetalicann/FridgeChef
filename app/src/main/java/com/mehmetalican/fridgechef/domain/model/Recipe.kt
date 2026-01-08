package com.mehmetalican.fridgechef.domain.model

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val usedIngredientCount: Int,
    val missedIngredientCount: Int,
    val likes: Int
)
