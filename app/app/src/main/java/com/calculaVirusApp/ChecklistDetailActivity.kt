package com.calculaVirusApp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.calculaVirusApp.model.Checklist
import com.calculaVirusApp.model.ChecklistInsumo
import com.calculaVirusApp.model.RequestChecklist
import com.calculaVirusApp.model.RequestChecklistInsumo
import kotlinx.android.synthetic.main.activity_checklist_detail.*

class ChecklistDetailActivity : AppCompatActivity() {

    private val datalist:MutableList<ChecklistInsumo> = mutableListOf()
    private lateinit var checklist_detailAdapter: ChecklistDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checklist_detail)
        setSupportActionBar(findViewById(R.id.toolbar))
        val checklist_id: Int = intent.getIntExtra("checklist_id",1)
        recycler_checklist_detail.layoutManager= LinearLayoutManager(this)
        checklist_detailAdapter = ChecklistDetailAdapter(datalist)
        recycler_checklist_detail.adapter = checklist_detailAdapter

        AndroidNetworking.initialize(this)
        AndroidNetworking.get("http://192.168.1.84:8000/checklistinsumo/")
            .build().getAsObject(RequestChecklistInsumo::class.java,object:
                ParsedRequestListener<RequestChecklistInsumo> {
                override fun onResponse(response: RequestChecklistInsumo?) {
                    response?.results?.let{
                        for(item in it){
                            if(item.checklist_id==checklist_id){
                                datalist.add(item)
                            }
                        }
                        //datalist.addAll(it)
                    }
                    checklist_detailAdapter.notifyDataSetChanged()
                }

                override fun onError(anError: ANError?) {
                    Log.e("NetworkError",anError.toString())
                }
            })
        button_proxima_compra.setOnClickListener {
            var intent = Intent(this,ChecklistBuyDateActivity::class.java)
            intent.putExtra("checklist_id", checklist_id)
            this.startActivity(intent)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        menu?.findItem(R.id.toolbar)?.title = "Calcula virus"
        return true
    }
}
