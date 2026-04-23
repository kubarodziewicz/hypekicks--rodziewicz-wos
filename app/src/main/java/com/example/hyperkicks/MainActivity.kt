package com.example.hyperkicks

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hyperkicks.databinding.ActivityMainBinding
import com.example.hyperkicks.model.ShoeModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var shoeList: MutableList<ShoeModel>
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.adminTextView.setOnClickListener {
            val intent = Intent(this, AdminPanelActivity::class.java)
            startActivity(intent)
        }

        //seedDatabase()

        shoeList = mutableListOf()

        fetchDataFromDatabase()
    }

    private fun fetchDataFromDatabase() {
        db.collection("sneakers")
            .get()
            .addOnSuccessListener { documents ->
                shoeList.clear()
                for (doc in documents) {
                    val brand = doc.getString("brand") ?: "adidas"
                    val modelName = doc.getString("modelName") ?: "Samba Vegan White Black"
                    val releaseYear = doc.getLong("releaseYear")?.toInt() ?: 6767
                    val resellPrice = doc.getLong("resellPrice")?.toInt() ?: 420
                    val imageUrl = doc.getString("imageUrl") ?: "https://i.postimg.cc/bNJC21YC/xd.webp"

                    shoeList.add(ShoeModel(brand, modelName, releaseYear, resellPrice, imageUrl))
                }
                //adapter.notifyDataSetChanged()
                Toast.makeText(this, "Załadowano ${shoeList.size} butów!!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { exception ->
                Log.e("FIREBASE_ERROR", "Błąd pobierania danych: ", exception)
                Toast.makeText(this, "Błąd pobierania danych z chmury!", Toast.LENGTH_LONG).show()
            }
    }


    private fun seedDatabase() {
        val shoeList = listOf(
            ShoeModel(
                brand = "adidas",
                modelName = "Campus 00s Grey White",
                releaseYear = 2022,
                resellPrice = 450,
                imageUrl = "https://i.postimg.cc/HsxZ7bWy/adidas-Campus-00s.webp"
            ),
            ShoeModel(
                brand = "adidas",
                modelName = "Handball Spezial Light Blue",
                releaseYear = 1979,
                resellPrice = 480,
                imageUrl = "https://i.postimg.cc/hPjy7V4V/adidas-Handball-Spezial.webp"
            ),
            ShoeModel(
                brand = "adidas",
                modelName = "Superstar II White Black",
                releaseYear = 1969,
                resellPrice = 400,
                imageUrl = "https://i.postimg.cc/Wb3WJ0pM/adidas-Superstar-II.webp"
            ),
            ShoeModel(
                brand = "adidas",
                modelName = "XLG Runner Deluxe Gray",
                releaseYear = 2023,
                resellPrice = 520,
                imageUrl = "https://i.postimg.cc/ZKR7y6Yp/adidas-XLG-Runner-Deluxe-Gray.webp"
            ),
            ShoeModel(
                brand = "BAPE",
                modelName = "Sk8 Sta Low Black White",
                releaseYear = 2021,
                resellPrice = 1200,
                imageUrl = "https://i.postimg.cc/rFmnt5yG/BAPE-Sk8-Sta-Low.webp"
            ),
            ShoeModel(
                brand = "Nike",
                modelName = "Air Jordan 4 Retro Military Black",
                releaseYear = 2022,
                resellPrice = 2100,
                imageUrl = "https://i.postimg.cc/m2DXF7ZN/but67.webp"
            ),
            ShoeModel(
                brand = "New Balance",
                modelName = "530 White Silver Navy",
                releaseYear = 1992,
                resellPrice = 430,
                imageUrl = "https://i.postimg.cc/tCTSVFRN/New-Balance-530.webp"
            ),
            ShoeModel(
                brand = "Nike",
                modelName = "Air Force 1 LE Triple White",
                releaseYear = 1982,
                resellPrice = 450,
                imageUrl = "https://i.postimg.cc/DyZjb1f6/Nike-Air-Force-1-LE.webp"
            ),
            ShoeModel(
                brand = "Nike",
                modelName = "Shox TL White Metallic Silver",
                releaseYear = 2019,
                resellPrice = 950,
                imageUrl = "https://i.postimg.cc/jd5gJyqX/Nike-Shox-TL-AR3566-100.jpg"
            ),
            ShoeModel(
                brand = "Vans",
                modelName = "Knu Skool Black White",
                releaseYear = 1998,
                resellPrice = 350,
                imageUrl = "https://i.postimg.cc/vHBq658P/Vans-Knu-Skool.webp"
            )
        )

        val db = Firebase.firestore

        for (shoe in shoeList) {
            db.collection("sneakers")
                .add(shoe)
                .addOnSuccessListener {
                    Log.d("jajo", "Dodano but! ${shoe.modelName}")
                }
                .addOnFailureListener {
                    Log.e("jajko", "Nie dodano but :(")
                }
        }

    }
}