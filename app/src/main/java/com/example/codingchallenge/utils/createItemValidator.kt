package com.example.codingchallenge.utils

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.codingchallenge.R

object createItemValidator {

    const val errorBase: String = "Missing: "
    const val errorName: String = "- name -"
    const val errorColorCode: String = "- color code -"
    const val errorImage: String = "- image -"


    //Business rules are: name, colorCode or image cannot be empty
    fun validate(name: String, colorCode: String, image: String): Boolean {
        var isValid = true

        if(name.isEmpty()) {
            isValid = false
        }
        if(colorCode.isEmpty()) {
            isValid = false
        }
        if(image.isEmpty()) {
            isValid = false
        }

        return isValid
    }

    fun generateErrorMessage(name: String, colorCode: String, image: String): String {

        var errorMessage: String = errorBase
        var isValid = true

        if(name.isEmpty()) {
            errorMessage = errorMessage.plus(errorName)
            isValid = false
        }

        if(colorCode.isEmpty()) {
            errorMessage = errorMessage.plus(errorColorCode)
            isValid = false
        }

        if(image.isEmpty()) {
            errorMessage = errorMessage.plus(errorImage)
            isValid = false
        }

        if(!isValid) {
            return errorMessage
        }

        //Return an empty error message if there was no error
        return ""
    }

}