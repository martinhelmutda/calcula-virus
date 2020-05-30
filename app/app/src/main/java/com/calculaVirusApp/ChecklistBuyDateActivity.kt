package com.calculaVirusApp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.calculaVirusApp.model.FechaCompra
import kotlinx.android.synthetic.main.activity_checklist_buy_date.*

class ChecklistBuyDateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checklist_buy_date)
        val checklist_id: Int = intent.getIntExtra("checklist_id",1)
        AndroidNetworking.initialize(this)
        AndroidNetworking.get("http://192.168.1.84:8000/checklist/"+checklist_id+"/get_buy_date")
            .build()
            .getAsObject(FechaCompra::class.java,object: ParsedRequestListener<FechaCompra> {
                override fun onResponse(response: FechaCompra?) {
                    fecha_proxima_compra.text = ""+response!!.buy_date
                }

                override fun onError(anError: ANError?) {
                    Log.e("NetworkError",anError.toString())
                }
            })
    }
}
