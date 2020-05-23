package com.calculaVirusApp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.calculaVirusApp.model.Checklist
import kotlinx.android.synthetic.main.activity_checklist.*
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.calculaVirusApp.model.RequestChecklist

class ChecklistActivity : AppCompatActivity() {

    private val datalist:MutableList<Checklist> = mutableListOf()
    private lateinit var checklistAdapter: ChecklistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checklist)
        recycler_checklist.layoutManager = LinearLayoutManager(this)
        checklistAdapter = ChecklistAdapter(datalist)
        recycler_checklist.adapter = checklistAdapter

        AndroidNetworking.initialize(this)
        AndroidNetworking.get("http://192.168.1.84:8000/checklist")
            .build().getAsObject(RequestChecklist::class.java,object: ParsedRequestListener<RequestChecklist>{
                override fun onResponse(response: RequestChecklist?) {
                    response?.results?.let{datalist.addAll(it)}
                    checklistAdapter.notifyDataSetChanged()
                }

                override fun onError(anError: ANError?) {
                    Log.e("NetworkError",anError.toString())
                }
            })
    }
}
