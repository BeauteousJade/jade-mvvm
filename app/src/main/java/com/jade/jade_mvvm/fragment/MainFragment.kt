package com.jade.jade_mvvm.fragment

import android.content.Intent
import android.view.View
import com.jade.jade_mvvm.R
import com.jade.jade_mvvm.activity.VenueActivity
import com.jade.jade_mvvm.viewModel.MainViewModel
import com.jade.mvvm.fragment.BaseFragment

class MainFragment : BaseFragment<MainViewModel>() {
    override fun getLayoutId() = R.layout.fragment_main

    override fun onPrepareView(view: View) {
        super.onPrepareView(view)
        view.findViewById<View>(R.id.recyclerView_load_data_with_position).setOnClickListener {
            val intent = Intent(activity, VenueActivity::class.java)
            intent.putExtra(VenueActivity.KEY_TYPE, VenueActivity.LOAD_DATA_WITH_POSITION)
            startActivity(intent)
        }
        view.findViewById<View>(R.id.recyclerView_load_data_with_key).setOnClickListener {
            val intent = Intent(activity, VenueActivity::class.java)
            intent.putExtra(VenueActivity.KEY_TYPE, VenueActivity.LOAD_DATA_WITH_KEY)
            startActivity(intent)
        }
    }

    companion object {

        fun newInstance(): MainFragment = MainFragment()
    }
}