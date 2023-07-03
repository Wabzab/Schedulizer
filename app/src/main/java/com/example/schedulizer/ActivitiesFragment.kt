package com.example.schedulizer

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.IOException

class ActivitiesFragment : Fragment(R.layout.fragment_activities) {

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_IMAGE_PICK = 2
    }

    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private lateinit var btnReset: Button
    private lateinit var tvTimer: TextView
    private lateinit var stopwatch: Stopwatch
    private lateinit var btnActSave: Button
    private lateinit var btnSelectImage: Button
    private lateinit var ivSelectedImage: ImageView
    private lateinit var ivActIcon: ImageView

    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_activities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = Firebase.firestore
        var tracking = false
        var paused = false
        var time: Long = 0
        var mainActivity = requireActivity() as MainActivity
        btnStart = view.findViewById(R.id.btnStart)
        btnStop = view.findViewById(R.id.btnStop)
        btnReset = view.findViewById(R.id.btnReset)
        tvTimer = view.findViewById(R.id.tvTimer)
        btnActSave = view.findViewById(R.id.btnActSave)
        btnSelectImage = view.findViewById(R.id.btnSelectImage)
        ivSelectedImage = view.findViewById(R.id.ivSelectedImage)

        stopwatch = Stopwatch { elapsedTime ->
            tvTimer.text = formatTime(elapsedTime)
            time = elapsedTime
        }

        btnStart.setOnClickListener {
            if (!tracking) {
                tracking = true
            }
            stopwatch.start()
            paused = false
        }

        btnStop.setOnClickListener {
            if (tracking) {
                stopwatch.stop()
                paused = true
            }
        }

        btnReset.setOnClickListener {
            stopwatch.stop()
            stopwatch.reset()
            tracking = false
            paused = false
            selectedImageUri = null
            ivSelectedImage.visibility = View.GONE
            ivSelectedImage.setImageDrawable(null)
        }

        btnActSave.setOnClickListener {
            if (tracking) {
                stopwatch.stop()
                val layout = layoutInflater.inflate(R.layout.alert_save_activity, null)
                val spinner = layout.findViewById<Spinner>(R.id.spnAlertActTag)
                setSpinner(spinner, mainActivity, db)
                var alertDialogBuilder = AlertDialog.Builder(mainActivity)
                alertDialogBuilder.setTitle("Save Activity")
                alertDialogBuilder.setCancelable(false)
                alertDialogBuilder.setView(layout)
                // Set positive button functionality
                alertDialogBuilder.setPositiveButton(
                    "Save",
                    DialogInterface.OnClickListener { dialog: DialogInterface, i: Int ->
                        tracking = false
                        paused = false
                        saveActivity(layout, mainActivity, time, selectedImageUri)
                        dialog.cancel()
                    })
                // Set negative button functionality
                alertDialogBuilder.setNegativeButton(
                    "Cancel",
                    DialogInterface.OnClickListener { dialog: DialogInterface, i: Int ->
                        if (tracking) {
                            stopwatch.start()
                        }
                        dialog.cancel()
                    })
                alertDialogBuilder.show()
            }
        }

        btnSelectImage.setOnClickListener {
            showImageSelectionDialog()
        }
    }

    private fun formatTime(timeInMillis: Long): String {
        val seconds = timeInMillis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60)
    }

    private fun setSpinner(spinner: Spinner, context: Context, db: FirebaseFirestore) {
        var tagList: ArrayList<String> = ArrayList()
        db.collection("Tags")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    tagList.add(
                        document.get("name") as String
                    )
                }
                val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                    context,
                    android.R.layout.simple_list_item_1,
                    tagList
                )
                spinner.adapter = arrayAdapter
            }
    }

    private fun saveActivity(layout: View, context: Context, time: Long, selectedImageUri: Uri?) {
        stopwatch.reset()
        val spinner = layout.findViewById<Spinner>(R.id.spnAlertActTag)
        val etName = layout.findViewById<EditText>(R.id.etAlertActName)
        val etDesc = layout.findViewById<EditText>(R.id.etAlertActDesc)
        val date = Timestamp.now()
        var userID: String = ""
        var tagID: String = ""
        val getUserTask = DatabaseManager.getUser(SaveSharedPreferences.getUserName(context))
        getUserTask.addOnSuccessListener { result ->
            for (document in result) {
                userID = document.id
            }
        }
        val getTagTask = DatabaseManager.getTag(spinner.selectedItem.toString())
        getTagTask.addOnSuccessListener { result ->
            for (document in result) {
                tagID = document.id
            }
        }
        var allTasks = Tasks.whenAll(getUserTask, getTagTask)
        allTasks.addOnSuccessListener {
            val activity = Activity(
                "",
                etName.text.toString(),
                etDesc.text.toString(),
                date,
                time,
                tagID,
                userID
            )
            // Save the selected image URI along with the activity data
            val imageUri = selectedImageUri?.toString() ?: ""
            activity.imageUri = imageUri
            DatabaseManager.addActivity(activity)
        }
    }

    private fun showImageSelectionDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Image")
        builder.setItems(options) { dialog, item ->
            when (options[item]) {
                "Take Photo" -> {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
                "Choose from Gallery" -> {
                    val pickImageIntent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    startActivityForResult(pickImageIntent, REQUEST_IMAGE_PICK)
                }
                "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    ivSelectedImage.visibility = View.VISIBLE
                    ivSelectedImage.setImageBitmap(imageBitmap)
                }
                REQUEST_IMAGE_PICK -> {
                    data?.data?.let { uri ->
                        selectedImageUri = uri
                        try {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                requireActivity().contentResolver,
                                uri
                            )
                            ivSelectedImage.visibility = View.VISIBLE
                            ivSelectedImage.setImageBitmap(bitmap)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }
}
