package com.lexneoapps.concerttickets.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.lexneoapps.concerttickets.R
import com.lexneoapps.concerttickets.data.local.models.Discounted
import com.lexneoapps.concerttickets.databinding.FragmentDetailBinding
import com.lexneoapps.concerttickets.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var _binding: FragmentDetailBinding? = null


    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()
    private var discounted: Discounted? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpInitialState()


    }

    private fun setUpInitialState(){
        discounted = args.discounted

        binding.apply {

            Glide.with(requireActivity()).load(Constants.BASE_URL_IMAGE +discounted?.photo).into(image)
            performerNameTv.text = discounted?.name
            descriptionTv.text = discounted?.description
            placeInputTv.text = discounted?.place
            timeInputTv.text = discounted?.date
            quantityInputTv.text = discounted?.quantity.toString()
            discountInputTv.text = discounted?.percentage.toString()
            finalPriceInputTv.text = discounted?.discountDifference.toString()

        }
    }
}