package com.lexneoapps.concerttickets.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lexneoapps.concerttickets.R
import com.lexneoapps.concerttickets.databinding.FragmentAdminBinding
import com.lexneoapps.concerttickets.ui.admin.adapter.AdminDiscountAdapter
import com.lexneoapps.concerttickets.ui.admin.adapter.AdminNonDiscountAdapter
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "AdminFragment"
@AndroidEntryPoint
class AdminFragment : Fragment(R.layout.fragment_admin) {




    private var _binding: FragmentAdminBinding? = null

    private val viewModel: AdminViewModel by activityViewModels()

    private lateinit var nonDiscountAdapter : AdminNonDiscountAdapter
    private lateinit var discountAdapter: AdminDiscountAdapter


    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setUpObservers()
        setUpVisibility()
        setUpListeners()

    }

    private fun setUpListeners(){
        binding.addFab.setOnClickListener {
            val action = AdminFragmentDirections.actionAdminFragmentToCreateEditFragment(null,null)
            findNavController().navigate(action)
        }
        discountAdapter.setOnEditClickListener {
            val action = AdminFragmentDirections.actionAdminFragmentToCreateEditFragment(it,null)
            findNavController().navigate(action)
        }
        discountAdapter.setOnDeleteClickListener {
            viewModel.setDiscounted(it)
            val action = AdminFragmentDirections.actionAdminFragmentToDeleteFragment()
            findNavController().navigate(action)
        }
        nonDiscountAdapter.setOnEditClickListener {
            val action = AdminFragmentDirections.actionAdminFragmentToCreateEditFragment(null,it)
            findNavController().navigate(action)
        }
        nonDiscountAdapter.setOnDeleteClickListener {
            viewModel.setNonDiscounted(it)
            val action = AdminFragmentDirections.actionAdminFragmentToDeleteFragment()
            findNavController().navigate(action)
        }
        binding.resetButton.setOnClickListener {
            viewModel.resetEverything()
//            val action = AdminFragmentDirections.actionAdminFragmentToHomeFragment()
            findNavController().navigateUp()
        }
    }

    private fun setUpObservers(){
        viewModel.discountedTickets.observe(viewLifecycleOwner){
            discountAdapter.submitList(it)
        }
        viewModel.nonDiscountedTickets.observe(viewLifecycleOwner){
            nonDiscountAdapter.submitList(it)
        }
    }

    private fun setUpVisibility(){
        binding.toggleButtonGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked){
                when(checkedId){
                    R.id.button1 -> {
                        binding.discountRv.visibility = View.GONE
                        binding.nonDiscountRv.visibility = View.VISIBLE
                    }
                    R.id.button2 ->{
                        binding.discountRv.visibility = View.VISIBLE
                        binding.nonDiscountRv.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        nonDiscountAdapter = AdminNonDiscountAdapter()
        binding.nonDiscountRv.apply {
            adapter = nonDiscountAdapter
            layoutManager = LinearLayoutManager(activity)

        }

        discountAdapter = AdminDiscountAdapter()
        binding.discountRv.apply {
            adapter = discountAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }
}