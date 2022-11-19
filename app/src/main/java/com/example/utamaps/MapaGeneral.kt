//Actualizacion de ubicacion Version Final

package com.example.utamaps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.utamaps.databinding.ActivityMapaGeneralBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


class MapaGeneral : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener{

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapaGeneralBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var btnCalculate:Button

    private var start:String = ""
    private var end:String = ""

    private val LOG_TAG = "EnviarUbicacion"

    companion object{
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapaGeneralBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        enableLocation()
        createMarker()
        //createPolyLines()
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)

        btnCalculate = findViewById(R.id.btnCalculateRoute)
        btnCalculate.setOnClickListener{
            start = ""
            end = ""
            if (::mMap.isInitialized){
                mMap.setOnMapClickListener {
                    if (start.isEmpty()){
                        start = "${it.longitude}, ${it.latitude}"
                    }else if(end.isEmpty()){
                        end = "${it.longitude}, ${it.latitude}"
                        createRoute()
                    }
                }
            }
        }


        //Marcador Universidad de Tarapaca
        val universidad = LatLng(-18.490145119500152, -70.29633263195471)
        mMap.addMarker(MarkerOptions().position(universidad).title("Universidad de Tarapaca"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(universidad))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(universidad, 18F), 3000, null)

    }

    private fun createRoute(){
         CoroutineScope(Dispatchers.IO).launch {
             val call = getRetrofit().create(ApiService::class.java).getRoute("5b3ce3597851110001cf6248d522e41edcd94e51852cd0fddfe1970a", start, end)
             if (call.isSuccessful){
                Log.i("aris", "OK")
             }else{
                 Log.i("aris", "KO")
             }
         }
    }

    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

     fun createMarker(){
        //Aulario C
        var aularioC = LatLng(-18.49119864947299, -70.29738137069214)
        var markerB = mMap.addMarker(MarkerOptions().position(aularioC).title("Aulario C"))

        //Departamento de matematicas
        val deptoMatematicas = LatLng(-18.491765900973082, -70.29676982703793)
        val markerC = mMap.addMarker(MarkerOptions().position(deptoMatematicas).title("Departamento de matematicas"))

        //Registraduria
        val registraduria = LatLng(-18.490354171365787, -70.29657426208078)
        val markerD = mMap.addMarker(MarkerOptions().position(registraduria).title("Registraduria"))

        //Biblioteca Central
        val  bibliotecaCentral= LatLng(-18.490326190161305, -70.29592248528115)
        val markerE = mMap.addMarker(MarkerOptions().position(bibliotecaCentral).title("Biblioteca Central"))

        //Facultad de Ciencias Sociales y Jurídicas
        val facSociales = LatLng(-18.488426001426102, -70.29702487318757)
        val markerF = mMap.addMarker(MarkerOptions().position(facSociales).title("Facultad de Ciencias Sociales y Jurídicas"))

        //Aulario D
        val aularioD = LatLng(-18.488554848758422, -70.2967299870205)
        val markerG = mMap.addMarker(MarkerOptions().position(aularioD).title("Aulario D"))

        //Departamento de Informática
        val deptoInformatica= LatLng(-18.48916789676081, -70.29522526775676)
        val markerH = mMap.addMarker(MarkerOptions().position(deptoInformatica).title("Departamento de Informática"))


        val polylineOptions = PolylineOptions()
            .add(aularioC, registraduria)
            .width(5f)
            .color(ContextCompat.getColor(this, R.color.black))

        val polyline = mMap.addPolyline(polylineOptions)

    }

    //Funcion para saber si el permiso esta aceptado
    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    //Pedir los permisos de Ubicacion
    private fun requestLocationPermission(){
        //Si el usuario rechaza los permisos
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(this, "Ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        }
        else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION)
        }
    }

    //Funcion para entregar la ubicacion
    private fun enableLocation(){
        if (!::mMap.isInitialized) return
        if (isLocationPermissionGranted()){
            mMap.isMyLocationEnabled = true

        }
        else{
            requestLocationPermission()
        }
    }

    //Comprueba si el permiso fue aceptado
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_CODE_LOCATION -> if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                mMap.isMyLocationEnabled = true
            }
            else{
                Toast.makeText(this, "Para activar la localizacion ve a ajuste y acepta los permisos", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (!::mMap.isInitialized) return
        if (!isLocationPermissionGranted()){
            mMap.isMyLocationEnabled = false
            Toast.makeText(this, "Para activar la localizacion ve a ajuste y acepta los permisos", Toast.LENGTH_SHORT).show()
        }
    }

    //-----------------------------------------------------
    fun imprimirUbicacion(ubicacion: Location) {

        Log.d(LOG_TAG, "Latitud es ${ubicacion.latitude} y la longitud es ${ubicacion.longitude}")
        Toast.makeText(this,"Tu ubicacion actual es Latitud es ${ubicacion.latitude} y la longitud es ${ubicacion.longitude}",Toast.LENGTH_SHORT).show()
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "Boton pulsado", Toast.LENGTH_SHORT).show()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    imprimirUbicacion(it)
                } else {
                    Log.d(LOG_TAG, "No se pudo obtener la ubicación")
                }
            }
            //////
            val locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
                    Log.d(LOG_TAG, "Se recibió una actualización")
                    for (location in locationResult.locations) {
                        imprimirUbicacion(location)
                    }
                }
            }
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            Log.d(LOG_TAG, "Tal vez no solicitaste permiso antes")
        }

        return false
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(this, "Estas en ${p0.latitude}, ${p0.longitude}", Toast.LENGTH_LONG).show()
    }

}