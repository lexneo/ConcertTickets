package com.lexneoapps.concerttickets.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lexneoapps.concerttickets.data.local.models.Discounted
import com.lexneoapps.concerttickets.databinding.ExpiredItemBinding
import com.lexneoapps.concerttickets.utils.Constants.BASE_URL_IMAGE

class ExpiredAdapter() :
    ListAdapter<Discounted, ExpiredAdapter.MyViewHolder>(ExpiredCallBack()) {

    class MyViewHolder(val binding: ExpiredItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(discounted: Discounted) {
            binding.apply {
                Glide.with(this.root).load(BASE_URL_IMAGE+discounted.photo).into(image)
                cityNameTv.text = discounted.place
                performerNameTv.text = discounted.name
//                val percentage = 100*(discounted.price -discounted.discount)/ discounted.price)
                discountPercentageTv.text = "-${discounted.percentage}%"
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ExpiredItemBinding.inflate
                (LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentDiscounted = getItem(position)

        holder.bind(currentDiscounted)
        holder.binding.root.setOnClickListener {
            onItemClickListener?.let { click ->
                click(currentDiscounted)
            }
        }
    }


    private var onItemClickListener: ((Discounted) -> Unit)? = null

    fun setOnItemClickListener(onItemClick: (Discounted) -> Unit) {
        this.onItemClickListener = onItemClick
    }
}
private class ExpiredCallBack : DiffUtil.ItemCallback<Discounted>() {
    override fun areItemsTheSame(oldItem: Discounted, newItem: Discounted): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Discounted, newItem: Discounted): Boolean {
        return oldItem == newItem
    }
}
