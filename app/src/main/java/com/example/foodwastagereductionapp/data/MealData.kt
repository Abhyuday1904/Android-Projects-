package com.example.foodwastagereductionapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Meal(
    val date: String,
    val day: String,
    val meals: Meals
)

@Serializable
data class Meals(
    val breakfast: String,
    val lunch: String,
    val snacks: String,
    val dinner: String
)



