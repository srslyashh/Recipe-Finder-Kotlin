package com.example.foodie.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.foodie.R

class RecipeDetailFragment : Fragment(R.layout.recipe_detail_fragment) {
    private val args: RecipeDetailFragmentArgs by navArgs()

    private var isFavorited = false

    private val viewModel: FavoritedRecipesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        view.findViewById<TextView>(R.id.tv_recipe_title).text = args.recipe.title
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.activity_recipe_detail, menu)

        val favorite = menu.findItem(R.id.action_favorite)
        viewModel.getFavoritedRecipeByTitle(args.recipe.title).observe(viewLifecycleOwner) { favoritedRecipe ->
            when (favoritedRecipe) {
                null -> {
                    isFavorited = false
                    favorite?.isChecked = false
                    favorite?.icon = AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_action_favorite_off
                    )
                }
                else -> {
                    isFavorited = true
                    favorite?.isChecked = true
                    favorite?.icon = AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_action_favorite_on
                    )
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_share -> {
                shareRecipe()
                true
            }
            R.id.action_favorite -> {
                toggleFavoriteRecipe()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toggleFavoriteRecipe() {
        when (isFavorited) {
            false -> viewModel.addFavoritedRecipe(args.recipe)
            true ->  viewModel.removeFavoritedRecipe(args.recipe)
        }
    }

    private fun shareRecipe() {
            val text = getString(R.string.share_text, args.recipe.title)
            val intent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(intent, null))
        }
}