package com.calculaVirusApp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.calculaVirusApp.R
import com.calculaVirusApp.holders.PlaceHolder
import com.calculaVirusApp.model.Place
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.place_element.view.*

class PlaceAdapter (private val dataList:MutableList<Place>): RecyclerView.Adapter<PlaceHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): PlaceHolder {
        context = parent.context
        return PlaceHolder(LayoutInflater.from(context).inflate(R.layout.place_element,parent,false))
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: PlaceHolder, position: Int) {
        val data = dataList[position]

        val name = holder.itemView.nombre
        val qty = holder.itemView.quantity
        val img = holder.itemView.image

        name.text = data.nombre
        qty.text = "Quantity: n/a"

        Picasso.get()
            .load(data.image)
            .into(img)

        holder.itemView.setOnClickListener(){
            Toast.makeText(context,data.descripcion,Toast.LENGTH_SHORT).show()
        }
    }
}