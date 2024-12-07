package com.example.kuafrapp.View.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuafrapp.adapter.ServicesAdapter
import com.example.kuafrapp.databinding.FragmentHomeBinding

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
    }

    private fun setupRecyclerView() {
        servicesAdapter = ServicesAdapter { service ->
            viewModel.selectService(service)
            // Detay sayfasına geçiş yapılacak
        }
        binding.servicesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = servicesAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.businessLiveData.observe(viewLifecycleOwner) { business ->
            business?.let {
                servicesAdapter.submitList(it.services)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.hasError.observe(viewLifecycleOwner) { hasError ->
            binding.errorTextView.visibility = if (hasError) View.VISIBLE else View.GONE
        }
    }

    private fun setupSearchAndFilterControls() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Arama işlevi eklenecek
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Anlık arama işlevi eklenecek
                return true
            }
        })
    }
}