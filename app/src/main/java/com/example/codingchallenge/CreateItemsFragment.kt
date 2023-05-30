package com.example.codingchallenge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.codingchallenge.data.Item
import com.example.codingchallenge.utils.createItemValidator
import com.google.android.material.floatingactionbutton.FloatingActionButton


class CreateItemsFragment : Fragment(), RadioGroup.OnCheckedChangeListener {

    private var colorCodeSelection = ""

    private lateinit var buttonConfirm: FloatingActionButton
    private lateinit var inputItemText: EditText
    //Radio group for color code choose
    private lateinit var radioGroupColorCode : RadioGroup
    private lateinit var radioButtonRed: RadioButton
    private lateinit var radioButtonBlue: RadioButton
    private lateinit var radioButtonGreen: RadioButton
    private lateinit var radioButtonYellow: RadioButton



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonConfirm = view.findViewById(R.id.buttonConfirm)
        inputItemText = view.findViewById(R.id.itemText)
        radioGroupColorCode = view.findViewById(R.id.radioGroupColorCode)
        radioButtonRed = view.findViewById(R.id.radioButtonRed)
        radioButtonBlue = view.findViewById(R.id.radioButtonBlue)
        radioButtonGreen = view.findViewById(R.id.radioButtonGreen)
        radioButtonYellow = view.findViewById(R.id.radioButtonYellow)

        buttonConfirm.setOnClickListener { onConfirm() }
        radioGroupColorCode.setOnCheckedChangeListener(this)

    }

    private fun onConfirm() {
        val title = inputItemText.text.toString()
        //Color code selection is set in the onCheckedChanged fun and is hold in the colorCodeSelection
        //temporary image value
        val image = "image"

        if(createItemValidator.validate(title, colorCodeSelection, image)) {
            val newItem = Item(title, colorCodeSelection, image)
        } else {
            val errorText = createItemValidator.generateErrorMessage(title, colorCodeSelection, image)
            Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show()
        }

        val action = CreateItemsFragmentDirections.actionCreateItemsFragmentToItemsMenuFragment()
        findNavController().navigate(action)
    }

    override fun onCheckedChanged(p0: RadioGroup?, selectedButton: Int) {
        when (selectedButton) {
            radioButtonRed?.id -> colorCodeSelection = "RED"
            radioButtonBlue?.id -> colorCodeSelection = "BLUE"
            radioButtonGreen?.id -> colorCodeSelection = "GREEN"
            radioButtonYellow?.id -> colorCodeSelection = "YELLOW"
        }
    }
}