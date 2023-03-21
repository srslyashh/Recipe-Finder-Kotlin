package com.example.foodie.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Ingredient(
    val name: String,

    val amount: Float,
    val unit: String,
) : java.io.Serializable
