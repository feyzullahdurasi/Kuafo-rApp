package com.example.kuafrapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kuafrapp.databinding.ItemServiceBinding
import com.example.kuafrapp.model.Service

class ServicesAdapter(
    private val onItemClick: (Service) -> Unit
) : ListAdapter<Service, ServicesAdapter.ServiceViewHolder>(ServiceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = ItemServiceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ServiceViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ServiceViewHolder(
        private val binding: ItemServiceBinding,
        private val onItemClick: (Service) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(service: Service) {
            binding.apply {
                serviceName.text = service.serviceType
                serviceLocation.text = "Salon Adresi" // Business adresinden alınabilir
                serviceRating.text = "4.8" // Sabit rating, gerekirse dinamik yapılabilir

                root.setOnClickListener {
                    onItemClick(service)
                }
            }
        }
    }

    class ServiceDiffCallback : DiffUtil.ItemCallback<Service>() {
        override fun areItemsTheSame(oldItem: Service, newItem: Service): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Service, newItem: Service): Boolean {
            return oldItem == newItem
        }
    }
}