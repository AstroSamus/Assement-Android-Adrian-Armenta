package com.example.codingchallenge.adapter


import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.codingchallenge.ItemsMenuFragmentDirections
import com.example.codingchallenge.R
import com.example.codingchallenge.database.Item


class ItemElementAdapter (
    private val dataset : List<Item>
) : RecyclerView.Adapter<ItemElementAdapter.ItemElementViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemElementViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_element, parent, false)
        return ItemElementViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int{
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemElementViewHolder, position: Int) {
        holder.itemElementName.text = dataset[position].name
        holder.itemElementColorCode.setBackgroundColor(dataset[position].safeColorCode)
        //val uri = Uri.parse(dataset[position].image)
        holder.itemElementImage?.let{
            Glide.with(it)
                .load(dataset[position].image)
                .error(R.drawable.ic_delete)
                .into(it)
        }

        holder.view.setOnClickListener {
            val action = ItemsMenuFragmentDirections.actionItemsMenuFragmentToCreateItemsFragment(dataset[position])
            Navigation .findNavController(it).navigate(action)
        }
    }

    class ItemElementViewHolder( val view: View): RecyclerView.ViewHolder(view) {
        val itemElementName: TextView = view.findViewById(R.id.itemElementName)
        val itemElementColorCode: LinearLayout = view.findViewById(R.id.itemElementCard)
        val itemElementImage: ImageView = view.findViewById(R.id.itemElementImage)
    }
}