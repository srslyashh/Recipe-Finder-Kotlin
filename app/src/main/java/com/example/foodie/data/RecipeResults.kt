package com.example.foodie.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeResults(
    val results: List<Recipe>
)