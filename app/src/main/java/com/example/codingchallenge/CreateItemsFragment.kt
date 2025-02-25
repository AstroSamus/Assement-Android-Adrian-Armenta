package com.example.codingchallenge

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.codingchallenge.MainActivity.Companion.permissionsGranted
import com.example.codingchallenge.data.AppConstants.FILENAME_FORMAT
import com.example.codingchallenge.data.AppConstants.TAG
import com.example.codingchallenge.database.Item
import com.example.codingchallenge.database.ItemDatabase
import com.example.codingchallenge.utils.CoroutineFragment
import com.example.codingchallenge.utils.createItemValidator
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CreateItemsFragment : CoroutineFragment(), RadioGroup.OnCheckedChangeListener {
    private var item: Item? = null
    private var isInUpdateOrDeleteMode = false

    var colorCodeSelection = ""
    private var imageCapture: ImageCapture? = null
    private var selectedImageURI: Uri? = null
    private lateinit var cameraExecutor: ExecutorService


    private lateinit var buttonConfirm: FloatingActionButton
    private lateinit var inputItemText: EditText
    //Radio group for color code choose
    private lateinit var radioGroupColorCode : RadioGroup
    private lateinit var radioButtonRed: RadioButton
    private lateinit var radioButtonBlue: RadioButton
    private lateinit var radioButtonGreen: RadioButton
    private lateinit var radioButtonYellow: RadioButton
    private lateinit var buttonPickPreviewImage: ImageButton
    private lateinit var createItemsViewFinder: PreviewView
    private lateinit var buttonTakePicture: FloatingActionButton
    private lateinit var buttonDelete: FloatingActionButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (permissionsGranted) {
            startCamera()
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

    }

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
        buttonPickPreviewImage = view.findViewById(R.id.buttonPickPreviewImage)
        createItemsViewFinder = view.findViewById(R.id.createItemsViewFinder)
        buttonTakePicture = view.findViewById(R.id.buttonTakePicture)
        buttonDelete = view.findViewById(R.id.buttonDelete)




        buttonConfirm.setOnClickListener { launch { onConfirm() }}
        buttonDelete.setOnClickListener { launch { onDelete() }}
        radioGroupColorCode.setOnCheckedChangeListener(this)
        buttonPickPreviewImage.setOnClickListener { onPickImage() }
        buttonTakePicture.setOnClickListener{ takePicture() }
        //This line of text should be executed after the inputItemText has been setted from the view
        arguments?.let{
            item = CreateItemsFragmentArgs.fromBundle(it).itemToEdit
            if(item!=null) {
                //If itemToEdit comes full of data then we should update an existing note instead of adding
                isInUpdateOrDeleteMode = true
                inputItemText.setText(item?.name)
                //WET programming, if we use this code block once again we pass it into a reusable function
                setActiveColorRadioButton(item?.colorCode)
                Glide.with(buttonPickPreviewImage)
                    .load(item?.image)
                    .error(R.drawable.ic_delete)
                    .into(buttonPickPreviewImage)
                selectedImageURI = Uri.parse(item?.image)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private suspend fun onDelete() {
        if(isInUpdateOrDeleteMode) {
            context.let {
                deleteItem(item!!)
            }
        }
        toItemsMenuFragment()
    }

    private suspend fun onConfirm() {
        val title = inputItemText.text.toString()
        var image = ""

        if(selectedImageURI != null) {
            image = selectedImageURI.toString()
        }

        //Add or update the note created
        if(createItemValidator.validate(title, colorCodeSelection, image)) {
            val newItem = Item(title, colorCodeSelection, image)
            context.let {
                if(isInUpdateOrDeleteMode) {
                    updateItem(newItem)
                }
                else  {
                    saveItem(newItem)
                }
            }
            //Return the user to the itemsMenuFragment
            toItemsMenuFragment()
        }
        //if it's not possible to create or edit a note, then display an error message
        else {
            val errorText = createItemValidator.generateErrorMessage(title, colorCodeSelection, image)
            Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCheckedChanged(p0: RadioGroup?, selectedButton: Int) {
        when (selectedButton) {
            radioButtonRed?.id -> colorCodeSelection = "RED"
            radioButtonBlue?.id -> colorCodeSelection = "BLUE"
            radioButtonGreen?.id -> colorCodeSelection = "GREEN"
            radioButtonYellow?.id -> colorCodeSelection = "YELLOW"
        }
    }

    private fun setActiveColorRadioButton(color: String? = "GREEN") {
        when(color) {
            "RED" ->  radioButtonRed.isChecked = true
            "BLUE" -> radioButtonBlue.isChecked = true
            "YELLOW" -> radioButtonYellow.isChecked = true
            else -> radioButtonGreen.isChecked = true
        }
    }

    fun toItemsMenuFragment() {
        val action = CreateItemsFragmentDirections.actionCreateItemsFragmentToItemsMenuFragment()
        findNavController().navigate(action)
    }


//Beginning of the file picker code ----------------------------------------
    private val pickImg = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        //When user picks an image or closes the file selector this gets executed
        if (uri != null) {
            selectedImageURI = uri
            buttonPickPreviewImage.setImageURI(selectedImageURI)
        }
    }
    private fun onPickImage() {
        //When button to pick image is pressed, the picker gets displayed
        pickImg.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
//End of the file picker code ------------------------------------------------------------------

//Beginning of the camera code -------------------------------------------------------------------
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(createItemsViewFinder.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder()
                //set quality to 50, this will improve a lot the size in storage of the image
                .setJpegQuality(40)
                .build()
            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview, imageCapture
                )

            } catch(e: Exception) {
                Log.e(TAG, "Use case binding failed", e)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePicture() {
        if(!permissionsGranted){
            Toast.makeText(context, R.string.app_will_not_work_permissions, Toast.LENGTH_SHORT).show()
            return
        }

        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            //image as webp to improve size
            put(MediaStore.MediaColumns.MIME_TYPE, "image/webp")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CodingChallengeHTS")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(requireActivity().contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(e: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${e.message}", e)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults){
                    selectedImageURI = output?.savedUri
                    buttonPickPreviewImage.setImageURI(selectedImageURI)
                }
            }
        )
    }

//End of the camera code -------------------------------------------------------------------


//Beginning of code for the DB--------------------------------------------------------------
    private suspend fun saveItem(item: Item) {
        context?.let {
            ItemDatabase(it).getItemDao().addItem(item)
        }
    }

    private suspend fun updateItem(itemForUpdate: Item) {
        //Set the id from the item that comes from the items menu
        itemForUpdate.id = item!!.id
        context?.let {
            ItemDatabase(it).getItemDao().updateItem(itemForUpdate)
        }
    }

    private suspend fun deleteItem(itemForDelete: Item) {
        itemForDelete.id = item!!.id
        context?.let {
            ItemDatabase(it).getItemDao().deleteItem(itemForDelete)
        }
    }
//End of the code for the DB
}