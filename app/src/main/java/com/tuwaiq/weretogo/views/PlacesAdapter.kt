package com.tuwaiq.weretogo.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tuwaiq.weretogo.databinding.ItemEmptyViewBinding
import com.tuwaiq.weretogo.databinding.ItemPlaceBinding
import com.tuwaiq.weretogo.network.model.Place

class PlacesAdapter(
    var onClick: (place: Place) -> Unit,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: MutableList<Place> = ArrayList()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }

    companion object {
        const val VIEW_TYPE_EMPTY = 0
        const val VIEW_TYPE_NORMAL = 1
    }

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (data.isEmpty() || data.size == 0) VIEW_TYPE_EMPTY
        else VIEW_TYPE_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_NORMAL -> {
                val binding = ItemPlaceBinding.inflate(inflater, parent, false)
                ViewHolder(binding)
            }
            VIEW_TYPE_EMPTY -> {
                val binding = ItemEmptyViewBinding.inflate(inflater, parent, false)
                EmptyViewHolder(binding)
            }

            else -> {
                val binding = ItemEmptyViewBinding.inflate(inflater, parent, false)
                EmptyViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) holder.onBind(position)
        else if (holder is EmptyViewHolder) holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return if (data.size == 0) 1
        else data.size
    }

    inner class ViewHolder(val binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(position: Int) {


            val bean = data[position]
            binding.placeId.text = bean.id
            binding.tvTitle.text = bean.title
            binding.tvDescription.text = bean.description
            binding.rating.rating = bean.rating?.toFloat()!!

            Glide.with(itemView.context)
                .load(bean.imageUrl)
                .centerCrop()
                .into(binding.ivPlace)

            binding.root.setOnClickListener {
                onClick.invoke(bean)
            }

        }
    }

    inner class EmptyViewHolder(val binding: ItemEmptyViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(position: Int) {


        }


    }


}