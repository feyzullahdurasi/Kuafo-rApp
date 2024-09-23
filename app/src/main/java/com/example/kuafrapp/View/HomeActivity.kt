package com.example.kuafrapp.View

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuafrapp.adapter.BarberRecyclerAdapter
import com.example.kuafrapp.databinding.ActivityHomeBinding
import com.example.kuafrapp.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel
    private val barberRecyclerAdapter = BarberRecyclerAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding'i başlat
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel'i başlat
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.refreshData()

        // RecyclerView ayarlarını yap
        binding.barberRecycleView.layoutManager = LinearLayoutManager(this)
        binding.barberRecycleView.adapter = barberRecyclerAdapter

        // SwipeRefreshLayout için listener ekle
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.barberRecycleView.visibility = View.GONE
            binding.barberErrorMessage.visibility = View.GONE
            binding.barberLoading.visibility = View.VISIBLE
            viewModel.refreshDataFromInternet()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        // LiveData'ları gözlemle
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.barberLiveData.observe(this) {
            barberRecyclerAdapter.updateBarberList(it)
            binding.barberRecycleView.visibility = View.VISIBLE
        }

        viewModel.barberError.observe(this) {
            if (it) {
                binding.barberErrorMessage.visibility = View.VISIBLE
                binding.barberRecycleView.visibility = View.GONE
            } else {
                binding.barberErrorMessage.visibility = View.GONE
            }
        }

        viewModel.barberLoading.observe(this) {
            if (it) {
                binding.barberLoading.visibility = View.VISIBLE
                binding.barberRecycleView.visibility = View.GONE
                binding.barberErrorMessage.visibility = View.GONE
            } else {
                binding.barberLoading.visibility = View.GONE
            }
        }
    }
}
