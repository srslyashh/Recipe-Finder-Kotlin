package com.example.foodie.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie.R
import com.example.foodie.data.Recipe
import androidx.navigation.fragment.findNavController

class FavoritedRecipesActivity : AppCompatActivity() {
    private val viewModel:FavoritedRecipesViewModel by viewModels()
    private val recipeListAdapter = RecipeListAdapter(::onRecipeClick)
    private lateinit var favoritedRecipeRV: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorited_recipes)

        /*
         * Set up RecyclerView
         */
        favoritedRecipeRV = findViewById(R.id.rv_favorited_recipes)
        favoritedRecipeRV.layoutManager = LinearLayoutManager(this)
        favoritedRecipeRV.setHasFixedSize(true)
        favoritedRecipeRV.adapter = recipeListAdapter

        viewModel.favoritedRecipes.observe(this) {
            recipeListAdapter.updateSimpleRecipeList(it)
        }
    }

    private fun onRecipeClick(recipe: Recipe) {
       val intent = Intent(this, RecipeDetailActivity::class.java).apply{
           putExtra(EXTRA_FAVORITE_RECIPE, recipe)
       }
       startActivity(intent)
    }
}