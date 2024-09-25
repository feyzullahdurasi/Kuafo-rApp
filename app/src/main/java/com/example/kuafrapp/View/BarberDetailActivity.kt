package com.example.kuafrapp.View

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuafrapp.adapter.CommentsAdapter
import com.example.kuafrapp.databinding.ActivityBarberDetailBinding
import com.example.kuafrapp.model.Comment
import com.example.kuafrapp.util.dowloandImage
import com.example.kuafrapp.util.makePlaceHolder
import com.example.kuafrapp.viewmodel.BarberDetailViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BarberDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBarberDetailBinding
    private lateinit var viewModel: BarberDetailViewModel
    private var barberId = 0
    private val calendar = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val bookedDates = listOf("15/10/2024", "16/10/2024") // Dolu olan tarihler örneği
    private val availableHours = (9..18).toList() // Günlük saat aralığı
    private val services = mapOf("Saç Kesimi" to 100, "Sakal Tıraşı" to 50, "Saç Boyama" to 200)
    private var selectedServices = mutableListOf<String>()
    private var totalPrice = 0

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

        // Hizmet Seçimi
        setupServiceCheckboxes()

        // Tarih ve Saat Seçimi
        setupDatePickerAndTimePicker()

        // Randevu alma işlemi
        binding.bookAppointmentButton.setOnClickListener {
            bookAppointment()
        }

        // Yorum ekleme ve önceki yorumları gösterme
        setupCommentsSection()
    }

    private fun setupServiceCheckboxes() {
        val serviceContainer = binding.serviceContainer
        services.forEach { (serviceName, price) ->
            val checkBox = CheckBox(this).apply {
                text = "$serviceName - ${price}₺"
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedServices.add(serviceName)
                        totalPrice += price
                    } else {
                        selectedServices.remove(serviceName)
                        totalPrice -= price
                    }
                    binding.totalPriceText.text = "Toplam: ${totalPrice}₺"
                }
            }
            serviceContainer.addView(checkBox)
        }
    }

    private fun setupDatePickerAndTimePicker() {
        // DatePicker açılması ve tarih seçilmesi
        binding.datePickerButton.text = dateFormatter.format(calendar.time)

        binding.datePickerButton.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, month, dayOfMonth)
                    val formattedDate = dateFormatter.format(selectedDate.time)

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
            datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
            datePicker.show()
        }

        // TimePicker açılması ve saat seçilmesi
        binding.timePickerButton.setOnClickListener {
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
    }

    private fun setupCommentsSection() {
        // Yorumlar için RecyclerView ayarlama
        val commentsAdapter = CommentsAdapter(listOf(
            Comment("Ahmet", "Hizmet mükemmeldi!"),
            Comment("Mehmet", "Çok memnun kaldım."),
        ))
        binding.commentsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@BarberDetailActivity)
            adapter = commentsAdapter
        }

        // Yorum ekleme butonu
        binding.addCommentButton.setOnClickListener {
            val newComment = binding.commentEditText.text.toString()
            if (newComment.isNotEmpty()) {
                // Yorum eklenip listeye güncelleme yapılabilir
                commentsAdapter.addComment(Comment("Yeni Kullanıcı", newComment))
                binding.commentEditText.text.clear()
            } else {
                Toast.makeText(this, "Yorum boş olamaz", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeLiveData() {
        viewModel.barberLiveData.observe(this) { barber ->
            binding.barberName.text = barber.barberName
            binding.localeName.text = barber.localeName
            binding.barberImage.dowloandImage(barber.barberImage, makePlaceHolder(this))
        }
    }

    private fun bookAppointment() {
        val selectedDate = binding.datePickerButton.text.toString()
        val selectedTime = binding.timePickerButton.text.toString()

        if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty() && selectedServices.isNotEmpty()) {
            Toast.makeText(
                this,
                "Randevu alındı: $selectedDate - $selectedTime - ${selectedServices.joinToString(", ")} (${totalPrice}₺)",
                Toast.LENGTH_LONG
            ).show()
            // Randevu API'ye gönderilebilir
        } else {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_LONG).show()
        }
    }
}
