package com.tuwaiq.weretogo.views

import android.content.Intent
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.tuwaiq.weretogo.R
import com.tuwaiq.weretogo.databinding.FragmentIntroBinding

private const val TAG = "LoginFragment"

class IntroFragment : Fragment() {

    private lateinit var binding: FragmentIntroBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {

            var firestore: FirebaseFirestore
            firestore = FirebaseFirestore.getInstance()
            firestore.collection("users")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.e("DataRef", "${document.data}")
                        Log.e("ID", "${document.data.get("id")}")
                        if ("${document.data.get("id")}".equals(Firebase.auth.currentUser?.uid)) {
                            Log.e("UserType", "${document.data.get("UserType")}")
                            if ("${document.data.get("UserType")}".equals("owner")) {
//                                val intent = Intent(context, MainActivity::class.java)
//                                startActivity(intent)
                            } else {

                                findNavController().navigate(IntroFragmentDirections.actionIntroFragmentToHomeFragment())
                                Toast.makeText(
                                    requireContext(),
                                    auth.currentUser?.displayName.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
        }
    }

                    override fun onCreateView(
                        inflater: LayoutInflater, container: ViewGroup?,
                        savedInstanceState: Bundle?
                    ): View? {
                        binding = FragmentIntroBinding.inflate(inflater, container, false)
                        return binding.root

                    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(IntroFragmentDirections.actionIntroFragmentToLoginFragment())
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(IntroFragmentDirections.actionIntroFragmentToRegisterFragment())
        }


        binding.regOwner.setOnClickListener {

            findNavController().navigate(R.id.action_introFragment_to_registerOwnerFragment)

        }
    }
}