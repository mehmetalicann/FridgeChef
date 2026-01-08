package com.mehmetalican.fridgechef.domain.model

data class Ingredient(
    val id: Int,
    val name: String,
    val imageUrl: String = "" // Optional for now
)
