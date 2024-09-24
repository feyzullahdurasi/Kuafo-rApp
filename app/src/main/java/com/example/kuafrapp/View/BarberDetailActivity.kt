package com.example.kuafrapp.View

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.kuafrapp.databinding.ActivityBarberDetailBinding
import com.example.kuafrapp.util.dowloandImage
import com.example.kuafrapp.util.makePlaceHolder
import com.example.kuafrapp.viewmodel.BarberDetailViewModel
import java.text.SimpleDateFormat
import java.util.*

class BarberDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBarberDetailBinding
    private lateinit var viewModel: BarberDetailViewModel
    private var barberId = 0
    private val calendar = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val bookedDates = listOf("15/10/2024", "16/10/2024") // Dolu olan tarihler örneği, API'den gelmeli
    private val availableHours = (9..18).toList() // Günlük saat aralığı (9:00 - 18:00)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding kullanımı
        binding = ActivityBarberDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel başlatma
        viewModel = ViewModelProvider(this).get(BarberDetailViewModel::class.java)

        // Intent ile gönderilen barberId'yi al
        intent?.let {
            barberId = it.getIntExtra("barberId", 0)
        }

        // ViewModel'den veriyi çek
        viewModel.roomGetData(barberId)

        // LiveData'yı gözlemleme
        observeLiveData()

        // DatePicker açılması ve tarih seçilmesi
        binding.datePickerButton.text = dateFormatter.format(calendar.time) // Buton başlangıçta bugünün tarihi ile gösterilir

        binding.datePickerButton.setOnClickListener {
            openDatePicker()
        }

        // Randevu alma işlemi
        binding.bookAppointmentButton.setOnClickListener {
            val selectedDate = binding.datePickerButton.text.toString()
            val selectedTime = binding.timePickerButton.text.toString()

            // Randevu sadece tarih ve saat seçildiyse alınır
            if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
                bookAppointment(selectedDate, selectedTime)
            } else {
                Toast.makeText(this, "Lütfen geçerli bir tarih ve saat seçin", Toast.LENGTH_LONG).show()
            }
        }

        // TimePicker açılması ve saat seçilmesi
        binding.timePickerButton.setOnClickListener {
            openTimePicker()
        }
    }

    private fun openDatePicker() {
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                val formattedDate = dateFormatter.format(selectedDate.time)

                // Geçmiş tarih veya dolu tarihler kontrolü
                if (selectedDate.before(Calendar.getInstance())) {
                    Toast.makeText(this, "Geçmiş tarih seçemezsiniz", Toast.LENGTH_SHORT).show()
                } else if (bookedDates.contains(formattedDate)) {
                    Toast.makeText(this, "Bu tarih dolu", Toast.LENGTH_SHORT).show()
                } else {
                    binding.datePickerButton.text = formattedDate
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Geçmiş tarihlerin seçimini engelle
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        datePicker.show()
    }

    private fun openTimePicker() {
        val timePicker = TimePickerDialog(
            this,
            { _, hourOfDay, _ ->
                if (hourOfDay in availableHours) {
                    binding.timePickerButton.text = String.format("%02d:00", hourOfDay)
                } else {
                    Toast.makeText(this, "Sadece 9:00 - 18:00 saatleri arasında randevu alınabilir", Toast.LENGTH_SHORT).show()
                }
            },
            9, 0, true
        )
        timePicker.show()
    }

    private fun observeLiveData() {
        viewModel.barberLiveData.observe(this) { barber ->
            binding.barberName.text = barber.barberName
            binding.localeName.text = barber.localeName
            binding.barberImage.dowloandImage(barber.barberImage, makePlaceHolder(this))
        }
    }

    private fun bookAppointment(selectedDate: String, selectedTime: String) {
        // API'ye ya da veri tabanına randevu bilgilerini gönder
        Toast.makeText(this, "Randevu alındı: $selectedDate - $selectedTime", Toast.LENGTH_LONG).show()
    }
}
