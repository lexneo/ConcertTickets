package com.lexneoapps.concerttickets.ui.admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lexneoapps.concerttickets.data.local.models.Discounted
import com.lexneoapps.concerttickets.data.local.models.NonDiscounted
import com.lexneoapps.concerttickets.databinding.AdminFragmentItemBinding
import com.lexneoapps.concerttickets.utils.Constants

class AdminNonDiscountAdapter() :
    ListAdapter<NonDiscounted, AdminNonDiscountAdapter.MyViewHolder>(AdminNonDiscountAdapterDiffCallback()) {

    class MyViewHolder(val binding: AdminFragmentItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(nonDiscounted: NonDiscounted) {
            binding.apply {
                Glide.with(this.root).load(Constants.BASE_URL_IMAGE +nonDiscounted.photo).into(image)
                cityTv.text = nonDiscounted.place
                timeTv.text = nonDiscounted.time
                upcomingCvDateTextview1.text = nonDiscounted.day
                upcomingCvMonthTextview1.text = nonDiscounted.month
                upcomingCvYearTextview1.text = nonDiscounted.year
                ticketsLeftTv.text = "only ${nonDiscounted.quantity} left for ${nonDiscounted.price}"
                discountDifferenceTv.text = nonDiscounted.price.toString()
                performerNameTv.text = nonDiscounted.name
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            AdminFragmentItemBinding.inflate
                (LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentNonDiscounted = getItem(position)

        holder.bind(currentNonDiscounted)
        holder.binding.editTv.setOnClickListener {
            onEditClickListener?.let { click ->
                click(currentNonDiscounted)
            }
        }
        holder.binding.deleteTv.setOnClickListener {
            onDeleteClickListener?.let {
                it(currentNonDiscounted)
            }
        }
    }


    private var onDeleteClickListener: ((NonDiscounted) -> Unit)? = null
    private var onEditClickListener: ((NonDiscounted) -> Unit)? = null


    fun setOnEditClickListener(onItemClick: (NonDiscounted) -> Unit) {
        this.onEditClickListener = onItemClick
    }

    fun setOnDeleteClickListener(onItemClick: (NonDiscounted) -> Unit) {
        this.onDeleteClickListener = onItemClick
    }
}
private class AdminNonDiscountAdapterDiffCallback : DiffUtil.ItemCallback<NonDiscounted>() {
    override fun areItemsTheSame(oldItem: NonDiscounted, newItem: NonDiscounted): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NonDiscounted, newItem: NonDiscounted): Boolean {
        return oldItem == newItem
    }
}