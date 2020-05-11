package com.calculaVirusApp

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.calculaVirusApp.model.Checklist
import kotlinx.android.synthetic.main.checklist_row.view.*

class ChecklistAdapter(private val dataList:MutableList<Checklist>): RecyclerView.Adapter<ChecklistHolder>() {
    private lateinit var context: Context

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ChecklistHolder {
        context = p0.context
        val layoutInflater = LayoutInflater.from(context)
        val cellForRow = layoutInflater.inflate(R.layout.checklist_row,p0,false)
        return ChecklistHolder(cellForRow)
    }

    override fun onBindViewHolder(p0: ChecklistHolder, p1: Int) {
        val data = dataList[p1]
        val texto = p0.itemView.checkrow
        texto.text=data.lugar_compra
        p0.checklist_id=data.id
    }
}

class ChecklistHolder(val view: View, var checklist_id:Int? = 1): RecyclerView.ViewHolder(view){
    init {
        view.checkrow.setOnClickListener {
            val intent = Intent(view.context,ChecklistDetailActivity::class.java)
            intent.putExtra("checklist_id", checklist_id)
            view.context.startActivity(intent)
        }
    }
}