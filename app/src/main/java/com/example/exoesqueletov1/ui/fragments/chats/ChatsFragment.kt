package com.example.exoesqueletov1.ui.fragments.chats

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.models.PatientModel
import com.example.exoesqueletov1.databinding.FragmentChatsBinding
import com.example.exoesqueletov1.ui.fragments.chats.adapter.ChatsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private lateinit var viewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        binding = FragmentChatsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_scale_up)
        binding.addPatient.startAnimation(animation)
        val list = mutableListOf<PatientModel>()
        val adapter = ChatsAdapter(list) { data ->
            viewModel.savePatient(data.id, data.name)
        }
        binding.recyclerChats.layoutManager = LinearLayoutManager(context, VERTICAL, false)
        binding.recyclerChats.setHasFixedSize(true)
        binding.recyclerChats.adapter = adapter
        viewModel.user.observe(viewLifecycleOwner) {
            list.clear()
            list.addAll(it)
            adapter.notifyDataSetChanged()
        }
        binding.addPatient.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_message_to_patientFragment)
        }
    }

}