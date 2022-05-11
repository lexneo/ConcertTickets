package com.lexneoapps.concerttickets.ui.delete

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.lexneoapps.concerttickets.ui.admin.AdminViewModel
import com.lexneoapps.concerttickets.ui.createedit.CreateEditViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteFragment : DialogFragment() {

    private val viewModel: AdminViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm deletion")
            .setMessage("Are you sure?")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Yes") { _, _ ->
                viewModel.onConfirmClick()
            }
            .create()
}