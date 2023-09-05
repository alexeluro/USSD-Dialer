package com.inspiredcoda.ussddialer.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.inspiredcoda.ussddialer.databinding.FragmentUssdHomeBinding
import com.inspiredcoda.ussddialer.presentation.contracts.ActionCallContract
import com.inspiredcoda.ussddialer.util.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class UssdHomeFragment : Fragment() {

    private var _binding: FragmentUssdHomeBinding? = null
    private val binding: FragmentUssdHomeBinding
        get() = _binding!!

    private lateinit var mAdapter: UssdListAdapter

    private val ussdViewModel: UssdHomeViewModel by viewModels()

    private lateinit var actionCall: ActivityResultLauncher<String>
    private lateinit var actionCallPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUssdHomeBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLauncher()

        checkForPermission()

        mAdapter = UssdListAdapter { ussdCode ->
            // launch intent here
            checkForPermission {
                Timber.d("USSD code: ${ussdCode.ussdCode}")
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${ussdCode.ussdCode}"))
//                val intent = Intent(Intent.ACTION_DIAL).also {
//                    it.data = Uri.parse("tel:${ussdCode}")
//                }

                requireActivity().startActivity(intent)
            }

        }

        observers()

        initRecyclerView()

    }

    private fun checkForPermission(action: (() -> Unit) = {}) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            actionCallPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
            return
        }

        action()

    }


    private fun initLauncher() {
        actionCallPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    Timber.d("Permission granted")

                } else {
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.CALL_PHONE
                    )
                }
            }

        actionCall = registerForActivityResult(ActionCallContract()) { isSuccessful ->
            if (isSuccessful) {
                snackbar("Successful")
            } else {
                snackbar("Failed!")
            }
        }
    }

    private fun observers() {
        ussdViewModel.uiEvent.onEach { event ->
            when (event) {
                is UssdHomeViewModel.UssdUiEvent.OnUssdCodesGenerated -> {
                    Timber.d("USSD codes retrieved: ${event.ussdCodes.size}")
                    mAdapter.updateUssdCodes(event.ussdCodes)
                }
            }

        }.launchIn(lifecycleScope)
    }

    private fun initRecyclerView() {
        binding.ussdHomeRecyclerView.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

            adapter = mAdapter
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


}