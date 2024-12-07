package com.example.kuafrapp.service

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.kuafrapp.R

sealed class APIError(val userErrorMessage: String) {
    object InvalidURL : APIError("Geçersiz bir URL ile karşılaşıldı. Lütfen URL'yi kontrol edin.")
    object InvalidResponse : APIError("Sunucu yanıtında bir hata oluştu.")
    object UnableToComplete : APIError("İnternette bir hata oluştu. Lütfen daha sonra tekrar deneyin.")
    object InvalidData : APIError("Geçersiz veri.")
}

class UserErrorDialog(private val context: Context) {
    fun showErrorDialog(error: APIError, onDismiss: () -> Unit) {
        // Inflate custom layout
        val inflater = LayoutInflater.from(context)
        val dialogView: View = inflater.inflate(R.layout.dialog_user_error, null)

        // Bind UI components
        val errorTitle = dialogView.findViewById<TextView>(R.id.errorTitle)
        val errorMessage = dialogView.findViewById<TextView>(R.id.errorMessage)
        val dismissButton = dialogView.findViewById<Button>(R.id.dismissButton)

        // Set error message and title
        errorTitle.text = "Hata"
        errorMessage.text = error.userErrorMessage

        // Create dialog
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // Dismiss button action
        dismissButton.setOnClickListener {
            dialog.dismiss()
            onDismiss()
        }

        dialog.show()
    }
}
