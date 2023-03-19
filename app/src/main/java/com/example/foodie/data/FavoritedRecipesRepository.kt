package com.example.foodie.data

class FavoritedRecipesRepository(
    private val dao: RecipeDao
) {
    suspend fun insertFavoritedRecipe(recipe: Recipe) = dao.insert(recipe)
    suspend fun deleteFavoritedRecipe(recipe: Recipe) = dao.delete(recipe)
    fun getAllFavoritedRecipes() = dao.getAllRecipes()
    fun getRecipeByTitle(title: String) = dao.getRecipeByTitle(title)
}