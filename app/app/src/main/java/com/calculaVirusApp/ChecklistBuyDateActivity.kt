package com.calculaVirusApp

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.calculaVirusApp.model.FechaCompra
import kotlinx.android.synthetic.main.activity_checklist_buy_date.*

class ChecklistBuyDateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checklist_buy_date)
        setSupportActionBar(findViewById(R.id.toolbar))
        val checklist_id: Int = intent.getIntExtra("checklist_id",1)
        AndroidNetworking.initialize(this)
        AndroidNetworking.get("http://martinhelmut.pythonanywhere.com/checklist/"+checklist_id+"/get_buy_date")
            .build()
            .getAsObject(FechaCompra::class.java,object: ParsedRequestListener<FechaCompra> {
                override fun onResponse(response: FechaCompra?) {
                    //fecha_proxima_compra.text = ""+response!!.buy_date
                    val d = response!!.buy_date
                    fecha_compra_picker.updateDate(1900+d.year,d.month,d.date)
                }

                override fun onError(anError: ANError?) {
                    Log.e("NetworkError",anError.toString())
                }
            })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        menu?.findItem(R.id.toolbar)?.title = "Calcula virus"
        return true
    }
}
