package com.android.sample

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.autopager.AutoVerticalUiProvider
import com.android.autopager.PagerBaseFragment
import com.android.autopager.R
import com.android.autopager.Util
import com.android.autopager.callback.AutoVerticalListener
import com.android.autopager.model.PagerModel
import kotlinx.android.synthetic.main.fragment_pager.*

class PagerFragment : PagerBaseFragment() {

    private var pagerPosition: Int? = 0

    companion object {
        private val TAG = PagerFragment::class.java.simpleName
        fun newInstance(
            pagerModel: PagerModel,
            pagerPosition: Int,
            autoVerticalListener: AutoVerticalListener
        ): PagerFragment {
            val bundle = Bundle()
            bundle.putSerializable(Util.KEY_PAGE_NAME, pagerModel)
            bundle.putInt(Util.KEY_POSITION, pagerPosition)

            val fragment = PagerFragment()
            fragment.arguments = bundle
            fragment.setAutoVerticalListener(autoVerticalListener)
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            val pagerModel = arguments?.getSerializable(Util.KEY_PAGE_NAME) as PagerModel
            pagerPosition = arguments?.getInt(Util.KEY_POSITION, 0)
            pageNameTv.text = pagerModel.pageName
            processNextHeadingOperation(pagerModel)
        } catch (e: Exception) {
            Log.e(TAG, "onViewCreated()".plus(e.message))
        }
    }

    private fun processNextHeadingOperation(pagerModel: PagerModel?) {
        try {
            autoVerticalUiProvider?.hideNextPageCardView()

            if (!pagerModel?.nextPageName.isNullOrEmpty()) {
                autoVerticalUiProvider?.setNextPageContainer(pagerModel)
            }
        } catch (e: Exception) {
            Log.e(TAG, "processNextHeadingOperation()".plus(e.message))
        }
    }
}