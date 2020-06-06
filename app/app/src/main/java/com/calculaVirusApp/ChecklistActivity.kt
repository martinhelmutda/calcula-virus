package com.calculaVirusApp

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.calculaVirusApp.model.Checklist
import com.calculaVirusApp.model.RequestChecklist
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.activity_checklist.*


class ChecklistActivity : AppCompatActivity() {

    private val datalist:MutableList<Checklist> = mutableListOf()
    private lateinit var checklistAdapter: ChecklistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checklist)
        setSupportActionBar(findViewById(R.id.toolbar))
        val account = GoogleSignIn.getLastSignedInAccount(this)
        var user_email="barrons.guillermo.sal@gmail.com"
        if(account!=null){
            user_email = account.email!!
        }
        recycler_checklist.layoutManager = LinearLayoutManager(this)
        checklistAdapter = ChecklistAdapter(datalist)
        recycler_checklist.adapter = checklistAdapter

        AndroidNetworking.initialize(this)
        /*AndroidNetworking.get("http://192.168.1.84:8000/checklist")
            .build().getAsObject(RequestChecklist::class.java,object: ParsedRequestListener<RequestChecklist>{
                override fun onResponse(response: RequestChecklist?) {
                    response?.results?.let{datalist.addAll(it)}
                    checklistAdapter.notifyDataSetChanged()
                }

                override fun onError(anError: ANError?) {
                    Log.e("NetworkError",anError.toString())
                }
            })*/
        AndroidNetworking.get("http://192.168.1.84:8000/checklist/get_checklist_by_user/")
            .addQueryParameter("user_email",user_email)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        menu?.findItem(R.id.toolbar)?.title = "Calcula virus"
        return true
    }
}
