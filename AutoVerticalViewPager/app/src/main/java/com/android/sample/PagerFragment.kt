package com.android.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.autopager.AutoVerticalUiProvider
import com.android.autopager.R
import com.android.autopager.Util
import com.android.autopager.callback.AutoVerticalListener
import com.android.autopager.model.PagerModel
import kotlinx.android.synthetic.main.fragment_pager.*

class PagerFragment : BaseFragment() {

    private var autoVerticalUiProvider: AutoVerticalUiProvider? = null
    private var pagerPosition: Int? = 0
    private var autoVerticalListener: AutoVerticalListener? = null

    companion object {
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

    /**
     *
     * @param autoVerticalListener AutoVerticalListener
     */
    private fun setAutoVerticalListener(autoVerticalListener: AutoVerticalListener) {
        this.autoVerticalListener = autoVerticalListener
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
        val pagerModel = arguments?.getSerializable(Util.KEY_PAGE_NAME) as PagerModel
        pagerPosition = arguments?.getInt(Util.KEY_POSITION, 0)

        pageNameTv.text = pagerModel.pageName

        mainApp?.let {
            autoVerticalUiProvider =
                AutoVerticalUiProvider(
                    it,
                    autoVerticalListener,
                    view
                )
            autoVerticalUiProvider?.initProvider()
        }
        processNextHeadingOperation(pagerModel)
    }

    private fun processNextHeadingOperation(pagerModel: PagerModel?) {
        autoVerticalUiProvider?.hideNextPageCardView()

        if (!pagerModel?.nextPageName.isNullOrEmpty()) {
            autoVerticalUiProvider?.setNextPageContainer(pagerModel)
        }
    }

    private fun cancelNextStoryCounter() {
        autoVerticalUiProvider?.cancelNextPageCounter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        autoVerticalUiProvider?.onDestroyUiView()
    }
}