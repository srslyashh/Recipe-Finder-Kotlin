package com.example.foodie.ui

import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.foodie.R
import com.example.foodie.data.Recipe
import java.io.File
import java.io.FileOutputStream


const val EXTRA_FAVORITE_RECIPE = "FAVORITE_RECIPE"

class RecipeDetailActivity : AppCompatActivity() {
    private var isFavorited = false
    private var recipes: Recipe? = null
    private val viewModel: FavoritedRecipesViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)     //reference to detail

        if (intent != null && intent.hasExtra(EXTRA_FAVORITE_RECIPE)) {
            recipes = intent.getSerializableExtra(EXTRA_FAVORITE_RECIPE) as Recipe

            findViewById<TextView>(R.id.tv_recipe_title).text = recipes!!.title

            // Testing, Load the image using Glide
            Glide.with(this)
                .load(recipes!!.image)
                .into(findViewById<ImageView>(R.id.iv_recipe_image))

            findViewById<TextView>(R.id.tv_recipe_serving).text = recipes!!.servings.toString()


            findViewById<TextView>(R.id.tv_recipe_time).text = recipes!!.readyInMinutes.toString()


        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_favorite_detail, menu)

        val favorite = menu?.findItem(R.id.action_favorite)
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
        return when (item.itemId) {
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
            true -> viewModel.removeFavoritedRecipe(recipes!!)
        }
    }

    private fun shareRecipe() {
        val uri = generatePDF()
        this.grantUriPermission("android", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "application/pdf"
        }
        startActivity(Intent.createChooser(intent, null))
    }

    private fun generatePDF(): Uri {
        val document = PdfDocument()
        val pageInfo = PageInfo.Builder(794, 1123, 1).create()
        val page = document.startPage(pageInfo)

        val textPaint = Paint()
        textPaint.typeface = Typeface.DEFAULT
        textPaint.textSize = 34F

        val canvas = page.canvas
        val title = recipes!!.title
        var yPos = 100F
        var xPos = 100F

        canvas.drawText("Foodie Recipe: $title", xPos, yPos, textPaint)
        yPos += 50

        textPaint.textSize = 20F

        textPaint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText("Ingredients:",xPos, yPos, textPaint)
        textPaint.typeface = Typeface.DEFAULT
        yPos += 30
        for (ingredient in recipes!!.ingredients) {
            canvas.drawText("- ${ingredient.name}", xPos, yPos, textPaint)
            yPos += 30
        }

        yPos += 20
        textPaint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText("Steps:", xPos, yPos, textPaint)
        textPaint.typeface = Typeface.DEFAULT
        yPos += 30
        for (step in recipes!!.instructionSets[0].steps) {
            canvas.drawText("${step.number}. ${step.step}", xPos, yPos, textPaint)
            yPos += 30
        }
        document.finishPage(page)
        val file = File(applicationContext.filesDir.path, "recipe.pdf")
        document.writeTo(FileOutputStream(file))
        val uri = FileProvider.getUriForFile(
            this,
            "com.example.foodie.provider",
            file
        )
        document.close();
        return uri
    }
}