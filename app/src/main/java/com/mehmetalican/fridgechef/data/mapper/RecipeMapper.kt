package com.mehmetalican.fridgechef.data.mapper

import com.mehmetalican.fridgechef.data.remote.dto.RecipeDto
import com.mehmetalican.fridgechef.domain.model.Recipe

fun RecipeDto.toRecipe(): Recipe {
    return Recipe(
        id = id,
        title = title,
        image = image,
        usedIngredientCount = usedIngredientCount,
        missedIngredientCount = missedIngredientCount,
        likes = likes
    )
}

fun com.mehmetalican.fridgechef.data.remote.dto.detail.RecipeDetailDto.toRecipeDetail(): com.mehmetalican.fridgechef.domain.model.RecipeDetail {
    val allSteps = analyzedInstructions.flatMap { it.steps }.map { it.step }
    // If analyzed instructions are empty, try to split raw instructions or just use raw instructions as one step
    val finalSteps = if (allSteps.isNotEmpty()) {
        allSteps
    } else {
        // Simple fallback splitting by period, filter empty
        instructions?.split(".")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList()
    }

    return com.mehmetalican.fridgechef.domain.model.RecipeDetail(
        id = id,
        title = title,
        image = image,
        instructions = instructions ?: "Tarif detayı bulunamadı.",
        steps = finalSteps
    )
}
