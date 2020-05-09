package com.calculaVirusApp

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupUI()
        setSupportActionBar(findViewById(R.id.toolbar));
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu);
        menu?.findItem(R.id.toolbar)?.title = "Calcula virus"
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logOut -> signOut()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupUI() {
        sign_out_button.setOnClickListener {
            signOut()
        }
    }
    private fun signOut() {
        startActivity(LogInGoogle.getLaunchIntent(this))
        FirebaseAuth.getInstance().signOut();
    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
}
