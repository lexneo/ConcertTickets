package com.lexneoapps.concerttickets.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lexneoapps.concerttickets.data.local.models.Discounted
import com.lexneoapps.concerttickets.databinding.ForYouItemBinding
import com.lexneoapps.concerttickets.utils.Constants.BASE_URL
import com.lexneoapps.concerttickets.utils.Constants.BASE_URL_IMAGE

class ForYouAdapter() :
    ListAdapter<Discounted, ForYouAdapter.MyViewHolder>(ForYouDiffCallback()) {

    class MyViewHolder(val binding: ForYouItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(discounted: Discounted) {
            binding.apply {
                Glide.with(this.root).load(BASE_URL_IMAGE+discounted.photo).into(image)
                cityNameTv.text = discounted.place
                dateTv.text = discounted.dateShort
                ticketsLeftTv.text = "only ${discounted.quantity} left for ${discounted.price}"
                performerNameTv.text = discounted.name
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ForYouItemBinding.inflate
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
private class ForYouDiffCallback : DiffUtil.ItemCallback<Discounted>() {
    override fun areItemsTheSame(oldItem: Discounted, newItem: Discounted): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Discounted, newItem: Discounted): Boolean {
        return oldItem == newItem
    }
}
