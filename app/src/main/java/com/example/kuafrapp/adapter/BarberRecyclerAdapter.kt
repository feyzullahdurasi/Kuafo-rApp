package com.example.kuafrapp.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kuafrapp.databinding.BarberRecyclerRowBinding
import com.example.kuafrapp.model.Business

class BarberRecyclerAdapter(
    private val onBarberClick: (Business) -> Unit
) : ListAdapter<Business, BarberRecyclerAdapter.BarberViewHolder>(BarberDiffCallback()) {

    class BarberViewHolder(
        private val binding: BarberRecyclerRowBinding,
        private val onBarberClick: (Business) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(business: Business) {
            binding.apply {
                barberName.text = business.name
                localeName.text = business.address
                //ratingBar.rating = business.rating ?: 0f

                // Glide ile image loading
                Glide.with(imageView.context)
                    .load(business.image)
                    //.placeholder(R.drawable.placeholder_image)
                    //.error(R.drawable.error_image)
                    .into(imageView)

                root.setOnClickListener { 
                    onBarberClick(business)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarberViewHolder {
        return BarberViewHolder(
            BarberRecyclerRowBinding.inflate(
                LayoutInflater.from(parent.context), 
                parent, 
                false
            ),
            onBarberClick
        )
    }

    override fun onBindViewHolder(holder: BarberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class BarberDiffCallback : DiffUtil.ItemCallback<Business>() {
        override fun areItemsTheSame(oldItem: Business, newItem: Business): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Business, newItem: Business): Boolean {
            return oldItem == newItem
        }
    }
}


