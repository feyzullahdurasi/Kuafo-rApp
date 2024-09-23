package com.example.kuafrapp.View.login

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.kuafrapp.R
import com.example.kuafrapp.View.InfoActivity
import com.example.kuafrapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.fullNameEt.onFocusChangeListener = this
        binding.eMailEt.onFocusChangeListener = this
        binding.passwordEt.onFocusChangeListener = this
        binding.rePasswordEt.onFocusChangeListener = this
        binding.registerButton.setOnClickListener(this)
        binding.passwordEt.addTextChangedListener(this)
        binding.rePasswordEt.addTextChangedListener(this)
    }

    private fun observeViewModel() {
        viewModel.fullNameError.observe(this) { error ->
            binding.fullNameTitle.error = error
            binding.fullNameTitle.isErrorEnabled = error != null
        }

        viewModel.emailError.observe(this) { error ->
            binding.eMailTitle.error = error
            binding.eMailTitle.isErrorEnabled = error != null
        }

        viewModel.passwordError.observe(this) { error ->
            binding.passwordTitle.error = error
            binding.passwordTitle.isErrorEnabled = error != null
        }

        viewModel.rePasswordError.observe(this) { error ->
            binding.rePasswordTitle.error = error
            binding.rePasswordTitle.isErrorEnabled = error != null
        }

        viewModel.passwordMatch.observe(this) { match ->
            if (match) {
                binding.rePasswordTitle.apply {
                    setStartIconDrawable(R.drawable.baseline_check_circle_24)
                    setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                }
            } else {
                binding.rePasswordTitle.setStartIconDrawable(null)
            }
        }

        viewModel.navigateToInfo.observe(this) { shouldNavigate ->
            if (shouldNavigate) {
                navigateToInfoActivity()
            }
        }
    }

    private fun navigateToInfoActivity() {
        val intent = Intent(this, InfoActivity::class.java)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.registerButton -> viewModel.onRegisterClick(
                binding.fullNameEt.text.toString(),
                binding.eMailEt.text.toString(),
                binding.passwordEt.text.toString(),
                binding.rePasswordEt.text.toString()
            )
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        when (v?.id) {
            R.id.fullNameEt -> {
                if (!hasFocus) {
                    viewModel.validateFullName(binding.fullNameEt.text.toString())
                }
            }
            R.id.eMailEt -> {
                if (!hasFocus) {
                    viewModel.validateEmail(binding.eMailEt.text.toString())
                }
            }
            R.id.passwordEt -> {
                if (!hasFocus) {
                    viewModel.validatePassword(binding.passwordEt.text.toString())
                    viewModel.validatePasswordMatch(binding.passwordEt.text.toString(), binding.rePasswordEt.text.toString())
                }
            }
            R.id.rePasswordEt -> {
                if (!hasFocus) {
                    viewModel.validateRePassword(binding.rePasswordEt.text.toString())
                    viewModel.validatePasswordMatch(binding.passwordEt.text.toString(), binding.rePasswordEt.text.toString())
                }
            }
        }
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_UP) {
            when (v?.id) {
                R.id.rePasswordEt -> {
                    viewModel.onRegisterClick(
                        binding.fullNameEt.text.toString(),
                        binding.eMailEt.text.toString(),
                        binding.passwordEt.text.toString(),
                        binding.rePasswordEt.text.toString()
                    )
                    return true
                }
            }
        }
        return false
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // İsteğe bağlı işlemler için eklenebilir
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val password = binding.passwordEt.text.toString()
        val rePassword = binding.rePasswordEt.text.toString()

        if (password.isNotEmpty() && rePassword.isNotEmpty()) {
            if (password == rePassword) {
                binding.rePasswordTitle.apply {
                    setStartIconDrawable(R.drawable.baseline_check_circle_24)
                    setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                }
            } else {
                binding.rePasswordTitle.setStartIconDrawable(null)
            }
        } else {
            binding.rePasswordTitle.setStartIconDrawable(null)
        }
    }

    override fun afterTextChanged(s: Editable?) {
        // İsteğe bağlı işlemler için eklenebilir
    }
}
