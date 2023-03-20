package com.example.foodie.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodie.R
import com.example.foodie.data.Recipe

class RecipeListAdapter(
    private val onSimpleRecipeClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeListAdapter.SimpleRecipeViewHolder>() {
    private var simpleRecipeList = listOf<Recipe>()

    fun updateSimpleRecipeList(newSimpleRecipeList: List<Recipe>?) {
        simpleRecipeList = newSimpleRecipeList ?: listOf()
        notifyDataSetChanged()
    }

    override fun getItemCount() = simpleRecipeList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleRecipeViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_list_item, parent, false)
        return SimpleRecipeViewHolder(itemView, onSimpleRecipeClick)
    }

    override fun onBindViewHolder(holder: SimpleRecipeViewHolder, position: Int) {
        holder.bind(simpleRecipeList[position])
    }

    class SimpleRecipeViewHolder(
        itemView: View,
        private val onClick: (Recipe) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val nameTV: TextView = itemView.findViewById(R.id.tv_recipe_title)
        private val imageIV : ImageView = itemView.findViewById(R.id.iv_recipe_image)
        private val timeTV : TextView = itemView.findViewById(R.id.tv_recipe_time)
        private val servingTV: TextView = itemView.findViewById(R.id.tv_recipe_serving)
        private var currentSimpleRecipe: Recipe? = null


        init {
            itemView.setOnClickListener {
                currentSimpleRecipe?.let(onClick)
            }
        }

        fun wordServing(word: String) : String
        {
            val returnVal = when(word)
            {
                "1" -> " person"
                else -> " people"
            }

            return returnVal
        }

        fun bind(simpleRecipe: Recipe) {
            val ctx = itemView.context

            Glide.with(ctx)
                .load(simpleRecipe.image)
                .into(imageIV)

            currentSimpleRecipe = simpleRecipe

            nameTV.text = simpleRecipe.title
            timeTV.text = simpleRecipe.readyInMinutes.toString() + " mins"
            servingTV.text = simpleRecipe.servings.toString() + wordServing(simpleRecipe.servings.toString())
        }
    }
}