package com.tuwaiq.weretogo.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.tuwaiq.weretogo.databinding.FragmentPlaceDetailsBinding
import com.tuwaiq.weretogo.network.model.Place

private const val TAG = "LoginFragment"

class PlaceDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPlaceDetailsBinding
    private val args: PlaceDetailsFragmentArgs by navArgs()
    private lateinit var place: Place
    private lateinit var ratingAdapter: RatingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaceDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        place = args.place
        ratingAdapter = RatingAdapter()
        binding.rvRating.adapter = ratingAdapter
        binding.ratingBar.rating = place.rating?.toFloat()!!
        binding.tvTitle.text = place.title
        binding.tvDescription.text = place.description

        ratingAdapter.data = place.rateList!!.toMutableList()

        if (place.rateList!!.isNotEmpty()) {
            var overallRate = 0f
            place.rateList!!.forEach {
                overallRate += it.rating?.toFloat()!!
            }
            overallRate /= place.rateList!!.size
            binding.ratingBar.rating = overallRate
        }

        Glide.with(requireContext())
            .load(place.imageUrl)
            .fitCenter()
            .into(binding.ivPlace)

//        binding.share.setOnClickListener {
//            val i: Intent = Intent().apply {
//                action = Intent.ACTION_SEND
//               putExtra(
//                   Intent.EXTRA_TEXT,
//
//               )

//            }
//        }


        binding.btnAdd.setOnClickListener {
            findNavController().navigate(
                PlaceDetailsFragmentDirections.actionPlaceDetailsFragmentToAddCommentFragment(
                    place
                )
            )
        }

        binding.btnLocation.setOnClickListener {
            findNavController().navigate(
                PlaceDetailsFragmentDirections.actionPlaceDetailsFragmentToMapFragment(
                    place.lat, place.lng
                )
            )
        }
    }
}