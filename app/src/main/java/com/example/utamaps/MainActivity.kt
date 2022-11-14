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

        //Boton mapa Uta
        val mapaUta = findViewById(R.id.MapaUniversidad) as Button

        mapaUta.setOnClickListener(View.OnClickListener() {
            val Intent = Intent(this, MapaGeneral::class.java)
            startActivity(Intent)
        })

        //Boton menu general
        val ubicaciones = findViewById(R.id.PtsInteres) as Button

        ubicaciones.setOnClickListener(View.OnClickListener(){
            val Intent = Intent(this, MenuUbicaciones::class.java)
            startActivity(Intent)
        })
    }
}