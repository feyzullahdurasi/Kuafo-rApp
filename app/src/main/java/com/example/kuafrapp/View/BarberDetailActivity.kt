package com.example.kuafrapp.View

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.kuafrapp.adapter.CommentsAdapter
import com.example.kuafrapp.databinding.ActivityBarberDetailBinding
import com.example.kuafrapp.message.ReminderWorker
import com.example.kuafrapp.model.Comment
import com.example.kuafrapp.util.dowloandImage
import com.example.kuafrapp.util.makePlaceHolder
import com.example.kuafrapp.viewmodel.BarberDetailViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

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

        // Paylaş butonu için tıklama işlemi
        binding.shareButton.setOnClickListener {
            shareBarberDetails()
        }
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

        // TimePicker açılması ve sadece 09:00 - 18:00 saatleri arasında seçim yapılabilmesi
        binding.timePickerButton.setOnClickListener {
            // Saat aralığını 9 ile 18 arasında liste olarak belirleyelim
            val availableHours = (9..18).map { String.format("%02d:00", it) }.toTypedArray()

            // Saatleri seçmek için bir AlertDialog kullanıyoruz
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Saat Seç")
            builder.setItems(availableHours) { _, which ->
                // Kullanıcının seçtiği saati butona yazdırıyoruz
                binding.timePickerButton.text = availableHours[which]
            }

            // Dialogu göster
            builder.show()
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
            binding.barberName.text = barber?.barberName ?: "empty"
            binding.localeName.text = barber?.localeName ?: "empty"
            binding.barberImage.dowloandImage(barber?.barberImage, makePlaceHolder(this))
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

            // Randevu için hatırlatıcı işini zamanlama
            scheduleReminder(selectedDate, selectedTime)

            // Randevu API'ye gönderilebilir
        } else {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_LONG).show()
        }
    }

    private fun scheduleReminder(date: String, time: String) {
        // Randevu saatinden 2 saat önce hatırlatma yapmak için gecikme hesaplama
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val appointmentDateTime = formatter.parse("$date $time") ?: return
        val reminderTime = appointmentDateTime.time - (2 * 60 * 60 * 1000) // 2 saat öncesi

        // WorkRequest oluşturma
        val data = Data.Builder()
            .putString("message", "Randevunuz 2 saat içinde!") // Eğer ek veri gerekiyorsa
            .build()

        val reminderWorkRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(reminderTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS) // Gecikme ayarı
            .setInputData(data)
            .build()

        // WorkManager ile görevi başlatma
        WorkManager.getInstance(this).enqueue(reminderWorkRequest)
    }

    // Paylaşım işlemini gerçekleştiren fonksiyon
    private fun shareBarberDetails() {
        val barberName = binding.barberName.text.toString()
        val localeName = binding.localeName.text.toString()

        val shareText = "Kuaför Detayları:\nKuaför: $barberName\nYer: $localeName\nToplam Ücret: ${totalPrice}₺"

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }

        startActivity(Intent.createChooser(shareIntent, "Paylaşmak için uygulama seçin"))
    }
}
