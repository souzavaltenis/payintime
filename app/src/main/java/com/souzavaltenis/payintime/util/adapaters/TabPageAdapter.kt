package com.souzavaltenis.payintime.util.adapaters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.souzavaltenis.payintime.view.fragments.ListContasFixasFragment
import com.souzavaltenis.payintime.view.fragments.ListContasNormaisFragment
import com.souzavaltenis.payintime.view.fragments.ResumoFragment
import com.souzavaltenis.payintime.util.interfaces.CallbackFragment
import com.souzavaltenis.payintime.util.interfaces.CallbackMenuConta

class TabPageAdapter(private val activity: FragmentActivity,
                     private val tabCount: Int,
                     private val callbacksFragmentsMap: HashMap<Int, CallbackFragment>,
                     private val callbackMenuConta: CallbackMenuConta
    ): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = tabCount

    override fun createFragment(position: Int): Fragment {

        return when (position){
            0 -> {
                activity.supportFragmentManager
                val listContasNormaisFragment = ListContasNormaisFragment(callbackMenuConta)
                callbacksFragmentsMap[position] = listContasNormaisFragment
                listContasNormaisFragment
            }
            1 -> {
                val listContasFixasFragment = ListContasFixasFragment()
                callbacksFragmentsMap[position] = listContasFixasFragment
                listContasFixasFragment
            }
            2 -> {
                val resumoFragment = ResumoFragment()
                callbacksFragmentsMap[position] = resumoFragment
                resumoFragment
            }
            else -> {
                val listContasNormaisFragment = ListContasNormaisFragment(callbackMenuConta)
                callbacksFragmentsMap[position] = listContasNormaisFragment
                listContasNormaisFragment
            }
        }
    }

}