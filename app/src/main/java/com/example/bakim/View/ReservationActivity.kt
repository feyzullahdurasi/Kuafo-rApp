package com.example.bakim.View

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bakim.databinding.ActivityReservationBinding

class ReservationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservationBinding
    private var totalPrice = 0
    private var selectedDate = ""
    private var selectedTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data from intent
        totalPrice = intent.getIntExtra("totalPrice", 0)
        selectedDate = intent.getStringExtra("selectedDate") ?: ""
        selectedTime = intent.getStringExtra("selectedTime") ?: ""

        // Set the selected date, time, and total price
        binding.reservationDateText.text = "Randevu Tarihi: $selectedDate"
        binding.reservationTimeText.text = "Randevu Saati: $selectedTime"
        binding.totalPriceText.text = "Toplam Ücret: ${totalPrice}₺"

        // Set up TextWatchers for formatting inputs
        setupCardNumberWatcher()
        setupExpirationDateWatcher()
        setupCVVWatcher()

        binding.payButton.setOnClickListener {
            processPayment()
        }
    }

    private fun setupCardNumberWatcher() {
        binding.cardNumberInput.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return

                val digitsOnly = s.toString().replace(" ", "")
                if (digitsOnly.length <= 16) {
                    isUpdating = true
                    binding.cardNumberInput.setText(digitsOnly.chunked(4).joinToString(" "))
                    binding.cardNumberInput.setSelection(binding.cardNumberInput.text.length)
                    isUpdating = false
                }
            }
        })
    }

    private fun setupExpirationDateWatcher() {
        binding.expirationDateInput.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return

                val digitsOnly = s.toString().replace("/", "")
                if (digitsOnly.length <= 4) {
                    isUpdating = true
                    if (digitsOnly.length > 2) {
                        binding.expirationDateInput.setText(digitsOnly.substring(0, 2) + "/" + digitsOnly.substring(2))
                    } else {
                        binding.expirationDateInput.setText(digitsOnly)
                    }
                    binding.expirationDateInput.setSelection(binding.expirationDateInput.text.length)
                    isUpdating = false
                }
            }
        })
    }

    private fun setupCVVWatcher() {
        binding.cvvInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length > 3) {
                    binding.cvvInput.setText(s.toString().substring(0, 3))
                    binding.cvvInput.setSelection(binding.cvvInput.text.length)
                }
            }
        })
    }

    private fun processPayment() {
        val cardNumber = binding.cardNumberInput.text.toString()
        val expirationDate = binding.expirationDateInput.text.toString()
        val cvv = binding.cvvInput.text.toString()
        val cardHolderName = binding.cardHolderNameInput.text.toString()

        if (cardNumber.length == 19 && expirationDate.length == 5 && cvv.length == 3 && cardHolderName.isNotEmpty()) {
            Toast.makeText(this, "Payment processed for $totalPrice₺ on $selectedDate at $selectedTime", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Lütfen tüm ödeme bilgilerini doğru girin", Toast.LENGTH_LONG).show()
        }
    }

    // Extension function to chunk a string
    private fun String.chunked(size: Int): List<String> {
        return this.chunked(size)
    }
}

