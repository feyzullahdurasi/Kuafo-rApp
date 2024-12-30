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
                // Servis adı
                tvServiceName.text = service.serviceType

                // Servis özellikleri
                val features = service.serviceFeature.joinToString(", ") {
                    "${it.name} (${it.price} TL)"
                }
                tvServiceFeatures.text = features

                // Toplam fiyat
                val totalPrice = service.serviceFeature.sumOf { it.price }
                tvServicePrice.text = String.format("%d TL", totalPrice)

                // Tıklama işlemi
                root.setOnClickListener { onItemClick(service) }
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