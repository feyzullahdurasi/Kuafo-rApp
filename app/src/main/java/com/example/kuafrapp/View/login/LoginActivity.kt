package com.example.kuafrapp.View.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.kuafrapp.R
import com.example.kuafrapp.View.InfoActivity
import com.example.kuafrapp.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    // Google SignIn Client
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Layout binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Google SignIn setup
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.loginWithGoogleButton.setOnClickListener(this)
        binding.loginButton.setOnClickListener(this)
        binding.registerButton.setOnClickListener(this)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        // Set focus and click listeners
        binding.eMailEt.onFocusChangeListener = this
        binding.passwordEt.onFocusChangeListener = this
        binding.loginButton.setOnClickListener(this)
        binding.passwordEt.addTextChangedListener(this)
    }

    private fun observeViewModel() {
        // Observe ViewModel data
        viewModel.emailError.observe(this) { error ->
            binding.eMailTitle.error = error
            binding.eMailTitle.isErrorEnabled = error != null
        }

        viewModel.passwordError.observe(this) { error ->
            binding.passwordTitle.error = error
            binding.passwordTitle.isErrorEnabled = error != null
        }

        viewModel.loginSuccess.observe(this) { success ->
            if (success) {
                navigateToHomeActivity()
            } else {
                Log.d("LoginActivity", "Login failed")
            }
        }
    }

    private fun navigateToHomeActivity() {
        val infoActivity = InfoActivity()
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.loginButton ->
                /*viewModel.onLoginClick(
                binding.eMailEt.text.toString(),
                binding.passwordEt.text.toString()
            )*/ {
                val intent = Intent(this, InfoActivity::class.java)
                startActivity(intent)
            }
            R.id.registerButton -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
            R.id.loginWithGoogleButton -> {
                signInWithGoogle()
            }
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Google Sign-in success
            Log.d("LoginActivity", "signInWithGoogle:success")
            // Perform further actions like fetching account details or navigating to another activity
        } catch (e: ApiException) {
            Log.w("LoginActivity", "signInWithGoogle:failure", e)
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (v != null) {
            when (v.id) {
                R.id.eMailEt -> {
                    if (!hasFocus) {
                        viewModel.validateEmail(binding.eMailEt.text.toString())
                    }
                }
                R.id.passwordEt -> {
                    if (!hasFocus) {
                        viewModel.validatePassword(binding.passwordEt.text.toString())
                    }
                }
            }
        }
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_UP) {
            when (v?.id) {
                R.id.passwordEt -> {
                    viewModel.onLoginClick(
                        binding.eMailEt.text.toString(),
                        binding.passwordEt.text.toString()
                    )
                    return true
                }
            }
            submitForm()
        }
        return false
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Kullanılmadı
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // Kullanılmadı
    }

    override fun afterTextChanged(s: Editable?) {
        // Kullanılmadı
    }

    private fun submitForm() {
        if (viewModel.validateEmail(binding.eMailEt.text.toString()) && viewModel.validatePassword(binding.passwordEt.text.toString())) {
            viewModel.onLoginClick(binding.eMailEt.text.toString(), binding.passwordEt.text.toString())
        }
    }

    companion object {
        const val RC_SIGN_IN = 9001
    }
}
