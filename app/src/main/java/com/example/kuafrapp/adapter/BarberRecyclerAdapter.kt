package com.example.kuafrapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.kuafrapp.View.BarberDetailActivity
import com.example.kuafrapp.View.HomeFragmentDirections
import com.example.kuafrapp.databinding.BarberRecyclerRowBinding
import com.example.kuafrapp.model.Barber
import com.example.kuafrapp.util.dowloandImage
import com.example.kuafrapp.util.makePlaceHolder

class BarberRecyclerAdapter(val barberList: ArrayList<Barber>) : RecyclerView.Adapter<BarberRecyclerAdapter.BarberViewHolder>() {

    class BarberViewHolder(val binding: BarberRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarberViewHolder {
        val binding = BarberRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BarberViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return barberList.size
    }

    fun updateBarberList(newBarberList: List<Barber>) {
        barberList.clear()
        barberList.addAll(newBarberList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BarberViewHolder, position: Int) {
        val barber = barberList[position]
        holder.binding.barberName.text = barber.barberName
        holder.binding.localeName.text = barber.localeName

        holder.itemView.setOnClickListener {
            // Activity'ye yönlendirme yap
            val intent = Intent(holder.itemView.context, BarberDetailActivity::class.java)
            intent.putExtra("barberId", barber.uuid)
            holder.itemView.context.startActivity(intent)
        }

        holder.binding.imageView.dowloandImage(barberList[position].barberImage, makePlaceHolder(holder.itemView.context))
    }

}


