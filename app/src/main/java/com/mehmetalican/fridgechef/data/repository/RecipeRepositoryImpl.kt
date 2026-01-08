package com.mehmetalican.fridgechef.data.repository

import com.mehmetalican.fridgechef.common.Resource
import com.mehmetalican.fridgechef.data.mapper.toRecipe
import com.mehmetalican.fridgechef.data.mapper.toRecipeDetail
import com.mehmetalican.fridgechef.data.remote.SpoonacularApi
import com.mehmetalican.fridgechef.domain.model.Recipe
import com.mehmetalican.fridgechef.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val api: SpoonacularApi,
    private val db: com.mehmetalican.fridgechef.data.local.FridgeChefDatabase
) : RecipeRepository {

    private val dao = db.recipeDao()

    override suspend fun searchRecipes(ingredients: String): Flow<Resource<List<Recipe>>> = flow {
        emit(Resource.Loading())
        try {
            val remoteRecipes = api.searchRecipesByIngredients(ingredients)
            val recipes = remoteRecipes.map { it.toRecipe() }
            emit(Resource.Success(recipes))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Beklenmedik bir hata oluştu"))
        } catch (e: IOException) {
            emit(Resource.Error("Sunucuya ulaşılamıyor. İnternet bağlantınızı kontrol edin."))
        }
    }

    override suspend fun getRecipeDetail(id: Int): Flow<Resource<com.mehmetalican.fridgechef.domain.model.RecipeDetail>> = flow {
        emit(Resource.Loading())
        try {
            val remoteDetail = api.getRecipeInformation(id)
            emit(Resource.Success(remoteDetail.toRecipeDetail()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Beklenmedik bir hata oluştu"))
        } catch (e: IOException) {
            emit(Resource.Error("Sunucuya ulaşılamıyor. İnternet bağlantınızı kontrol edin."))
        }
    }

    override fun getFavorites(): Flow<List<Recipe>> {
        return dao.getAllFavorites().map { entities ->
            entities.map { entity ->
                Recipe(
                    id = entity.id,
                    title = entity.title,
                    image = entity.image,
                    missedIngredientCount = entity.missedIngredientCount,
                    usedIngredientCount = entity.usedIngredientCount,
                    likes = 0 // Not stored locally
                )
            }
        }
    }

    override suspend fun toggleFavorite(recipe: Recipe) {
        val existing = dao.getFavoriteById(recipe.id)
        if (existing == null) {
            dao.insertFavorite(
                com.mehmetalican.fridgechef.data.local.FavoriteRecipeEntity(
                    id = recipe.id,
                    title = recipe.title,
                    image = recipe.image,
                    missedIngredientCount = recipe.missedIngredientCount,
                    usedIngredientCount = recipe.usedIngredientCount
                )
            )
        } else {
            dao.deleteFavorite(existing)
        }
    }

    override fun isFavorite(id: Int): Flow<Boolean> {
        return dao.isFavorite(id)
    }
}
