package com.calculaVirusApp

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.calculaVirusApp.model.Request
import com.calculaVirusApp.model.RequestInsumo
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.activity_crear_insumo.*
import kotlinx.android.synthetic.main.activity_insumo_detail.*
import java.io.*
import java.net.URI
import java.util.*
import kotlin.collections.HashMap

class CrearInsumoActivity : AppCompatActivity() {
    lateinit var image_file: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_insumo)
        setSupportActionBar(findViewById(R.id.toolbar))
        val context = this
        var array_dropdown = arrayOf("Supermercado","Mercado","Tiendita","Otro")
        val account = GoogleSignIn.getLastSignedInAccount(this)
        var user_email="barrons.guillermo.sal@gmail.com"
        if(account!=null){
            user_email = account.email!!
        }
        val places = mutableListOf<String>()
        val places_id = mutableMapOf<String,Int>()
        AndroidNetworking.initialize(this)
        AndroidNetworking.get("http://martinhelmut.pythonanywhere.com/lugares")
            .addQueryParameter("user_email",user_email)
            .build()
            .getAsObject(Request::class.java, object : ParsedRequestListener<Request> {
                override fun onResponse(response: Request?) {
                    for(lugar in response?.results!!){
                        places.add(lugar.nombre)
                        places_id[lugar.nombre]=lugar.id
                    }
                    val array_places = places.toTypedArray()
                    val adapter = ArrayAdapter(context,android.R.layout.simple_spinner_item,array_places)
                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                    lugar_compra_insumo_crear.adapter = adapter
                    lugar_compra_insumo_crear.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                            // Display the selected item text on text view
                        }

                        override fun onNothingSelected(parent: AdapterView<*>){
                            // Another interface callback
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.e("NetworkError",anError.toString())
                }

            })
        photo_button.setOnClickListener{
            if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
                val camera_intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(camera_intent, 1)
            }
            else{
            }
        }
        button_guardar_insumo_crear.setOnClickListener{
            val send_date=
                Date(fecha_caducidad_crear.year,fecha_caducidad_crear.month,fecha_caducidad_crear.dayOfMonth)
            //val img = photo_insumo.drawable
            val lugar_name =lugar_compra_insumo_crear.selectedItem.toString()
            var lugar_id = places_id[lugar_name]
            AndroidNetworking.initialize(this)
            AndroidNetworking.upload("http://martinhelmut.pythonanywhere.com/insumos/"+0+"/")
                .addMultipartParameter("nombre",nombre_insumo_crear.text.toString())
                .addMultipartParameter("marca",marca_insumo_crear.text.toString())
                .addMultipartParameter("descripcion",descripcion_insumo_crear.text.toString())
                .addMultipartParameter("lugar_compra",lugar_id.toString())
                .addMultipartParameter("categoria",categoria_insumo_crear.text.toString())
                .addMultipartParameter("caducidad_year",fecha_caducidad_crear.year.toString())
                .addMultipartParameter("caducidad_month",fecha_caducidad_crear.month.toString())
                .addMultipartParameter("caducidad_day",fecha_caducidad_crear.dayOfMonth.toString())
                .addMultipartParameter("prioridad",prioridad_insumo_crear.text.toString())
                .addMultipartParameter("duracion_promedio",duracion_insumo_crear.text.toString())
                .addMultipartParameter("cantidad",cantidad_insumo_detail_crear.text.toString())
                .addMultipartParameter("user_id","1")
                .addMultipartParameter("user_email",user_email)
                .addMultipartFile("image",File(image_file.path))
                //.setPriority(Priority.HIGH)
                .build()
                .getAsObject(RequestInsumo::class.java,object:
                    ParsedRequestListener<RequestInsumo> {
                    override fun onResponse(response: RequestInsumo?) {
                    }

                    override fun onError(anError: ANError?) {
                        Log.e("NetworkError",anError.toString())
                    }
                })
            AndroidNetworking.post("http://martinhelmut.pythonanywhere.com/checklistinsumo/create_insumo_row/")
                .addBodyParameter("user_id","1")
                .addBodyParameter("user_email",user_email)
                .addBodyParameter("lugar_compra",lugar_name)
                .addBodyParameter("insumo_nombre",nombre_insumo_crear.text.toString())
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val bits = data?.extras?.get("data") as Bitmap
        image_file = bitmapToFile(bits)
        photo_insumo.setImageBitmap(bits)
    }


    // Method to save an bitmap to a file
    private fun bitmapToFile(bitmap:Bitmap): Uri {
        // Get the context wrapper
        val wrapper = ContextWrapper(applicationContext)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file,"${UUID.randomUUID()}.jpg")

        try{
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        }catch (e: IOException){
            e.printStackTrace()
        }

        // Return the saved bitmap uri
        return Uri.parse(file.absolutePath)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        menu?.findItem(R.id.toolbar)?.title = "Calcula virus"
        return true
    }
}
