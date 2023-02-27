package com.example.frameapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.frameapp.R

class ImageListAdapter(private var context: Context, private var images: ArrayList<Uri?>?) : RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageListAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ImageListAdapter.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageListAdapter.ViewHolder, position: Int) {
        holder.imageName.text = images!![position].toString()
    }

    override fun getItemCount(): Int {
        return images!!.size;
    }

    class ViewHolder(itemView: View)  : RecyclerView.ViewHolder(itemView){
         var imageName : TextView = itemView.findViewById(R.id.imageName)
    }
}