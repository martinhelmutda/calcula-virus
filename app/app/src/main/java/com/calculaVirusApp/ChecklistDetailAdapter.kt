package com.calculaVirusApp

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.calculaVirusApp.model.ChecklistInsumo
import com.calculaVirusApp.model.RequestChecklistInsumo
import kotlinx.android.synthetic.main.checklist_insumo_row.view.*

class ChecklistDetailAdapter(private val datalist:MutableList<ChecklistInsumo>) : RecyclerView.Adapter<ChecklistInsumoHolder>() {
    private lateinit var context: Context

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ChecklistInsumoHolder {
        context=p0.context
        val layoutInflater = LayoutInflater.from(context)
        val cellForRow = layoutInflater.inflate(R.layout.checklist_insumo_row,p0,false)
        return ChecklistInsumoHolder(cellForRow)
    }

    override fun onBindViewHolder(p0: ChecklistInsumoHolder, p1: Int) {
        val data = datalist[p1]
        val texto_cantidad_edit = p0.itemView.cantidad_insumo_editar
        texto_cantidad_edit.setText(data.cantidad.toString())
        val insumo_nombre = data.insumo_nombre
        val check_point = p0.itemView.checked_box
        check_point.text = insumo_nombre
        check_point.isChecked = data.comprado
        p0?.id_checklistinsumo=data.id
    }
}

class ChecklistInsumoHolder(val view: View, var id_checklistinsumo: Int?=0): RecyclerView.ViewHolder(view){
    init {
        view.checked_box.setOnClickListener{
            var cantidad: String = "0"
            if(!view.checked_box.isChecked){
                cantidad = view.cantidad_insumo_editar.text.toString()
            }
            else{
                cantidad = "0"
                view.cantidad_insumo_editar.setText("0")
            }
            AndroidNetworking.initialize(view.context)
            AndroidNetworking.put("http://192.168.1.84:8000/checklistinsumo/"+id_checklistinsumo.toString()+"/")
                .addBodyParameter("cantidad",cantidad)
                .addBodyParameter("comprado",view.checked_box.isChecked.toString())
                .build()
                .getAsObject(RequestChecklistInsumo::class.java,object:
                    ParsedRequestListener<RequestChecklistInsumo> {
                    override fun onResponse(response: RequestChecklistInsumo?) {
                    }

                    override fun onError(anError: ANError?) {
                        Log.e("NetworkError",anError.toString())
                    }
                })
        }
        view.cantidad_insumo_editar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                AndroidNetworking.initialize(view.context)
                AndroidNetworking.put("http://192.168.1.84:8000/checklistinsumo/"+id_checklistinsumo.toString()+"/")
                    .addBodyParameter("cantidad",view.cantidad_insumo_editar.text.toString())
                    .addBodyParameter("comprado",view.checked_box.isChecked.toString())
                    .build()
                    .getAsObject(RequestChecklistInsumo::class.java,object:
                        ParsedRequestListener<RequestChecklistInsumo> {
                        override fun onResponse(response: RequestChecklistInsumo?) {
                        }

                        override fun onError(anError: ANError?) {
                            Log.e("NetworkError",anError.toString())
                        }
                    })
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }
}