package com.lexneoapps.concerttickets.ui.admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lexneoapps.concerttickets.data.local.models.Discounted
import com.lexneoapps.concerttickets.databinding.AdminFragmentItemBinding
import com.lexneoapps.concerttickets.utils.Constants

class AdminDiscountAdapter() :
    ListAdapter<Discounted, AdminDiscountAdapter.MyViewHolder>(AdminDiscountAdapterDiffCallback()) {

    /*interface AdminDiscountClickInterface(){
        fun onItemClick() {
            
        }
    }*/

    class MyViewHolder(val binding: AdminFragmentItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(discounted: Discounted) {
            binding.apply {
                Glide.with(this.root).load(Constants.BASE_URL_IMAGE +discounted.photo).into(image)
                cityTv.text = discounted.place
                timeTv.text = discounted.time
                upcomingCvDateTextview1.text = discounted.day
                upcomingCvMonthTextview1.text = discounted.month
                upcomingCvYearTextview1.text = discounted.year
                ticketsLeftTv.text = "only ${discounted.quantity} left for ${discounted.price}"
                discountPercentageTv.text = "-${discounted.percentage}%"
                discountDifferenceTv.text = discounted.discountDifference.toString()
                performerNameTv.text = discounted.name
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
        val currentDiscounted = getItem(position)

        holder.bind(currentDiscounted)
        holder.binding.editTv.setOnClickListener {
            onEditClickListener?.let { click ->
                click(currentDiscounted)
            }
        }
        holder.binding.deleteTv.setOnClickListener {
            onDeleteClickListener?.let {
                it(currentDiscounted)
            }
        }


    }


    private var onDeleteClickListener: ((Discounted) -> Unit)? = null
    private var onEditClickListener: ((Discounted) -> Unit)? = null


    fun setOnEditClickListener(onItemClick: (Discounted) -> Unit) {
        this.onEditClickListener = onItemClick
    }

    fun setOnDeleteClickListener(onItemClick: (Discounted) -> Unit) {
        this.onDeleteClickListener = onItemClick
    }
}
private class AdminDiscountAdapterDiffCallback : DiffUtil.ItemCallback<Discounted>() {
    override fun areItemsTheSame(oldItem: Discounted, newItem: Discounted): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Discounted, newItem: Discounted): Boolean {
        return oldItem == newItem
    }
}