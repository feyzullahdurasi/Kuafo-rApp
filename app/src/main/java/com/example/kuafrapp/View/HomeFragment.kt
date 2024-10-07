package com.example.kuafrapp.View

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuafrapp.adapter.BarberRecyclerAdapter
import com.example.kuafrapp.databinding.FragmentHomeBinding
import com.example.kuafrapp.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private val barberRecyclerAdapter = BarberRecyclerAdapter(arrayListOf())
    private var serviceType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        serviceType = arguments?.getString("SERVICE_TYPE")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        serviceType?.let {
            viewModel.refreshData(it)
        }

        setupViews()
        setupRecyclerView()
        observeLiveData()
    }

    private fun setupViews() {
        binding.filterButton.setOnClickListener {
            // Handle filter view logic if needed
        }

        binding.searchButton.setOnClickListener {
            binding.filterBarLayout.visibility = View.GONE
            binding.searchViewLayout.visibility = View.VISIBLE
        }

        binding.closeSearchView.setOnClickListener {
            binding.filterBarLayout.visibility = View.VISIBLE
            binding.searchViewLayout.visibility = View.GONE
        }

        binding.shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/feyzullahdurasi"))
            startActivity(intent)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    private fun setupRecyclerView() {
        binding.barberRecycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.barberRecycleView.adapter = barberRecyclerAdapter
    }

    private fun refreshData() {
        binding.barberRecycleView.visibility = View.GONE
        binding.barberErrorMessage.visibility = View.GONE
        binding.barberLoading.visibility = View.VISIBLE
        viewModel.refreshDataFromInternet(serviceType)
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun observeLiveData() {
        viewModel.barberLiveData.observe(viewLifecycleOwner) {
            barberRecyclerAdapter.updateBarberList(it)
            binding.barberRecycleView.visibility = View.VISIBLE
        }

        viewModel.barberError.observe(viewLifecycleOwner) {
            binding.barberErrorMessage.visibility = if (it) View.VISIBLE else View.GONE
            binding.barberRecycleView.visibility = if (it) View.GONE else View.VISIBLE
        }

        viewModel.barberLoading.observe(viewLifecycleOwner) {
            binding.barberLoading.visibility = if (it) View.VISIBLE else View.GONE
            if (it) {
                binding.barberRecycleView.visibility = View.GONE
                binding.barberErrorMessage.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}