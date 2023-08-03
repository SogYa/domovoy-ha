package ru.sogya.projects.smartrevolutionapp.screens.group

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sogya.domain.models.StateGroupDomain
import com.sogya.domain.utils.Constants
import ru.sogya.projects.smartrevolutionapp.screens.dashboards.DashboardFragment

class GroupViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private var groupList = ArrayList<StateGroupDomain>()
    override fun getItemCount(): Int {
        return groupList.size
    }

    override fun createFragment(position: Int): Fragment {
        val group = groupList[position]
        val fragment = DashboardFragment()
        fragment.arguments = Bundle().apply {
            putInt(Constants.GROUP_ID, group.groupId)
        }
        return fragment
    }

    fun updateGroupList(newGroupList: List<StateGroupDomain>) {
        groupList.clear()
        notifyItemChanged(1)
        groupList.addAll(newGroupList)
        notifyItemRangeChanged(0, groupList.size)
    }
}