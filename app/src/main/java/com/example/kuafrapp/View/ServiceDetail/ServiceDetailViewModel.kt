package com.example.kuafrapp.View.ServiceDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kuafrapp.model.Business
import com.example.kuafrapp.model.Service

class ServiceDetailViewModel(
    private val service: Service,
    private val business: Business
) : ViewModel() {
    private val _showAlert = MutableLiveData(false)
    val showAlert: LiveData<Boolean> = _showAlert

    private val _alertMessage = MutableLiveData("")
    val alertMessage: LiveData<String> = _alertMessage

    fun showAlert(message: String) {
        _alertMessage.value = message
        _showAlert.value = true
    }

    fun addUserComment(comment: String) {
        // Yorumu ekleme işlevi - gerekirse backend veya local storage ile entegre edilebilir
        // Şimdilik mock bir işlem yapacağız
        /*val newComment = Comment(
            username = "Current User",
            rating = 5,
            commentText = comment
        )*/
        // Business'a yeni yorumu ekleyebilirsiniz
    }
}