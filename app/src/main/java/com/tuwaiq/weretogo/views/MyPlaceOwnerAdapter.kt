package com.tuwaiq.weretogo.viewsimport

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tuwaiq.weretogo.R
import com.tuwaiq.weretogo.network.model.Place

class MyPlaceOwnerAdapter(private val list: List<Place>,val context:Context) :
    RecyclerView.Adapter<MyPlaceOwnerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyPlaceOwnerAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_owner__place_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        Glide.with(context)
            .load(item.imageUrl)
            .centerCrop()
            .into(holder.imagePlace)

        holder.titlePlace.text = item.title
        holder.descriptionPlace.text = item.description
        holder.placeId.text = item.address
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagePlace:ImageView = itemView.findViewById(R.id.iv_image)
        val titlePlace : TextView = itemView.findViewById(R.id.tv_title)
        val descriptionPlace :TextView = itemView.findViewById(R.id.tv_description)
        val placeId : TextView = itemView.findViewById(R.id.place_id)
    }
}
