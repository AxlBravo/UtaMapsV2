package com.example.utamaps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Boton
        val mapaUta = findViewById(R.id.MapaUniversidad) as Button
        val botonrelleno = findViewById(R.id.BotonRelleno) as Button
        mapaUta.setOnClickListener(View.OnClickListener() {
            val Intent = Intent(this, MapaGeneral::class.java)
            startActivity(Intent)
        })
        botonrelleno.setOnClickListener(View.OnClickListener {
            val principal = Intent(this,MenuPrincipal::class.java)
            startActivity(principal)
        })
    }
}