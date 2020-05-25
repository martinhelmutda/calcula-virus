package com.calculaVirusApp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.calculaVirusApp.model.Insumo
import com.calculaVirusApp.model.RequestChecklist
import com.calculaVirusApp.model.RequestInsumo
import kotlinx.android.synthetic.main.activity_insumo.*

class InsumoActivity : AppCompatActivity() {
    private val datalist:MutableList<Insumo> = mutableListOf()
    private lateinit var insumoAdapter: InsumoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insumo)
        recycler_insumo.layoutManager = LinearLayoutManager(this)
        insumoAdapter = InsumoAdapter(datalist)
        recycler_insumo.adapter = insumoAdapter

        AndroidNetworking.initialize(this)
        AndroidNetworking.get("http://192.168.1.84:8000/insumos")
            .build().getAsObject(RequestInsumo::class.java,object:
                ParsedRequestListener<RequestInsumo> {
                override fun onResponse(response: RequestInsumo?) {
                    response?.results?.let{datalist.addAll(it)}
                    insumoAdapter.notifyDataSetChanged()
                }

                override fun onError(anError: ANError?) {
                    Log.e("NetworkError",anError.toString())
                }
            })
        crear_insumo.setOnClickListener({
            val intent = Intent(this,CrearInsumoActivity::class.java)
            this.startActivity(intent)
        })
    }
}
