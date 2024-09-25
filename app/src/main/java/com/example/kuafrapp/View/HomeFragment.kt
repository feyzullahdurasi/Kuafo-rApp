package com.example.kuafrapp.View

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

        // Argümanlardan hizmet tipini alıyoruz
        serviceType = arguments?.getString("SERVICE_TYPE")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // Hizmet türüne göre verileri yenile
        serviceType?.let {
            viewModel.refreshData(it)
        }

        binding.barberRecycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.barberRecycleView.adapter = barberRecyclerAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.barberRecycleView.visibility = View.GONE
            binding.barberErrorMessage.visibility = View.GONE
            binding.barberLoading.visibility = View.VISIBLE
            viewModel.refreshDataFromInternet(serviceType)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.barberLiveData.observe(viewLifecycleOwner) {
            barberRecyclerAdapter.updateBarberList(it)
            binding.barberRecycleView.visibility = View.VISIBLE
        }

        viewModel.barberError.observe(viewLifecycleOwner) {
            if (it) {
                binding.barberErrorMessage.visibility = View.VISIBLE
                binding.barberRecycleView.visibility = View.GONE
            } else {
                binding.barberErrorMessage.visibility = View.GONE
            }
        }

        viewModel.barberLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.barberLoading.visibility = View.VISIBLE
                binding.barberRecycleView.visibility = View.GONE
                binding.barberErrorMessage.visibility = View.GONE
            } else {
                binding.barberLoading.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
