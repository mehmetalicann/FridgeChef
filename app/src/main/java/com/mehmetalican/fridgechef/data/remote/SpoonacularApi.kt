package com.mehmetalican.fridgechef.data.remote

import com.mehmetalican.fridgechef.data.remote.dto.RecipeDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SpoonacularApi {

    @GET("recipes/findByIngredients")
    suspend fun searchRecipesByIngredients(
        @Query("ingredients") ingredients: String,
        @Query("number") number: Int = 10
    ): List<RecipeDto>

    @GET("recipes/{id}/information")
    suspend fun getRecipeInformation(
        @retrofit2.http.Path("id") id: Int
    ): com.mehmetalican.fridgechef.data.remote.dto.detail.RecipeDetailDto

    companion object {
        const val BASE_URL = "https://api.spoonacular.com/"
    }
}
