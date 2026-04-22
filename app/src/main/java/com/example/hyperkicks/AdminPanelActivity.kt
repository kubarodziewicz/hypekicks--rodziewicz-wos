package com.example.hyperkicks

import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hyperkicks.databinding.ActivityAdminPanelBinding
import com.example.hyperkicks.model.ShoeModel
import com.google.firebase.firestore.FirebaseFirestore

class AdminPanelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminPanelBinding
    private val db = FirebaseFirestore.getInstance()

    private val adminShoeList = mutableListOf<ShoeModel>()
    private val shoeNames = mutableListOf<String>()
    private var editingShoeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListView()
        fetchDataForAdmin()

        binding.btnAddShoe.setOnClickListener {
            handleFormSubmit()
        }

        binding.adminListView.setOnItemClickListener { _, _, position, _ ->
            loadShoeIntoForm(position)
        }

        binding.adminListView.setOnItemLongClickListener { _, _, position, _ ->
            val shoeToDelete = adminShoeList[position]
            deleteShoe(shoeToDelete.id)
            true
        }
    }

    private fun setupListView() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, shoeNames)
        binding.adminListView.adapter = adapter
    }

    private fun fetchDataForAdmin() {
        db.collection("sneakers").addSnapshotListener { snapshots, _ ->
            if (snapshots != null) {
                adminShoeList.clear()
                shoeNames.clear()
                for (doc in snapshots) {
                    val shoe = doc.toObject(ShoeModel::class.java).copy(id = doc.id)
                    adminShoeList.add(shoe)
                    shoeNames.add("${shoe.brand} ${shoe.modelName} - ${shoe.resellPrice} PLN")
                }
                (binding.adminListView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
            }
        }
    }

    private fun loadShoeIntoForm(position: Int) {
        val shoe = adminShoeList[position]

        binding.etBrand.setText(shoe.brand)
        binding.etModel.setText(shoe.modelName)
        binding.etYear.setText(shoe.releaseYear.toString())
        binding.etPrice.setText(shoe.resellPrice.toString())
        binding.etImageUrl.setText(shoe.imageUrl)

        editingShoeId = shoe.id
        binding.btnAddShoe.text = "ZAKTUALIZUJ DANE"
        binding.btnAddShoe.setBackgroundColor(Color.BLUE)
        Toast.makeText(this, "Tryb edycji: ${shoe.modelName}", Toast.LENGTH_SHORT).show()
    }

    private fun handleFormSubmit() {
        val brand = binding.etBrand.text.toString().trim()
        val model = binding.etModel.text.toString().trim()
        val year = binding.etYear.text.toString().toIntOrNull() ?: 0
        val price = binding.etPrice.text.toString().toIntOrNull() ?: 0
        val url = binding.etImageUrl.text.toString().trim()

        if (brand.isEmpty() || model.isEmpty()) {
            Toast.makeText(this, "Wypełnij przynajmniej markę i model!", Toast.LENGTH_SHORT).show()
            return
        }

        val shoeData = mapOf(
            "brand" to brand,
            "modelName" to model,
            "releaseYear" to year,
            "resellPrice" to price,
            "imageUrl" to url
        )

        if (editingShoeId == null) {
            db.collection("sneakers").add(shoeData).addOnSuccessListener {
                Toast.makeText(this, "Dodano nowy but!", Toast.LENGTH_SHORT).show()
                resetForm()
            }
        } else {
            db.collection("sneakers").document(editingShoeId!!).update(shoeData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Zaktualizowano pomyślnie!", Toast.LENGTH_SHORT).show()
                    resetForm()
                }
        }
    }

    private fun deleteShoe(id: String) {
        if (id.isEmpty()) return
        db.collection("sneakers").document(id).delete().addOnSuccessListener {
            Toast.makeText(this, "Usunięto z bazy!", Toast.LENGTH_SHORT).show()
            if (id == editingShoeId) resetForm()
        }
    }

    private fun resetForm() {
        binding.etBrand.text.clear()
        binding.etModel.text.clear()
        binding.etYear.text.clear()
        binding.etPrice.text.clear()
        binding.etImageUrl.text.clear()

        editingShoeId = null
        binding.btnAddShoe.text = "DODAJ DO BAZY"
        binding.btnAddShoe.setBackgroundColor(Color.BLACK)
    }
}