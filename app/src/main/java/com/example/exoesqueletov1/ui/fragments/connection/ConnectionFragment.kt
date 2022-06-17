package com.example.exoesqueletov1.ui.fragments.connection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.databinding.FragmentConnectionBinding
import com.example.exoesqueletov1.interfaces.BluetoothResource
import com.example.exoesqueletov1.utils.Constants
import com.example.exoesqueletov1.utils.Utils.getTypeUser
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class ConnectionFragment : Fragment() {

    private lateinit var binding: FragmentConnectionBinding
    private lateinit var viewModel: ConnectionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConnectionBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ConnectionViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.user.observe(viewLifecycleOwner) {

            binding.apply {
                switchWalkExercise.setOnCheckedChangeListener { _, isRepetition ->
                    if (isRepetition) {
                        switchStepTime.visibility = View.GONE
                        textRepetition.visibility = View.VISIBLE
                        sliderRepetition.visibility = View.VISIBLE
                        textAternaciones.visibility = View.VISIBLE
                        chipGroup.visibility = View.VISIBLE
                        textSteps.visibility = View.GONE
                        sliderStep.visibility = View.GONE
                        textTime.visibility = View.GONE
                        sliderTime.visibility = View.GONE
                    } else {
                        switchStepTime.visibility = View.VISIBLE
                        textRepetition.visibility = View.GONE
                        sliderRepetition.visibility = View.GONE
                        textAternaciones.visibility = View.GONE
                        chipGroup.visibility = View.GONE
                        textSteps.visibility = View.GONE
                        sliderStep.visibility = View.GONE
                        textTime.visibility = View.GONE
                        sliderTime.visibility = View.GONE
                    }
                }
                switchStepTime.setOnCheckedChangeListener { _, isTime ->
                    if (isTime) {
                        textTime.visibility = View.VISIBLE
                        sliderTime.visibility = View.VISIBLE
                        textSteps.visibility = View.GONE
                        sliderStep.visibility = View.GONE
                    } else {
                        textSteps.visibility = View.VISIBLE
                        sliderStep.visibility = View.VISIBLE
                        textTime.visibility = View.GONE
                        sliderTime.visibility = View.GONE
                    }
                }
            }

            when (it.user.getTypeUser()) {
                Constants.TypeUser.Patient -> {
                    binding.textTitleCardProfile.visibility = View.GONE
                    binding.cardProfile.visibility = View.GONE
                }
                Constants.TypeUser.Specialist -> {
                    binding.textTitleCardProfile.visibility = View.VISIBLE
                    binding.cardProfile.visibility = View.VISIBLE
                }
                Constants.TypeUser.Admin -> {
                    binding.textTitleCardProfile.visibility = View.GONE
                    binding.cardProfile.visibility = View.GONE
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun getStatus(resource: BluetoothResource) {
        when (resource.status) {
            Constants.StatusBluetoothDevice.Disconnected -> {
                val status = findNavController().popBackStack()
                if (!status) findNavController().navigate(R.id.action_global_navigation_paired_device)
            }
            Constants.StatusBluetoothDevice.ConnectionFailed -> {
                val status = findNavController().popBackStack()
                if (!status) findNavController().navigate(R.id.action_global_navigation_paired_device)
            }
            else -> {}
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

}