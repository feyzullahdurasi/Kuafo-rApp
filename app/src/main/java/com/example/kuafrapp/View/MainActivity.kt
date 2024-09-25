package com.example.kuafrapp.View

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kuafrapp.R
import com.example.kuafrapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var serviceType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // InfoActivity'den gelen hizmet türünü alıyoruz
        serviceType = intent.getStringExtra("SERVICE_TYPE")

        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView

        // İlk başta HomeFragment'ı gösterelim ve hizmet tipini aktaralım
        val homeFragment = HomeFragment()
        serviceType?.let {
            val bundle = Bundle()
            bundle.putString("SERVICE_TYPE", it)
            homeFragment.arguments = bundle
        }
        replaceFragment(homeFragment)

        // BottomNavigationView'de item seçildiğinde fragment değiştirme
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    val homeFragment = HomeFragment()
                    serviceType?.let {
                        val bundle = Bundle()
                        bundle.putString("SERVICE_TYPE", it)
                        homeFragment.arguments = bundle
                    }
                    replaceFragment(homeFragment)
                    true
                }
                R.id.navigation_maps -> {
                    val mapsFragment = MapsFragment()
                    serviceType?.let {
                        val bundle = Bundle()
                        bundle.putString("SERVICE_TYPE", it)
                        mapsFragment.arguments = bundle
                    }
                    replaceFragment(mapsFragment)
                    true
                }
                R.id.navigation_settings -> {
                    replaceFragment(SettingsFragment())
                    true
                }
                else -> false
            }
        }
    }

    // Fragment değiştirme fonksiyonu
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
    }
}
