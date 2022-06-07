package com.example.exoesqueletov1.ui.fragments.connection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.databinding.FragmentConnectionBinding

class ConnectionFragment : Fragment() {

    private var state = true
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
        binding.motionFragmentConnection.addTransitionListener(object :
            MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                if (currentId == R.id.start) {
                    binding.imageBack.rotation = 0F
                    binding.imagePlay2.visibility = View.GONE
                    binding.imageStop2.visibility = View.GONE
                } else {
                    binding.imageBack.rotation = 180F
                    binding.imagePlay2.visibility = View.VISIBLE
                    binding.imageStop2.visibility = View.VISIBLE
                }
                state = !state
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }

        })

    }

}