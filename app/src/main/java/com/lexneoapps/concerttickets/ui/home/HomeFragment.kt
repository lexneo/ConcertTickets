package com.lexneoapps.concerttickets.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lexneoapps.concerttickets.R
import com.lexneoapps.concerttickets.data.local.PreferencesManager
import com.lexneoapps.concerttickets.data.local.models.Discounted
import com.lexneoapps.concerttickets.databinding.FragmentHomeBinding
import com.lexneoapps.concerttickets.ui.home.adapters.ExpiredAdapter
import com.lexneoapps.concerttickets.ui.home.adapters.ForYouAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {


    private var _binding: FragmentHomeBinding? = null


    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var forYouAdapter: ForYouAdapter
    private lateinit var expiredAdapter: ExpiredAdapter
    private var upcomingList = mutableListOf<Discounted>()

    @Inject
    lateinit var preferencesManager: PreferencesManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTicketsFromApiIfNeeded()

        setupRecyclerView()
        setUpObservers()
        setUpListeners()


    }

    private fun setupRecyclerView() {
        forYouAdapter = ForYouAdapter()
        binding.forYouRecyclerView.apply {
            adapter = forYouAdapter
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        }

        expiredAdapter = ExpiredAdapter()
        binding.expiredRecyclerView.apply {
            adapter = expiredAdapter
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        }
    }

    private fun setUpObservers() {

        viewModel.discountedTickets.observe(viewLifecycleOwner) {

            forYouAdapter.submitList(it)
            expiredAdapter.submitList(it)

//                setUpUpcoming(it.toList())
        }
    }


//can't setUpUpcoming after restarting data
/* private fun setUpUpcoming(list: List<Discounted>){

         binding.apply {
             Glide.with(requireActivity()).load(Constants.BASE_URL_IMAGE +list[0]?.photo).into(upcomingImage1)
             cityTv.text = list[0].place
             timeTv.text = list[0].date
             ticketsLeftTv.text = "only ${list[0].quantity} left for ${list[0].price}"
             discountPercentageTv.text = "-${list[0].percentage}%"
             performerNameTv.text = list[0].name

             Glide.with(requireActivity()).load(Constants.BASE_URL_IMAGE +list[1].photo).into(upcomingImage1)
             cityTv2.text = list[1].place
             timeTv2.text = list[1].date
             ticketsLeftTv2.text = "only ${list[1].quantity} left for ${list[1].price}"
             discountPercentageTv2.text = "-${list[1].percentage}%"
             performerNameTv2.text = list[1].name
         }
     }*/



private fun setUpListeners() {
    binding.admingFab.setOnClickListener {
        val action = HomeFragmentDirections.actionHomeFragmentToAdminFragment()
        findNavController().navigate(action)
    }
    forYouAdapter.setOnItemClickListener {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(it)
        findNavController().navigate(action)
    }
    expiredAdapter.setOnItemClickListener {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(it)
        findNavController().navigate(action)
    }
}
}