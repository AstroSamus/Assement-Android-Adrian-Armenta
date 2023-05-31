package com.example.codingchallenge.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.net.URI

internal class createItemValidatorTest {

    @Test
    fun `given an empty name to the validator should return false`() {
        val name = ""
        val colorCode = "#FAFAFA"
        val image = URI.create("image")

        var result = createItemValidator.validate(name, colorCode, image)

        assertThat(result).isFalse()
    }

    @Test
    fun `given an empty colorCode to the validator should return false`() {
        val name = "Title"
        val colorCode = ""
        val image = URI.create("image")

        var result = createItemValidator.validate(name, colorCode, image)

        assertThat(result).isFalse()
    }

    @Test
    fun `given an empty image to the validator should return false`() {
        val name = "Title"
        val colorCode = "#FAFAFA"
        val image = null

        var result = createItemValidator.validate(name, colorCode, image)

        assertThat(result).isFalse()
    }

    @Test
    fun `given name, colorCode and image as not empty to the validator should return true`() {
        val name = "Title"
        val colorCode = "#FAFAFA"
        val image = URI.create("image")

        var result = createItemValidator.validate(name, colorCode, image)

        assertThat(result).isTrue()
    }



    @Test
    fun `given empty name, color code and image to the generateErrorMessage, should return the corresponding error text`() {
        val name = ""
        val colorCode = ""
        val image = null

        var result = createItemValidator.generateErrorMessage(name, colorCode, image)

        var expectedResult = createItemValidator.errorBase + createItemValidator.errorName + createItemValidator.errorColorCode + createItemValidator.errorImage

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `given name, color code and image to the generateErrorMessage should return an empty string`() {
        val name = "Title"
        val colorCode = "#FAFAFA"
        val image = URI.create("image")

        var result = createItemValidator.generateErrorMessage(name, colorCode, image)

        assertThat(result).isEqualTo("")
    }


}