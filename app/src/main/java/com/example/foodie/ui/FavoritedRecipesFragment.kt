package com.example.foodie.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie.R
import com.example.foodie.data.Recipe
import androidx.navigation.fragment.findNavController

class FavoritedRecipesFragment : Fragment(R.layout.favorited_recipes_fragment) {
    private val viewModel:FavoritedRecipesViewModel by viewModels()
    private val recipeListAdapter = RecipeListAdapter(::onRecipeClick)
    private lateinit var favoritedRecipeRV: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
         * Set up RecyclerView
         */
        favoritedRecipeRV = view.findViewById(R.id.rv_favorited_recipes)
        favoritedRecipeRV.layoutManager = LinearLayoutManager(requireContext())
        favoritedRecipeRV.setHasFixedSize(true)
        favoritedRecipeRV.adapter = recipeListAdapter

        viewModel.favoritedRecipes.observe(viewLifecycleOwner) {
            recipeListAdapter.updateSimpleRecipeList(it)
        }
    }

    private fun onRecipeClick(recipe: Recipe) {
        val directions = FavoritedRecipesFragmentDirections.navigateToRecipeDetail(recipe)
        findNavController().navigate(directions)
    }
}