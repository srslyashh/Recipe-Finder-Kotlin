package com.example.foodie.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        private var currentSimpleRecipe: Recipe? = null

        init {
            itemView.setOnClickListener {
                currentSimpleRecipe?.let(onClick)
            }
        }

        fun bind(simpleRecipe: Recipe) {
            currentSimpleRecipe = simpleRecipe
            nameTV.text = simpleRecipe.title
        }
    }
}