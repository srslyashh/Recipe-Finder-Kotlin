package com.example.foodie.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.foodie.R
import com.example.foodie.data.Recipe

const val EXTRA_FAVORITE_RECIPE = "FAVORITE_RECIPE"
class RecipeDetailActivity : AppCompatActivity() {
    private var isFavorited = false
    private var recipes: Recipe? = null
    private val viewModel: FavoritedRecipesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_detail)

        if (intent != null && intent.hasExtra(EXTRA_FAVORITE_RECIPE)) {
            recipes = intent.getSerializableExtra(EXTRA_FAVORITE_RECIPE) as Recipe

            findViewById<TextView>(R.id.tv_recipe_title).text = recipes!!.title
        }
    }

    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
        menuInflater.inflate(R.menu.activity_favorite_detail, menu)

        val favorite = menu.findItem(R.id.action_favorite)
        viewModel.getFavoritedRecipeByTitle(recipes!!.title).observe(this) { favoritedRecipe ->
            when (favoritedRecipe) {
                null -> {
                    isFavorited = false
                    favorite?.isChecked = false
                    favorite?.icon = AppCompatResources.getDrawable(
                        this,
                        R.drawable.ic_action_favorite_off
                    )
                }
                else -> {
                    isFavorited = true
                    favorite?.isChecked = true
                    favorite?.icon = AppCompatResources.getDrawable(
                        this,
                        R.drawable.ic_action_favorite_on
                    )
                }
            }
        }
        return true
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
            false -> viewModel.addFavoritedRecipe(recipes!!)
            true ->  viewModel.removeFavoritedRecipe(recipes!!)
        }
    }

    private fun shareRecipe() {
            val text = getString(R.string.share_text, recipes!!.title)
            val intent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(intent, null))
        }
}