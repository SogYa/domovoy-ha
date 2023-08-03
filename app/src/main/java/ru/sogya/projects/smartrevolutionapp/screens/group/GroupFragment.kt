package ru.sogya.projects.smartrevolutionapp.screens.group

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator
import com.sogya.domain.models.StateGroupDomain
import com.sogya.domain.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import ru.sogya.projects.smartrevolutionapp.R
import ru.sogya.projects.smartrevolutionapp.databinding.FragmentGroupBinding

@AndroidEntryPoint
class GroupFragment : Fragment(R.layout.fragment_dashboard), GroupAdapter.OnGroupClickListener {
    private lateinit var binding: FragmentGroupBinding
    private val vm: GroupVM by viewModels()
    private lateinit var adapter: GroupViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            addFab.shrink()
            addFab.setOnClickListener {
//                GroupBottomSheetFragment()
//                    .show(childFragmentManager, GroupBottomSheetFragment().tag)
                if (!addFab.isExtended) {
                    addGroupFab.show()
                    addStateFab.show()
                    addFab.extend()
                    setLayoutParams(addGroupFab, true)
                    setLayoutParams(addStateFab, true)
                } else {
                    addGroupFab.hide()
                    addStateFab.hide()
                    addFab.shrink()
                    setLayoutParams(addGroupFab, false)
                    setLayoutParams(addStateFab, false)

                }
            }
            adapter = GroupViewPagerAdapter(this@GroupFragment)
            groupViewPager.adapter = adapter
        }
    }

    private fun setLayoutParams(
        fab: ExtendedFloatingActionButton,
        show: Boolean
    ) {
        val layoutParams = fab.layoutParams as CoordinatorLayout.LayoutParams
        var animationShow: Animation? = null
        var animationHide: Animation? = null
        val resultAnimation: Animation?
        var margin: Double? = null

        if (fab.id == binding.addGroupFab.id) {
            margin = 1.25
            animationShow =
                AnimationUtils.loadAnimation(requireContext(), R.anim.fab_add_group_show)
            animationHide =
                AnimationUtils.loadAnimation(requireContext(), R.anim.fab_add_group_hide)
        } else if (fab.id == binding.addStateFab.id) {
            margin = 2.5
            animationShow =
                AnimationUtils.loadAnimation(requireContext(), R.anim.fab_add_state_show)
            animationHide =
                AnimationUtils.loadAnimation(requireContext(), R.anim.fab_add_state_hide)
        }
        if (show) {
            resultAnimation =
                animationShow
            layoutParams.bottomMargin += (fab.height * margin!!).toInt()
        } else {
            resultAnimation =
                animationHide
            layoutParams.bottomMargin -= (fab.height * margin!!).toInt()
        }
        fab.layoutParams = layoutParams
        fab.startAnimation(resultAnimation)
        fab.isClickable = show
    }

    override fun onResume() {
        super.onResume()
        vm.getGroupsLiveData().observe(viewLifecycleOwner) {
            binding.apply {
                adapter.updateGroupList(it)
                TabLayoutMediator(groupTabLayout, groupViewPager) { tab, position ->
                    if (it.isNotEmpty()) {
                        val group = it[position]
                        tab.text = group.groupTag
                    }
                }.attach()
                Log.d("LiveDataGroup", it.toString())
            }
        }
    }

    override fun onClick(stateGroupDomain: StateGroupDomain) {
        val bundle = Bundle()
        bundle.putInt(Constants.GROUP_ID, stateGroupDomain.groupId)
        Log.d("GroupId", stateGroupDomain.groupId.toString())
        findNavController().navigate(
            R.id.action_homeFragment_to_dashboardFragment,
            bundle
        )
    }

    override fun onLongClick(stateGroupDomain: StateGroupDomain) {
        TODO("Not yet implemented")
    }
}