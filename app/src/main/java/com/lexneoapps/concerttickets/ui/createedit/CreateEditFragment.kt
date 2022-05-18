package com.lexneoapps.concerttickets.ui.createedit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.lexneoapps.concerttickets.R
import com.lexneoapps.concerttickets.data.local.models.Discounted
import com.lexneoapps.concerttickets.data.local.models.NonDiscounted
import com.lexneoapps.concerttickets.databinding.FragmentCreateEditBinding
import com.lexneoapps.concerttickets.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.log

private const val TAG = "CreateEditFragment"


@AndroidEntryPoint
class CreateEditFragment : Fragment(R.layout.fragment_create_edit) {


    private var _binding: FragmentCreateEditBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: CreateEditViewModel by viewModels()
    private val args: CreateEditFragmentArgs by navArgs()
    private var discounted: Discounted? = null
    private var nonDiscounted: NonDiscounted? = null
    private var totalTickets = 0


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
        viewModel.getTicketsById(args.id)
        subscribeToObservers()
        setUpListeners()

    }

    private fun setUpInitialState() {
        Log.i(
            TAG,
            "setUpInitialState discounted.id ${discounted?.id} nonDiscounted.id ${nonDiscounted?.id}"
        )


        if (discounted != null && nonDiscounted != null) {
            totalTickets = discounted!!.quantity!! + nonDiscounted!!.quantity!!
            Log.i(
                CREATE_COMBINED,
                "FRAGMENT setUpInitialState discounted id ${discounted!!.id}nonDiscounted id ${nonDiscounted?.id}"
            )

            binding.apply {
                switchButton.isChecked = true
                discountCardView.visibility = View.VISIBLE
                discountValueInputEditText.setText(discounted?.discount.toString())
                discountQuantityInputEditText.setText(discounted?.quantity.toString())
                Glide.with(requireActivity()).load(Constants.BASE_URL_IMAGE + discounted?.photo)
                    .into(image)
                nameEditText.setText(discounted?.name)
                descriptionEditText.setText(discounted?.description)
                placeEditText.setText(discounted?.place)
                timeEditText.setText(discounted?.date)
                priceEditText.setText(discounted?.price.toString())
                quantityEditText.setText(nonDiscounted?.quantity.toString())
            }
        } else if (discounted != null && nonDiscounted == null) {
            totalTickets = discounted!!.quantity!!

            binding.apply {
                switchButton.isChecked = true
                discountCardView.visibility = View.VISIBLE
                discountValueInputEditText.setText(discounted?.discount.toString())
                discountQuantityInputEditText.setText(discounted?.quantity.toString())
                Glide.with(requireActivity()).load(Constants.BASE_URL_IMAGE + discounted?.photo)
                    .into(image)
                nameEditText.setText(discounted?.name)
                descriptionEditText.setText(discounted?.description)
                placeEditText.setText(discounted?.place)
                timeEditText.setText(discounted?.date)
                priceEditText.setText(discounted?.price.toString())
                quantityEditText.setText(discounted?.quantity.toString())
            }
        } else if (discounted == null && nonDiscounted != null) {
            totalTickets = nonDiscounted!!.quantity!!

            Log.i(
                CREATE_COMBINED,
                "FRAGMENT setUpInitialState nonDiscounted id ${nonDiscounted?.id}"
            )
            binding.apply {
                binding.discountCardView.visibility = View.GONE
                Glide.with(requireActivity()).load(Constants.BASE_URL_IMAGE + nonDiscounted?.photo)
                    .into(image)
                nameEditText.setText(nonDiscounted?.name)
                descriptionEditText.setText(nonDiscounted?.description)
                placeEditText.setText(nonDiscounted?.place)
                timeEditText.setText(nonDiscounted?.date)
                priceEditText.setText(nonDiscounted?.price.toString())
                quantityEditText.setText(nonDiscounted?.quantity.toString())
            }
        } else {
            binding.apply {
                binding.createEditBtn.text = "Create"
                Glide.with(requireActivity()).load(Constants.BASE_URL_IMAGE + "/images/Concert.jpg")
                    .into(image)
            }
        }
    }

    private fun subscribeToObservers() {

        viewModel.finished.observe(viewLifecycleOwner) { boolean ->
            Log.i(CREATE_COMBINED, "FRAGMENT finished value $boolean")
            if (boolean) {
                viewModel.nonDiscounted.observe(viewLifecycleOwner) { nonDis ->
                    nonDiscounted = nonDis
                    Log.i(CREATE_COMBINED, "FRAGMENT nonDiscounted id${nonDiscounted?.id}")
                }
                viewModel.discounted.observe(viewLifecycleOwner) { dis ->
                    discounted = dis
                    Log.i(CREATE_COMBINED, "FRAGMENT discounted id${discounted?.id}")
                }
                setUpInitialState()

            }

        }

        viewModel.editEvent.observe(viewLifecycleOwner) { editEvent ->
            when (editEvent) {
                EditEvent.WRONG_PARAMETERS -> showToastMsg("Total number of tickets must stay the same!")
                EditEvent.UPDATED_BOTH -> showToastMsg("Both ticket types have been updated!")
                EditEvent.UP_DISC_CRE_NONDISC -> showToastMsg(
                    "Discounted tickets have been " +
                            "updated and non-discounted have been created."
                )
                EditEvent.UP_NONDISC_CRE_DISC -> showToastMsg(
                    "Non-discounted tickets have been " +
                            "updated and discounted have been created."
                )
                EditEvent.EMPTY_PARAMETER -> showToastMsg("Only description can be empty!")
                EditEvent.CREATED_NEW_TICKETS -> showToastMsg("New tickets have been created.")
            }
        }

        sharedViewModel.time.observe(viewLifecycleOwner) {
            binding.timeEditText.setText(it)
        }


    }

    private fun setUpListeners() {
        binding.switchButton.setOnCheckedChangeListener { _, b ->

            if (b) {
                binding.discountCardView.visibility = View.VISIBLE
            } else if (binding.discountQuantityInputEditText.text?.isNotEmpty() == true ||
                binding.discountValueInputEditText.text?.isNotEmpty() == true
            ) {
                binding.discountCardView.visibility = View.VISIBLE
                binding.switchButton.isChecked = true
                Toast.makeText(
                    requireContext(),
                    "Discount value field and/or discount amount contain values",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                binding.discountCardView.visibility = View.GONE
            }
            Log.i(TAG, "switchValue ${binding.switchButton.isChecked}")

        }
        binding.createEditBtn.setOnClickListener {
            if (binding.createEditBtn.text == "Edit") {
                editTickets()
            } else {
                createNewTickets()
            }


        }
        binding.timeEditText.setOnClickListener {
            showDatePickerDialog(it)
        }
    }

    fun showDatePickerDialog(v: View) {
        val newFragment = DatePickerFragment()
        newFragment.show(this.parentFragmentManager, "datePicker")
    }

    private fun editTickets() {

        var discountedQuantity = binding.discountQuantityInputEditText.text.toString().toIntOrNull()
        var nonDiscountedQuantity = binding.quantityEditText.text.toString().toIntOrNull()

        if (nonDiscountedQuantity == null) {
            binding.quantityEditText.setText("0")
            nonDiscountedQuantity = 0
        }
        if (discountedQuantity == null) {
            binding.discountQuantityInputEditText.setText("0")
            discountedQuantity = 0
        }
        var discountValue = binding.discountValueInputEditText.text.toString().toIntOrNull()
        if (discountValue == null) {
            binding.discountQuantityInputEditText.setText("0.0")
            discountValue = 0
        }



        viewModel.editTickets(
            discountedQuantity,
            nonDiscountedQuantity,
            totalTickets,
            discountValue
        )
    }

    private fun createNewTickets() {
        binding.apply {
            //validate edittext fields and save it to db
            val name = nameEditText.text.toString()
            val date = timeEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val place = placeEditText.text.toString()
            val price = priceEditText.text.toString().toDoubleOrNull()


            val quantity = quantityEditText.text.toString().toIntOrNull()
            val discount = discountValueInputEditText.text.toString().toIntOrNull()
            val discountQuantity = discountQuantityInputEditText.text.toString().toIntOrNull()

            val discountChecked = switchButton.isChecked

            Log.i(TAG, "WHAT $name ,$date,$place,$price,$quantity,$discount,$discountQuantity")
            Log.i(TAG, "discChecked $discountChecked")
            viewModel.createTickets(
                name,
                date,
                description,
                place,
                price,
                quantity,
                discount,
                discountQuantity
            )


        }
    }


    private fun showToastMsg(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }


}