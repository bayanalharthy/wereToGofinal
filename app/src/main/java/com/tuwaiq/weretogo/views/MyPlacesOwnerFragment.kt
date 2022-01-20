package com.tuwaiq.weretogo.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.tuwaiq.weretogo.R
import com.tuwaiq.weretogo.databinding.FragmentMyPlacesOwnerBinding
import com.tuwaiq.weretogo.network.model.Place
import com.tuwaiq.weretogo.viewsimport.MyPlaceOwnerAdapter

private const val TAG = "MyPlacesOwnerFragment"
class MyPlacesOwnerFragment : Fragment() {

    private lateinit var binding: FragmentMyPlacesOwnerBinding
    private lateinit var myPlaceOwnerAdapter:MyPlaceOwnerAdapter
//    val listMyPlace = mutableListOf<Place>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyPlacesOwnerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMyPlace()
    }

    private fun updateUi(places:List<Place>) {
        myPlaceOwnerAdapter = MyPlaceOwnerAdapter(places, requireContext())
        binding.recyclerView.adapter = myPlaceOwnerAdapter
    }

    fun getMyPlace(){
        Firebase.firestore.collection("places").whereEqualTo("userId",FirebaseAuth.getInstance().currentUser!!.uid).get().addOnSuccessListener {
            Log.d(TAG, "onViewCreated: ${it.documents}")
           val list = mutableListOf<Place>()
            for (document in it.documents){
                val place = document.toObject<Place>()
                list.add(place!!)
            }
            updateUi(list)
        }
    }
}