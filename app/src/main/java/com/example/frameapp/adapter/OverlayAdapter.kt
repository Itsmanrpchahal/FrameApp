package com.example.frameapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.frameapp.R
import com.example.frameapp.models.OverlayModel

class OverlayAdapter(var context: Context,var overlayList : OverlayModel):RecyclerView.Adapter<OverlayAdapter.ViewHolder>() {
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverlayAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.overlay_item, parent, false)
        return OverlayAdapter.ViewHolder(itemView)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: OverlayAdapter.ViewHolder, position: Int) {


        if (selectedPosition==position)
        {
            holder.parentRelative.setBackgroundResource(R.color.pink)
            holder.overlayTV.setTextColor(Color.parseColor("#FFFFFF"))
            holder.overlayTV.text = overlayList.get(position).title
        } else {
            holder.parentRelative.setBackgroundResource(R.color.gray)
            holder.overlayTV.setTextColor(Color.parseColor("#000000"))
            holder.overlayTV.text = overlayList.get(position).title
        }

        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0)
                notifyItemChanged(selectedPosition);
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(selectedPosition);
        }

    }

    override fun getItemCount(): Int {
        return overlayList.size;
    }

    class ViewHolder(itemView: View)  : RecyclerView.ViewHolder(itemView){
           var parentRelative : RelativeLayout = itemView.findViewById(R.id.layout)
           var overlayTV : TextView = itemView.findViewById(R.id.overlayTV)
    }
}