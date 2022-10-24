package com.example.instagram.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.instagram.LoginActivity
import com.example.instagram.MainActivity
import com.example.instagram.Post
import com.example.instagram.R
import com.parse.*
import java.io.File
import java.time.LocalDate
import java.util.*

class ComposeFragment : Fragment() {

    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    var photoFile: File? = null
    lateinit var pb: ProgressBar
    lateinit var cameraResultLauncher: ActivityResultLauncher<Intent>
    lateinit var ivImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ivImage: ImageView = view.findViewById(R.id.iv_pic)
        super.onViewCreated(view, savedInstanceState)
        pb = view.findViewById<ProgressBar>(R.id.pbLoading);
        view.findViewById<Button>(R.id.button_submit).setOnClickListener {
            val desc = view.findViewById<EditText>(R.id.et_desc).text.toString()
            val user = ParseUser.getCurrentUser()
            if (photoFile != null) {
                submitPost(desc, user, photoFile!!)
            } else {
                Toast.makeText(requireContext(), "Take a picture!", Toast.LENGTH_SHORT).show()
            }
        }
        view.findViewById<Button>(R.id.button_pic).setOnClickListener {
            onLaunchCamera()
        }
        view.findViewById<Button>(R.id.bt_logOut).setOnClickListener {
            ParseUser.logOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun submitPost(desc: String, user: ParseUser, file: File) {
        pb.setVisibility(ProgressBar.VISIBLE)
        val post = Post()
        post.setDescription(desc)
        post.setUser(user)
        post.setImage(ParseFile(file))
        post.setCreated(Date())
        post.saveInBackground{ exception ->
            if (exception != null) {
                Log.e(MainActivity.TAG, "Error saving post")
                exception.printStackTrace()
                Toast.makeText(requireContext(), "Error saving post", Toast.LENGTH_SHORT).show()
            } else {
                Log.i(MainActivity.TAG, "Successfully saved post")
                pb.setVisibility(ProgressBar.INVISIBLE)
            }
        }
    }
    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(requireContext(), "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }

    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), MainActivity.TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(MainActivity.TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image  into a preview
                ivImage = requireView().findViewById<ImageView>(R.id.iv_pic)
                ivImage.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(getContext(), "Error taking picture", Toast.LENGTH_SHORT).show()
            }
        }
    }

}