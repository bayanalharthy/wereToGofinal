package com.tuwaiq.weretogo.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tuwaiq.weretogo.databinding.FragmentHomeBinding

private const val TAG = "LoginFragment"

var catogery = ""

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    val user = Firebase.auth.currentUser


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logout.setOnClickListener {
            Firebase.auth.signOut()
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToIntroFragment())
        }

        binding.coffee.setOnClickListener {
            catogery = "coffee shop"
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPlacesListFragment())
        }

        binding.restaurants.setOnClickListener {
            catogery = "Restaurants"
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPlacesListFragment())
        }

        binding.malls.setOnClickListener {
            catogery = "malls"
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPlacesListFragment())
        }



    }
}