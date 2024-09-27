package com.example.kuafrapp.View

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.kuafrapp.R

class SettingsFragment : Fragment() {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var notificationSwitch: Switch
    private lateinit var themeRadioGroup: RadioGroup
    private lateinit var languageSpinner: Spinner
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // SharedPreferences kullanarak ayarları kaydetmek için
        sharedPreferences = requireActivity().getSharedPreferences("AppSettings", 0)

        // Bildirim ayarları
        notificationSwitch = view.findViewById(R.id.notificationSwitch)
        val notificationsEnabled = sharedPreferences.getBoolean("notifications", true)
        notificationSwitch.isChecked = notificationsEnabled

        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            editor.putBoolean("notifications", isChecked)
            editor.apply()
            Toast.makeText(context, if (isChecked) "Bildirimler Açık" else "Bildirimler Kapalı", Toast.LENGTH_SHORT).show()
        }

        // Tema ayarları
        themeRadioGroup = view.findViewById(R.id.themeRadioGroup)
        val isDarkMode = sharedPreferences.getBoolean("darkMode", false)
        themeRadioGroup.check(if (isDarkMode) R.id.darkThemeButton else R.id.lightThemeButton)

        themeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.lightThemeButton -> setThemeMode(AppCompatDelegate.MODE_NIGHT_NO)
                R.id.darkThemeButton -> setThemeMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        // Dil Seçimi
        languageSpinner = view.findViewById(R.id.languageSpinner)
        val languages = resources.getStringArray(R.array.languages)
        val currentLanguage = sharedPreferences.getString("language", "Türkçe")
        languageSpinner.setSelection(languages.indexOf(currentLanguage))

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLanguage = languages[position]
                val editor = sharedPreferences.edit()
                editor.putString("language", selectedLanguage)
                editor.apply()
                Toast.makeText(context, "Dil değiştirildi: $selectedLanguage", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Boş bırakılabilir
            }
        }

        // Gizlilik politikası butonu
        val privacyPolicyButton: Button = view.findViewById(R.id.privacyPolicyButton)
        privacyPolicyButton.setOnClickListener {
            // Gizlilik politikası sayfasına yönlendirecek kod
            Toast.makeText(context, "Gizlilik Politikası açıldı", Toast.LENGTH_SHORT).show()
        }

        // Hakkında butonu
        val aboutButton: Button = view.findViewById(R.id.aboutButton)
        aboutButton.setOnClickListener {
            // Uygulama hakkında bilgi gösterecek kod
            Toast.makeText(context, "Uygulama hakkında bilgi", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun setThemeMode(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
        val editor = sharedPreferences.edit()
        editor.putBoolean("darkMode", mode == AppCompatDelegate.MODE_NIGHT_YES)
        editor.apply()
        Toast.makeText(context, if (mode == AppCompatDelegate.MODE_NIGHT_YES) "Koyu Tema" else "Açık Tema", Toast.LENGTH_SHORT).show()
    }
}
