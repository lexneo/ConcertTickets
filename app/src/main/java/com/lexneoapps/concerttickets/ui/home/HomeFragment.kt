package com.lexneoapps.concerttickets.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lexneoapps.concerttickets.R
import com.lexneoapps.concerttickets.data.local.PreferencesManager
import com.lexneoapps.concerttickets.data.local.models.Discounted
import com.lexneoapps.concerttickets.data.local.models.NonDiscounted
import com.lexneoapps.concerttickets.databinding.FragmentHomeBinding
import com.lexneoapps.concerttickets.ui.home.adapters.ExpiredAdapter
import com.lexneoapps.concerttickets.ui.home.adapters.ForYouAdapter
import com.lexneoapps.concerttickets.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
        }
        viewModel.expiredDiscounted.observe(viewLifecycleOwner) {
            expiredAdapter.submitList(it)
        }

        viewModel.nonDiscounted2Tickets.observe(viewLifecycleOwner) {
            Log.i(TAG, "setUpObservers: $it")
            if (it.size >= 2) {
                setUpUpcoming(it)
            }
        }
        viewModel.errorLoading.observe(viewLifecycleOwner){
            if (it){
                Toast.makeText(requireContext(), "Error loading data from internet.\nCheck your internet connection"
                    , Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setUpUpcoming(list: List<NonDiscounted>) {

        binding.apply {
            Glide.with(this@HomeFragment).load(Constants.BASE_URL_IMAGE + list[0].photo)
                .into(upcomingImage1)
            upcomingCvMonthTextview1.text = list[0].month
            upcomingCvDateTextview1.text = list[0].day
            upcomingCvYearTextview1.text = list[0].year
            cityTv.text = list[0].place
            timeTv.text = list[0].time
            ticketsLeftTv.text = "only ${list[0].quantity} left for ${list[0].price}"
            performerNameTv.text = list[0].name

            upcomingCardView1.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(null, list[0])
                findNavController().navigate(action)
            }

            Glide.with(this@HomeFragment).load(Constants.BASE_URL_IMAGE + list[1].photo)
                .into(upcomingImage2)
            upcomingCvMonthTextview2.text = list[1].month
            upcomingCvDateTextview2.text = list[1].day
            upcomingCvYearTextview2.text = list[1].year
            cityTv2.text = list[1].place
            timeTv2.text = list[1].time
            ticketsLeftTv2.text = "only ${list[1].quantity} left for ${list[1].price}"
            performerNameTv2.text = list[1].name

            upcomingCardView2.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(null, list[1])
                findNavController().navigate(action)
            }
        }
    }

    private fun setUpListeners() {
        binding.admingFab.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToAdminFragment()
            findNavController().navigate(action)
        }
        forYouAdapter.setOnItemClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(it, null)
            findNavController().navigate(action)
        }
        expiredAdapter.setOnItemClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(it, null)
            findNavController().navigate(action)
        }

    }
}
