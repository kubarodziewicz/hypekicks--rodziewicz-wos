package com.example.hyperkicks

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.hyperkicks.databinding.ActivityDetailsBinding
import com.example.hyperkicks.databinding.ActivityMainBinding
import com.example.hyperkicks.model.ShoeModel
import kotlin.jvm.java

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val shoe = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {

            intent.getSerializableExtra("SHOE_DATA", ShoeModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("SHOE_DATA") as? ShoeModel
        }


        if(shoe != null) {
            binding.ModelTextView.text = shoe.modelName
            binding.brandTextView.text = "Marka: ${shoe.brand}"
            binding.priceTextView.text = "Cena: ${shoe.resellPrice}zł"
            binding.releaseYearTextView.text = "Rok produkcji: ${shoe.releaseYear}"

            Glide.with(this)
                .load(shoe.imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.detailImageView)

        } else {
            Toast.makeText(this, "Nie udało załadować się but :(", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}