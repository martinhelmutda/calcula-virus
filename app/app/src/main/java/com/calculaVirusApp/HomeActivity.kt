package com.calculaVirusApp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.calculaVirusApp.adapters.PlaceAdapter
import com.calculaVirusApp.model.Place
import com.calculaVirusApp.model.Request
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val dataList: MutableList<Place> = mutableListOf()
    private lateinit var placeAdapter: PlaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(findViewById(R.id.toolbar))
        // Set up Notifications
        setUpUserChannel()

        //Set up Adapter
        placeAdapter = PlaceAdapter(dataList)

        //Set Up recyclerview
        placesList.layoutManager =
            LinearLayoutManager(this)
        placesList.addItemDecoration(
            DividerItemDecoration(
                this,
                OrientationHelper.VERTICAL
            )
        )
        placesList.adapter = placeAdapter
        gotochecklist.setOnClickListener {
            intent = Intent(this,ChecklistActivity::class.java)
            startActivity(intent)
        }

        gotoinsumos.setOnClickListener {
            intent = Intent(this,InsumoActivity::class.java)
            intent.putExtra("query_type", 1)
            startActivity(intent)
        }

        //Set up Android Networking
        AndroidNetworking.initialize(this)
        AndroidNetworking.get("http://martinhelmut.pythonanywhere.com/lugares/")
            .build()
            .getAsObject(Request::class.java, object : ParsedRequestListener<Request> {
                override fun onResponse(response: Request?) {
                    response?.results?.let { dataList.addAll(it) }
                    placeAdapter.notifyDataSetChanged()
                    Log.d("Algo llego", response.toString())
                }

                override fun onError(anError: ANError?) {
                    Log.e("NetworkError",anError.toString())
                }

            })
        Log.d("Bandera","Se han terminado los set ups")
    }

    fun setUpUserChannel() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val email = user.email
            val parts = email!!.replace("@","")

            FirebaseMessaging.getInstance().subscribeToTopic(parts)
                .addOnCompleteListener { task ->
                    var msg = getString(R.string.msg_subscribed)
                    if (!task.isSuccessful) {
                        msg = getString(R.string.msg_subscribe_failed)
                    }
                    Log.d(TAG, msg)
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        menu?.findItem(R.id.toolbar)?.title = "Calcula virus"
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logOut -> signOut()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun signOut() {
        startActivity(LogInGoogle.getLaunchIntent(this))
        FirebaseAuth.getInstance().signOut()
    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
}
