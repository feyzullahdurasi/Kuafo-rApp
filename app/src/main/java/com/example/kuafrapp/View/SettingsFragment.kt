package com.example.kuafrapp.View

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.kuafrapp.R

class SettingsFragment : Fragment() {

    private lateinit var notificationSwitch: Switch
    private lateinit var changeThemeButton: Button
    private lateinit var languageSpinner: Spinner
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var updateInfoButton: Button
    private lateinit var logoutButton: Button
    private lateinit var feedbackButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("AppSettings", 0)

        // Bildirim Ayarları
        notificationSwitch = view.findViewById(R.id.notificationSwitch)
        notificationSwitch.isChecked = sharedPreferences.getBoolean("notifications", false)
        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("notifications", isChecked).apply()
            Toast.makeText(context, if (isChecked) "Notifications Enabled" else "Notifications Disabled", Toast.LENGTH_SHORT).show()
        }

        // Feedback Button
        feedbackButton = view.findViewById(R.id.feedbackButton)
        feedbackButton.setOnClickListener {
            sendFeedback()
        }

        // Tema Seçimi
        changeThemeButton = view.findViewById(R.id.changeThemeButton)
        changeThemeButton.setOnClickListener {
            toggleTheme()
        }

        // Dil Seçimi
        languageSpinner = view.findViewById(R.id.languageSpinner)
        val languages = resources.getStringArray(R.array.languages)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        languageSpinner.adapter = adapter

        languageSpinner.setSelection(languages.indexOf(sharedPreferences.getString("language", "English")))
        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sharedPreferences.edit().putString("language", languages[position]).apply()
                Toast.makeText(context, "Language set to: ${languages[position]}", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Kullanıcı Bilgilerini Güncelle
        updateInfoButton = view.findViewById(R.id.updateInfoButton)
        updateInfoButton.setOnClickListener {
            // Navigate to User Info Screen
        }

        // Çıkış
        logoutButton = view.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            logout()
        }

        return view
    }

    private fun sendFeedback() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("info@businessowner.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Feedback")
        }
        startActivity(intent)
    }

    private fun toggleTheme() {
        val isDarkMode = sharedPreferences.getBoolean("darkMode", false)
        val newMode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
        AppCompatDelegate.setDefaultNightMode(newMode)
        sharedPreferences.edit().putBoolean("darkMode", !isDarkMode).apply()
        Toast.makeText(context, if (!isDarkMode) "Dark Mode Enabled" else "Light Mode Enabled", Toast.LENGTH_SHORT).show()
    }

    private fun logout() {
        sharedPreferences.edit().clear().apply()
        Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show()
    }
}
