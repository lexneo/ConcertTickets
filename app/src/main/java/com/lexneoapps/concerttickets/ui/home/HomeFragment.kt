package com.lexneoapps.concerttickets.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.lexneoapps.concerttickets.R
import com.lexneoapps.concerttickets.data.remote.ConcertTicketsApi
import com.lexneoapps.concerttickets.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {




    private var _binding: FragmentHomeBinding? = null


    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!



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




    }
}