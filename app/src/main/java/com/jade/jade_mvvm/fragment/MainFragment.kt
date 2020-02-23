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
        view.findViewById<View>(R.id.recyclerView_load_data).setOnClickListener {
            val intent = Intent(activity, VenueActivity::class.java)
            intent.putExtra(VenueActivity.KEY_TYPE, VenueActivity.TYPE_RECYCLER_VIEW_LOAD_DATA)
            startActivity(intent)
        }
    }

    companion object {

        fun newInstance(): MainFragment = MainFragment()
    }
}