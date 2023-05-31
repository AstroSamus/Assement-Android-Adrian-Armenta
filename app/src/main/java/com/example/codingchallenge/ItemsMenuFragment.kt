package com.example.codingchallenge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.codingchallenge.utils.CoroutineFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ItemsMenuFragment : CoroutineFragment() {

    private lateinit var buttonCreateItem: FloatingActionButton

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

        buttonCreateItem.setOnClickListener {
            val action = ItemsMenuFragmentDirections.actionItemsMenuFragmentToCreateItemsFragment()
            findNavController().navigate(action)
        }
    }
}