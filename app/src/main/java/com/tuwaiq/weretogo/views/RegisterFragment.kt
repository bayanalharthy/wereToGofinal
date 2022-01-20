package com.tuwaiq.weretogo.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tuwaiq.weretogo.databinding.FragmentRegisterBinding


private const val TAG = "RegisterFragment"

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = Firebase.firestore
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnreg.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passRe.text.toString()
            val userName = binding.usernameTxt.text.toString()
            registerUser(email, password, userName)
        }
    }

    private fun registerUser(email: String, password: String, userName: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = hashMapOf(
                        "userName" to userName,
                        "email" to email
                    )

                    db.collection("users").document(FirebaseAuth.getInstance().uid.toString())
                        .set(user)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }


                    Toast.makeText(requireActivity(), "successfully", Toast.LENGTH_SHORT).show()
                    setDisplayName()

                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToHomeFragment())
                } else {
                    Log.e(TAG, "there was something wrong", task.exception)
                    Toast.makeText(
                        requireActivity(),
                        "there was something wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
    }

    private fun setDisplayName() {
        val user = Firebase.auth.currentUser
        val profileUpdates = userProfileChangeRequest {
            displayName = binding.usernameTxt.text.toString()
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            }
    }
}

