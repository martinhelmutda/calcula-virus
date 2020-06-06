package com.calculaVirusApp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.calculaVirusApp.model.Checklist
import com.calculaVirusApp.model.Insumo
import kotlinx.android.synthetic.main.checklist_row.view.*
import kotlinx.android.synthetic.main.insumo_row.view.*

class InsumoAdapter(private val dataList:MutableList<Insumo>): RecyclerView.Adapter<InsumoHolder>() {
    private lateinit var context: Context

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): InsumoHolder {
        context = p0.context
        val layoutInflater = LayoutInflater.from(context)
        val cellForRow = layoutInflater.inflate(R.layout.insumo_row,p0,false)
        return InsumoHolder(cellForRow)
    }

    override fun onBindViewHolder(p0: InsumoHolder, p1: Int) {
        val data = dataList[p1]
        val insumo_button = p0.itemView.insumo_button
        insumo_button.text=data.nombre
        p0.insumo_id = data.id
    }
}

class InsumoHolder(val view: View, var insumo_id:Int? = 1): RecyclerView.ViewHolder(view){
    init {
        view.insumo_button.setOnClickListener {
            val intent = Intent(view.context,InsumoDetailActivity::class.java)
            intent.putExtra("insumo_id", insumo_id)
            view.context.startActivity(intent)
        }
    }
}