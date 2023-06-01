package com.example.codingchallenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codingchallenge.adapter.ItemElementAdapter
import com.example.codingchallenge.database.ItemDatabase
import com.example.codingchallenge.utils.CoroutineFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class ItemsMenuFragment : CoroutineFragment() {

    private lateinit var buttonCreateItem: FloatingActionButton
    private lateinit var itemListContainer: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_items_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonCreateItem = view.findViewById(R.id.buttonCreateItem)
        itemListContainer = view.findViewById(R.id.itemListContainer)

        buttonCreateItem.setOnClickListener {
            val action = ItemsMenuFragmentDirections.actionItemsMenuFragmentToCreateItemsFragment()
            findNavController().navigate(action)
        }

        itemListContainer.apply {
            layoutManager = LinearLayoutManager(context)
        }

        launch {
            context?.let {
                val itemsDataset = ItemDatabase(it).getItemDao().getAll()
                itemListContainer.adapter = ItemElementAdapter(itemsDataset)
            }
        }
    }
}