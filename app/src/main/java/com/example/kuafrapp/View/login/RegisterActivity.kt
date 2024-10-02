package com.example.kuafrapp.View.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kuafrapp.databinding.ActivityRegisterBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class RegisterActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var selectedLocation: LatLng
    // İstek kodunu burada tanımlayın
    private val PHOTO_PICK_REQUEST_CODE = 1001

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    private fun setupListeners() {
        // Switch aktif olduğunda berber bilgileri alanlarını göster
        binding.barberSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.barberInfoLayout.visibility = View.VISIBLE
            } else {
                binding.barberInfoLayout.visibility = View.GONE
            }
        }

        binding.registerButton.setOnClickListener {
            val fullName = binding.fullNameEt.text.toString()
            val email = binding.eMailEt.text.toString()
            val password = binding.passwordEt.text.toString()
            val rePassword = binding.rePasswordEt.text.toString()
            val businessName = binding.businessNameEt.text.toString()
            val selectedLocation = selectedLocation // Haritadan seçilen konum
            val services = getSelectedServices() // Seçilen hizmetler

            if (binding.barberSwitch.isChecked) {
                registerBarber(fullName, email, password, rePassword, businessName, selectedLocation, services)
            }

            if (binding.barberSwitch.isChecked) {
                // Berber bilgileri
                val businessName = binding.businessNameEt.text.toString()
                val location = binding.locationEt.text.toString()

                // Fotoğraf işlemi (seçilen fotoğraf için bir URI veya yol alınmalı)
                // Örneğin: val photoUri = ...

                // Bu bilgileri kaydet
                registerBarber(fullName, email, password, rePassword, businessName, location)
            } else {
                // Sadece normal kullanıcı bilgilerini kaydet
                registerUser(fullName, email, password, rePassword)
            }
        }

        binding.selectPhotoButton.setOnClickListener {
            // Fotoğraf seçimi işlemi (Fotoğraf seçme için bir intent açılabilir)
            selectPhoto()
        }
    }

    private fun getSelectedServices(): List<String> {
        val selectedServices = mutableListOf<String>()

        if (binding.menHairCheckBox.isChecked) selectedServices.add("Erkek Kuaför")
        if (binding.womenHairCheckBox.isChecked) selectedServices.add("Kadın Kuaför")
        if (binding.petGroomingCheckBox.isChecked) selectedServices.add("Pet Kuaför")
        if (binding.carWashCheckBox.isChecked) selectedServices.add("Araba Yıkama")
        if (binding.kidHairCheckBox.isChecked) selectedServices.add("Çocuk Kuaför")
        if (binding.spaMassageCheckBox.isChecked) selectedServices.add("Spa ve Masaj")
        if (binding.nailCareCheckBox.isChecked) selectedServices.add("Tırnak Bakımı")
        if (binding.skinCareCheckBox.isChecked) selectedServices.add("Cilt Bakımı")

        return selectedServices
    }

    private fun registerUser(fullName: String, email: String, password: String, rePassword: String) {
        // Kullanıcı kaydı işlemleri
        if (password == rePassword) {
            // Kullanıcıyı kaydet
            // viewModel.registerUser(fullName, email, password)
        } else {
            Toast.makeText(this, "Şifreler eşleşmiyor", Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerBarber(fullName: String, email: String, password: String, rePassword: String, businessName: String, location: String) {
        // Berber kaydı işlemleri
        if (password == rePassword) {
            // Berberi kaydet (businessName, location ve fotoğrafı da kaydet)
            // viewModel.registerBarber(fullName, email, password, businessName, location)
        } else {
            Toast.makeText(this, "Şifreler eşleşmiyor", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PHOTO_PICK_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHOTO_PICK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val photoUri = data.data
            // Seçilen fotoğrafın URI'sini kaydet
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Varsayılan konumu belirle (Örn: İstanbul)
        val istanbul = LatLng(41.0082, 28.9784)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(istanbul, 12f))

        // Haritaya tıklayınca kullanıcıdan konumu al
        googleMap.setOnMapClickListener { latLng ->
            selectedLocation = latLng
            googleMap.clear() // Önceki işaretçileri temizle
            googleMap.addMarker(MarkerOptions().position(latLng).title("Seçilen Konum"))
        }
    }

    // Konumu kaydetme işlemi
    private fun registerBarber(fullName: String, email: String, password: String, rePassword: String, businessName: String, location: LatLng, services: List<String>) {
        // İşlemleri burada yapın
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
