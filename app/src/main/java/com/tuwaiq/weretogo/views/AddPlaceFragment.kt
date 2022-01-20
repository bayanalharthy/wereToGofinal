package com.tuwaiq.weretogo.views

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.tuwaiq.weretogo.R
import com.tuwaiq.weretogo.databinding.FragmentAddPlaceBinding
import com.tuwaiq.weretogo.network.model.Place
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import java.io.File
import java.util.*

private const val TAG = "AddPlaceFragment"

class AddPlaceFragment : Fragment() {

    private lateinit var binding: FragmentAddPlaceBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var lat: String
    private lateinit var lng: String
    private lateinit var address: String
    private var imageUri: Uri? = null
    private var imageUrl: String? = null
    private var uploadTask: UploadTask? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPlaceBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = Firebase.firestore

        Glide.with(requireContext())
            .load(imageUri)
            .placeholder(R.drawable.ic_baseline_add_24)
            .fitCenter()
            .into(binding.ivPlace)

        binding.btnLocation.setOnClickListener {
            findNavController().navigate(
                AddPlaceFragmentDirections.actionAddPlaceFragmentToMapFragment(
                    null,
                    null
                )
            )
        }

        binding.ivPlace.setOnClickListener {
            pickAndUploadImage()
        }

        setFragmentResultListener("location") { requestKey, bundle ->
            lat = bundle.getString("lat")!!
            lng = bundle.getString("lng")!!
            address = bundle.getString("address")!!
        }

        binding.btnConfirm.setOnClickListener {
            val place = Place(
                title = binding.etTitle.text.toString(),
                description = binding.etDescription.text.toString(),
                rating = binding.appCompatRatingBar.rating.toString(),
                lat = lat,
                lng = lng,
                address = address,
                imageUrl = imageUrl,
                catogery = binding.spinner.selectedItem.toString()


                )

            db.collection("places").add(place)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    Toast.makeText(requireContext(), "added successfully ", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().popBackStack()

                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    Toast.makeText(requireContext(), "some error ", Toast.LENGTH_SHORT).show()
                }
        }
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun pickAndUploadImage() = selectImageFromGalleryLauncher.launch("image/*")

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun onStorageDenied() {

    }

    private val selectImageFromGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
            Glide.with(requireContext())
                .load(uri)
                .fitCenter()
                .into(binding.ivPlace)
            uploadImage()
        }

    private fun uploadImage() {
        val file = Uri.fromFile(File(imageUri?.path))
        val ref = Firebase.storage.reference.child("images/${UUID.randomUUID()}")


        ref.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                Log.d(TAG, "uploadImage: success")
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { imageUrl ->
                    this.imageUrl = imageUrl.toString()
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "uploadImage: failed")
            }
            .addOnProgressListener { task ->
                val percent = task.bytesTransferred / task.totalByteCount

            }
            .addOnCanceledListener {

            }
    }
}