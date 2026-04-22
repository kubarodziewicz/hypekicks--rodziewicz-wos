package com.example.hyperkicks

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListView()
        fetchDataForAdmin()

        binding.btnAddShoe.setOnClickListener {
            addShoeToFirestore()
        }

        binding.adminListView.setOnItemLongClickListener { _, _, position, _ ->
            val shoeToDelete = adminShoeList[position]
            deleteShoe(shoeToDelete.id, position)
            true
        }
    }

    private fun setupListView() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, shoeNames)
        binding.adminListView.adapter = adapter
    }

    private fun fetchDataForAdmin() {
        db.collection("sneakers").addSnapshotListener { snapshots, _ ->
            adminShoeList.clear()
            shoeNames.clear()
            snapshots?.forEach { doc ->
                val shoe = doc.toObject(ShoeModel::class.java).copy(id = doc.id)
                adminShoeList.add(shoe)
                shoeNames.add("${shoe.brand} ${shoe.modelName}")
            }
            (binding.adminListView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        }
    }

    private fun addShoeToFirestore() {
        val shoe = ShoeModel(
            brand = binding.etBrand.text.toString(),
            modelName = binding.etModel.text.toString(),
            releaseYear = binding.etYear.text.toString().toIntOrNull() ?: 0,
            resellPrice = binding.etPrice.text.toString().toIntOrNull() ?: 0,
            imageUrl = binding.etImageUrl.text.toString()
        )

        db.collection("sneakers").add(shoe).addOnSuccessListener {
            Toast.makeText(this, "Dodano pomyślnie!", Toast.LENGTH_SHORT).show()
            clearFields()
        }
    }

    private fun deleteShoe(id: String, position: Int) {
        if (id.isEmpty()) return
        db.collection("sneakers").document(id).delete().addOnSuccessListener {
            Toast.makeText(this, "Usunięto buta!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearFields() {
        binding.etBrand.text.clear()
        binding.etModel.text.clear()
        binding.etYear.text.clear()
        binding.etPrice.text.clear()
        binding.etImageUrl.text.clear()
    }
}