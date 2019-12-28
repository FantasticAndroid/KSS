package com.android.autopager

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.autopager.AutoVerticalUiProvider
import com.android.autopager.R
import com.android.autopager.Util
import com.android.autopager.callback.AutoVerticalListener
import com.android.autopager.model.PagerModel
import kotlinx.android.synthetic.main.fragment_pager.*

abstract class PagerBaseFragment : Fragment() {

    protected var autoVerticalUiProvider: AutoVerticalUiProvider? = null
    private var autoVerticalListener: AutoVerticalListener? = null
    protected var appContext: Context? = null

    companion object {
        private val TAG = PagerBaseFragment::class.java.simpleName
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContext = context.applicationContext
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pager, container, false)
    }

    /**
     *
     * @param autoVerticalListener AutoVerticalListener
     */
    fun setAutoVerticalListener(autoVerticalListener: AutoVerticalListener) {
        this.autoVerticalListener = autoVerticalListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            autoVerticalUiProvider = AutoVerticalUiProvider(
                appContext!!, autoVerticalListener, view
            )
            autoVerticalUiProvider?.initProvider()
        } catch (e: Exception) {
            Log.e(TAG, "onViewCreated()".plus(e.message))
        }
    }

    /**
     * Call this method to cancel counter manually for ex: if user click on any pager item to redirect to any other page.
     */
    protected fun cancelNextPageCounter() {
        autoVerticalUiProvider?.cancelNextPageCounter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        autoVerticalUiProvider?.onDestroyUiView()
    }
}