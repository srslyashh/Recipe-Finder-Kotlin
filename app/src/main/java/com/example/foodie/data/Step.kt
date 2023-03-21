package com.example.foodie.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Instruction(
    val steps: List<Step>,
) : java.io.Serializable

@JsonClass(generateAdapter = true)
data class Step(
    val number: Int,
    val step: String,
) : java.io.Serializable
