package com.example.hyperkicks.model

data class ShoeModel(
    val brand: String = "",
    val modelName: String = "",
    val releaseYear: Int = 0,
    val resellPrice: Int = 0,
    val imageUrl: String = "https://i.postimg.cc/bNJC21YC/xd.webp"
): java.io.Serializable
