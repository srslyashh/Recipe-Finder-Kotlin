package com.example.foodie.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity
data class Recipe(
    @PrimaryKey
    val id: Int,

    val title: String,

    val image: String,

    val imageType: String,
) : java.io.Serializable
