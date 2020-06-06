package com.calculaVirusApp

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
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
import com.calculaVirusApp.model.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_insumo_detail.*
import java.util.*

class InsumoDetailActivity : AppCompatActivity() {
    private lateinit var insumo_detail: Insumo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insumo_detail)
        setSupportActionBar(findViewById(R.id.toolbar))
        val insumo_id: Int = intent.getIntExtra("insumo_id",1)


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
                    val string_end=lugar_compra.lastIndexOf("/")
                    lugar_compra=lugar_compra.substring(0,string_end)
                    val string_start=lugar_compra.lastIndexOf("/")
                    lugar_compra = lugar_compra.substring(string_start+1)
                    val index_lugar = lugar_compra.toInt()
                    Picasso.get().load(insumo_detail.image).into(photo_insumo_editar)
                }

                override fun onError(anError: ANError?) {
                    Log.e("NetworkError",anError.toString())
                }
            })

        button_guardar_insumo_editar.setOnClickListener{
            val send_date=Date(fecha_caducidad_editar.year,fecha_caducidad_editar.month,fecha_caducidad_editar.dayOfMonth)
            var lugar_id = 0
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        menu?.findItem(R.id.toolbar)?.title = "Calcula virus"
        return true
    }
}
