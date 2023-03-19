package com.example.foodie.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodie.data.AppDatabase
import com.example.foodie.data.FavoritedRecipesRepository
import com.example.foodie.data.Recipe
import kotlinx.coroutines.launch

class FavoritedRecipesViewModel(application: Application): AndroidViewModel(application) {
    private val repository = FavoritedRecipesRepository(
        AppDatabase.getInstance(application).recipeDao()
    )

    val favoritedRecipes = repository.getAllFavoritedRecipes().asLiveData()

    fun addFavoritedRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.insertFavoritedRecipe(recipe)
        }
    }

    fun removeFavoritedRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.deleteFavoritedRecipe(recipe)
        }
    }

    fun getFavoritedRecipeByTitle(title: String) = repository.getRecipeByTitle(title).asLiveData()
}