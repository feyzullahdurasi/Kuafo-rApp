package com.example.kuafrapp.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kuafrapp.View.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Kullanıcının giriş yapıp yapmadığını kontrol et
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // Eğer giriş yapılmışsa MainActivity'ye yönlendir
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
        } else {
            // Giriş yapılmamışsa LoginActivity'ye yönlendir
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        finish() // SplashActivity'yi kapat
    }
}
