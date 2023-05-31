package com.example.codingchallenge


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.codingchallenge.data.Item

class ItemsElementAdapter: RecyclerView.Adapter<ItemsElementAdapter.ItemElementViewHolder>() {

    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemElementViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_element, parent, false)
        return ItemElementViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ItemElementViewHolder, position: Int) {
        holder.view.findViewById<TextView>(R.id.itemElementName).text = items[position].name
        holder.view.findViewById<LinearLayout>(R.id.itemElementCard).setBackgroundColor(Color.GRAY) //items[position].colorCode
        holder.view.findViewById<ImageView>(R.id.itemElementImage).setImageURI(items[position].image)
    }

    class ItemElementViewHolder(val view: View): RecyclerView.ViewHolder(view)
}