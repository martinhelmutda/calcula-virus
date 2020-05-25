package com.calculaVirusApp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.calculaVirusApp.model.RequestInsumo
import kotlinx.android.synthetic.main.activity_crear_insumo.*
import kotlinx.android.synthetic.main.activity_insumo_detail.*
import java.util.*

class CrearInsumoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_insumo)
        val context = this
        var array_dropdown = arrayOf("Supermercado","Mercado","Tiendita","Otro")
        val adapter = ArrayAdapter(context,android.R.layout.simple_spinner_item,array_dropdown)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lugar_compra_insumo_crear.adapter = adapter
        lugar_compra_insumo_crear.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                // Display the selected item text on text view
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }
        button_guardar_insumo_crear.setOnClickListener{
            val send_date=
                Date(fecha_caducidad_crear.year,fecha_caducidad_crear.month,fecha_caducidad_crear.dayOfMonth)
            val lugar_name =lugar_compra_insumo_crear.selectedItem.toString()
            var lugar_id = 0
            if(lugar_name=="Supermercado"){
                lugar_id=1
            }
            else if(lugar_name=="Mercado"){
                lugar_id=2
            }
            else if(lugar_name=="Tiendita"){
                lugar_id=4
            }
            else{
                lugar_id=5
            }
            AndroidNetworking.initialize(this)
            AndroidNetworking.upload("http://192.168.1.84:8000/insumos/"+0+"/")
                .addMultipartParameter("nombre",nombre_insumo_crear.text.toString())
                .addMultipartParameter("marca",marca_insumo_crear.text.toString())
                .addMultipartParameter("descripcion",descripcion_insumo_crear.text.toString())
                .addMultipartParameter("lugar_compra",lugar_id.toString())
                .addMultipartParameter("categoria",categoria_insumo_crear.text.toString())
                .addMultipartParameter("caducidad_year",fecha_caducidad_crear.year.toString())
                .addMultipartParameter("caducidad_month",fecha_caducidad_crear.month.toString())
                .addMultipartParameter("caducidad_day",fecha_caducidad_crear.dayOfMonth.toString())
                .addMultipartParameter("prioridad",prioridad_insumo_crear.text.toString())
                .addMultipartParameter("duracion_promedio",duracion_insumo_crear.text.toString())
                .addMultipartParameter("cantidad",cantidad_insumo_detail_crear.text.toString())
                .addMultipartFile("image",null)
                //.setPriority(Priority.HIGH)
                .build()
                .getAsObject(RequestInsumo::class.java,object:
                    ParsedRequestListener<RequestInsumo> {
                    override fun onResponse(response: RequestInsumo?) {
                    }

                    override fun onError(anError: ANError?) {
                        Log.e("NetworkError",anError.toString())
                    }
                })
            val intent = Intent(this,InsumoActivity::class.java)
            this.startActivity(intent)
        }
    }
}
