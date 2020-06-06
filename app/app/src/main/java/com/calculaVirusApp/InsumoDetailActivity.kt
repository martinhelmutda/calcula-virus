package com.calculaVirusApp

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.ANRequest
import com.androidnetworking.common.ANResponse
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.calculaVirusApp.model.Insumo
import com.calculaVirusApp.model.LugarCompra
import com.calculaVirusApp.model.RequestInsumo
import com.calculaVirusApp.model.RequestLugarCompra
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_insumo_detail.*
import java.util.*

class InsumoDetailActivity : AppCompatActivity() {
    private lateinit var insumo_detail: Insumo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insumo_detail)
        val insumo_id: Int = intent.getIntExtra("insumo_id",1)
        val context = this
        var array_dropdown = arrayOf("Supermercado","Mercado","Tiendita","Otro")
        val adapter = ArrayAdapter(context,android.R.layout.simple_spinner_item,array_dropdown)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lugar_compra_insumo_editar.adapter = adapter
        lugar_compra_insumo_editar.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                // Display the selected item text on text view
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }


        AndroidNetworking.initialize(this)
        AndroidNetworking.get("http://192.168.1.84:8000/insumos/"+insumo_id)
            .build().getAsObject(Insumo::class.java,object:
                ParsedRequestListener<Insumo> {
                override fun onResponse(response: Insumo?) {
                    insumo_detail = response!!
                    nombre_insumo_editar.setText(insumo_detail.nombre)
                    descripcion_insumo_editar.setText(insumo_detail.descripcion)
                    categoria_insumo_editar.setText(insumo_detail.categoria)
                    prioridad_insumo_editar.setText(insumo_detail.prioridad.toString())
                    duracion_insumo_editar.setText(insumo_detail.duracion_promedio.toString())
                    cantidad_insumo_detail_editar.setText(insumo_detail.cantidad.toString())
                    marca_insumo_editar.setText(insumo_detail.marca)
                    var d:Date = insumo_detail.caducidad
                    fecha_caducidad_editar.updateDate(1900+d.year,d.month,d.date)
                    var lugar_compra = insumo_detail.lugar_compra
                    val adapter=lugar_compra_insumo_editar.adapter
                    val n = adapter.getCount()
                    Log.d("Count adapter",""+n)
                    val string_end=lugar_compra.lastIndexOf("/")
                    lugar_compra=lugar_compra.substring(0,string_end)
                    val string_start=lugar_compra.lastIndexOf("/")
                    lugar_compra = lugar_compra.substring(string_start+1)
                    val index_lugar = lugar_compra.toInt()
                    if(index_lugar==1){
                        lugar_compra_insumo_editar.setSelection(0)
                    }
                    else if(index_lugar==2){
                        lugar_compra_insumo_editar.setSelection(1)
                    }
                    else if(index_lugar==4){
                        lugar_compra_insumo_editar.setSelection(2)
                    }
                    else{
                        lugar_compra_insumo_editar.setSelection(3)
                    }
                    Picasso.get().load(insumo_detail.image).into(photo_insumo_editar)
                }

                override fun onError(anError: ANError?) {
                    Log.e("NetworkError",anError.toString())
                }
            })

        button_guardar_insumo_editar.setOnClickListener{
            val send_date=Date(fecha_caducidad_editar.year,fecha_caducidad_editar.month,fecha_caducidad_editar.dayOfMonth)
            val lugar_name =lugar_compra_insumo_editar.selectedItem.toString()
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
            AndroidNetworking.upload("http://192.168.1.84:8000/insumos/"+insumo_id.toString()+"/")
                .addMultipartParameter("id",insumo_id.toString())
                .addMultipartParameter("nombre",nombre_insumo_editar.text.toString())
                .addMultipartParameter("marca",marca_insumo_editar.text.toString())
                .addMultipartParameter("descripcion",descripcion_insumo_editar.text.toString())
                .addMultipartParameter("lugar_compra",lugar_id.toString())
                .addMultipartParameter("categoria",categoria_insumo_editar.text.toString())
                .addMultipartParameter("caducidad_year",fecha_caducidad_editar.year.toString())
                .addMultipartParameter("caducidad_month",fecha_caducidad_editar.month.toString())
                .addMultipartParameter("caducidad_day",fecha_caducidad_editar.dayOfMonth.toString())
                .addMultipartParameter("prioridad",prioridad_insumo_editar.text.toString())
                .addMultipartParameter("duracion_promedio",duracion_insumo_editar.text.toString())
                .addMultipartParameter("cantidad",cantidad_insumo_detail_editar.text.toString())
                .addMultipartFile("image",null)
                .setPriority(Priority.HIGH)
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

        button_eliminar_insumo.setOnClickListener{
            AndroidNetworking.initialize(this)
            AndroidNetworking.upload("http://192.168.1.84:8000/insumos/"+(insumo_id+10000).toString()+"/")
                .addMultipartParameter("id",insumo_id.toString())
                .setPriority(Priority.HIGH)
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
