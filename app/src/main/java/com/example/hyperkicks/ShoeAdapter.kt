package com.example.hyperkicks

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.example.hyperkicks.databinding.ItemShoeBinding
import com.example.hyperkicks.model.ShoeModel
//import kotlin.io.root

class ShoeAdapter (
    private val context: Context,
    private val shoeList: List<ShoeModel>
): BaseAdapter() {

    override fun getCount(): Int = shoeList.size

    override fun getItem(p0: Int): Any = shoeList[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    @SuppressLint("SetTextI18n")
    override fun getView(
        position: Int,
        converterView: View?,
        parent: ViewGroup?
    ): View? {
        val binding: ItemShoeBinding
        val view: View?

        if(converterView == null) {
            val inflater = LayoutInflater.from(context)
            binding = ItemShoeBinding.inflate(inflater, parent, false)
            view = binding.root

            view.tag = binding

        } else {
            view = converterView
            binding = view.tag as ItemShoeBinding
        }

        val shoe = shoeList[position]
        binding.shoeModelTextView.text = shoe.modelName
        binding.shoePriceTextView.text = "Cena: ${shoe.resellPrice}zł"

        Glide.with(context)
            .load(shoe.imageUrl)
            .placeholder(R.mipmap.ic_launcher)
            .into(binding.shoeImageView)

        return view
    }

}