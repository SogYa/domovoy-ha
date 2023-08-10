package ru.sogya.projects.domovoy.screens.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.sogya.projects.domovoy.R
import ru.sogya.projects.domovoy.databinding.FragmentContactsBinding

class ContactsFragment:Fragment(R.layout.fragment_contacts) {
    private var _binding:FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private val vm:ContcatsVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}