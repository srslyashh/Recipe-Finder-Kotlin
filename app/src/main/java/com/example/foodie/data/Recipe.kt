package com.example.foodie.data

import androidx.room.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
@Entity
data class Recipe(
    @PrimaryKey
    val id: Int,

    val title: String,
    val image: String,
    val imageType: String,

    val readyInMinutes: Int,
    val servings: Int,

    @Json(name = "missedIngredients")
    val ingredients: List<Ingredient>,

    @Json(name = "analyzedInstructions")
    val instructionSets: List<Instruction>,
) : java.io.Serializable



