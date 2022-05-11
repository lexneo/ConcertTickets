package com.lexneoapps.concerttickets.ui.createedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.lexneoapps.concerttickets.R
import com.lexneoapps.concerttickets.data.local.models.Discounted
import com.lexneoapps.concerttickets.data.local.models.NonDiscounted
import com.lexneoapps.concerttickets.databinding.FragmentCreateEditBinding
import com.lexneoapps.concerttickets.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CreateEditFragment : Fragment(R.layout.fragment_create_edit) {


    private var _binding: FragmentCreateEditBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    private val viewModel : CreateEditViewModel by viewModels()
    private val args: CreateEditFragmentArgs by navArgs()
    private var discounted: Discounted? = null
    private var nonDiscounted: NonDiscounted? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateEditBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpInitialState()
        setUpListeners()

    }

    private fun setUpInitialState(){
        discounted = args.discounted
        nonDiscounted = args.nonDiscounted
        if (discounted != null){
            binding.apply {
                switchButton.isChecked = true
                discountCardView.visibility = View.VISIBLE
                discountValueInputEditText.setText(discounted?.discount.toString())
                discountQuantityInputEditText.setText(discounted?.quantity.toString())
                Glide.with(requireActivity()).load(Constants.BASE_URL_IMAGE +discounted?.photo).into(image)
                nameEditText.setText(discounted?.name)
                descriptionEditText.setText(discounted?.description)
                placeEditText.setText(discounted?.place)
                timeEditText.setText(discounted?.date)
                priceEditText.setText(discounted?.price.toString())
                quantityEditText.setText(discounted?.quantity.toString())
            }
        }else if (nonDiscounted != null){
            binding.apply {
                binding.discountCardView.visibility = View.GONE
                Glide.with(requireActivity()).load(Constants.BASE_URL_IMAGE +nonDiscounted?.photo).into(image)
                nameEditText.setText(nonDiscounted?.name)
                descriptionEditText.setText(nonDiscounted?.description)
                placeEditText.setText(nonDiscounted?.place)
                timeEditText.setText(nonDiscounted?.date)
                priceEditText.setText(nonDiscounted?.price.toString())
                quantityEditText.setText(nonDiscounted?.quantity.toString())
            }
        }else{
            binding.apply {
                Glide.with(requireActivity()).load(Constants.BASE_URL_IMAGE +"images/Concert.jpg").into(image)
            }

        }
    }

    private fun setUpListeners(){
        binding.switchButton.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                binding.discountCardView.visibility = View.VISIBLE
            }else{
                binding.discountCardView.visibility = View.GONE
            }
        }
        binding.saveBtn.setOnClickListener {
            binding.apply {
            //validate edittext fields and save it to db
            }
        }
    }

    fun setUpObservers(){

    }


}