package com.tuwaiq.weretogo.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tuwaiq.weretogo.databinding.FragmentAddCommentBinding
import com.tuwaiq.weretogo.network.model.Place

private const val TAG = "LoginFragment"


class AddCommentFragment : Fragment() {

    private lateinit var binding: FragmentAddCommentBinding
    private val args: AddCommentFragmentArgs by navArgs()
    private lateinit var place: Place
    private var user: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCommentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        place = args.place
//        if (Firebase.auth)
        user = Firebase.auth.currentUser

        binding.btnComment.setOnClickListener {
            addComment(binding.ratingBar.rating.toString(), binding.etComment.text.toString())

        }

    }

    private fun addComment(rating: String, comment: String) {

        place.rateList?.add(
            Place.Rate(
                comment = comment,
                rating = rating,
                id = user?.uid,
                userName = user?.displayName
            )
        )

        Firebase.firestore.collection("places").document(place.id!!)
            .set(place).addOnSuccessListener {
                findNavController().popBackStack()
            }
    }

}