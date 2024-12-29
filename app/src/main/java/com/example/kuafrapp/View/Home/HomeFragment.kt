package com.example.kuafrapp.View.Home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuafrapp.View.ServiceDetail.ServiceDetailActivity
import com.example.kuafrapp.adapter.ServicesAdapter
import com.example.kuafrapp.databinding.FragmentHomeBinding
import com.example.kuafrapp.service.APIResult

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var servicesAdapter: ServicesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        setupSearchAndFilterControls()
        
        // Servis verilerini yükle
        viewModel.loadBusinesses()
    }

    private fun setupRecyclerView() {
        servicesAdapter = ServicesAdapter { service ->
            // Detay sayfasına yönlendirme
            val intent = Intent(requireContext(), ServiceDetailActivity::class.java).apply {
                putExtra("serviceId", service.id)
                putExtra("businessId", service.businessId)
            }
            startActivity(intent)
        }
        
        binding.servicesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = servicesAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.businesses.observe(viewLifecycleOwner) { result ->
            when (result) {
                is APIResult.Success -> {
                    binding.progressBar.visibility = View.GONE
                    servicesAdapter.submitList(result.data.flatMap { it.services })
                }
                is APIResult.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.errorTextView.visibility = View.VISIBLE
                    binding.errorTextView.text = result.error.userErrorMessage
                }
                is APIResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupSearchAndFilterControls() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchServices(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.searchServices(it) }
                return true
            }
        })
    }
}