package com.example.foodie.data

import com.example.foodie.api.SpoonacularService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeResultsRepository(
    private val service: SpoonacularService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun loadRecipes(
        query: String,
        apiKey: String,
        cuisine: String? = null,
        diet: String? = null,
        intolerances: String? = null,
        type: String? = null,
        includeIngredients: String? = null,
        excludeIngredients: String? = null,
    ): Result<List<Recipe>> =
        withContext(dispatcher) {
            try {
                val response = service.searchRecipes(query, apiKey, cuisine, diet, intolerances,
                    type, includeIngredients, excludeIngredients)
                if (response.isSuccessful) {
                    Result.success(response.body()?.results ?: listOf())
                } else {
                    Result.failure(Exception(response.errorBody()?.string()))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}