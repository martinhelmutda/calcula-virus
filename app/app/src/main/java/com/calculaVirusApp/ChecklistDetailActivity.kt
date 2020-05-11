package com.calculaVirusApp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
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
        val checklist_id: Int = intent.getIntExtra("checklist_id",1)
        recycler_checklist_detail.layoutManager=LinearLayoutManager(this)
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
    }
}
