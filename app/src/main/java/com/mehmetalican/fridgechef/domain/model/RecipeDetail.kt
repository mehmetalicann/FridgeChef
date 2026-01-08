package com.mehmetalican.fridgechef.domain.model

data class RecipeDetail(
    val id: Int,
    val title: String,
    val image: String,
    val instructions: String,
    val steps: List<String>
)
