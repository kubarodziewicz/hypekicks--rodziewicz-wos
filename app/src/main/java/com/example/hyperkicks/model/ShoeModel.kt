package com.example.hyperkicks.model

data class ShoeModel(
    val brand: String = "adidas",
    val modelName: String = "Samba Vegan White Black",
    val releaseYear: Int = 6767,
    val resellPrice: Int = 420,
    val imageUrl: String = "https://i.postimg.cc/bNJC21YC/xd.webp"
): java.io.Serializable
