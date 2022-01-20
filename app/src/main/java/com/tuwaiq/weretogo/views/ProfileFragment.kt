package com.tuwaiq.weretogo.views

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.tuwaiq.weretogo.R
import com.tuwaiq.weretogo.databinding.FragmentProfileBinding
import com.tuwaiq.weretogo.utils.AppConstants
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import java.io.File
import java.util.*

private const val TAG = "ProfileFragment"
private const val KEY_LANG = "language"


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private var imageUri: Uri? = null
    private var imageUrl: String? = null
//    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences =
            context?.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE)
        val language = sharedPreferences?.getString(KEY_LANG, "en")
        binding.tvLanguage.text = language

        val user = Firebase.auth.currentUser
        if (user != null) {
            binding.userName.setText(user.displayName)
            binding.email.text = user.email
            imageUrl = user.photoUrl.toString()
            Glide.with(requireContext())
                .load(user.photoUrl)
                .fitCenter()
                .into(binding.ivProfile)
        }

        binding.ivProfile.setOnClickListener {
            pickAndUploadImage()
        }

        binding.btnConfirm.setOnClickListener {
            updateData()
        }
        binding.delete.setOnClickListener {
            deleteDialog()
        }


        binding.tvLanguage.setOnClickListener {
            val dialog = LanguageDialog(language!!) {
                if (language != it) {
                    sharedPreferences.edit().putString(KEY_LANG, it).apply()
                    setLanguage(it)
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    activity?.finish()
                }

            }
            dialog.show(childFragmentManager, dialog.tag)
        }
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun pickAndUploadImage() = selectImageFromGalleryLauncher.launch("image/*")

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun onStorageDenied() {

    }

    private val selectImageFromGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            binding.loader.show()
            imageUri = uri
            Glide.with(requireContext())
                .load(uri)
                .fitCenter()
                .into(binding.ivProfile)
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
                    binding.loader.hide()
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "uploadImage: failed")
                binding.loader.hide()
            }
            .addOnProgressListener { task ->
                val percent = task.bytesTransferred / task.totalByteCount
            }
            .addOnCanceledListener {
            }
    }

    private fun updateData() {
        val user = Firebase.auth.currentUser
        val profileUpdates = userProfileChangeRequest {
            displayName = binding.userName.text.toString()
            if (imageUrl != null) photoUri = Uri.parse(imageUrl)
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                    Toast.makeText(requireContext(), getString(R.string.saved), Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun setLanguage(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val res: Resources = activity?.baseContext!!.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(locale)
        res.updateConfiguration(conf, dm)


    }


    fun deleteDialog() {
        val alertDialog = android.app.AlertDialog.Builder(context).setTitle("Delete account")
            .setMessage(
                "Are you sure? All flights and information will be deleted." +
                        "That can't be undone"
            )
        alertDialog.setPositiveButton("Delete") { _, _ ->
            Log.i(ContentValues.TAG, "Delete")
            deleteFirebaseUser()
            val user = Firebase.auth.currentUser!!
            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User account deleted.")
                    }
                }
            FirebaseAuth.getInstance().signOut()
            this?.let {
                findNavController().navigate(R.id.action_profileFragment_to_introFragment)

            }

//            requireActivity().finish()
        }
        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        alertDialog.show()
    }

    fun deleteFirebaseUser() {

        val db = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().uid.toString()).delete()

            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

    }


}

