package com.sample.android.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.android.R
import com.sample.android.adapter.ProductListItemAdapter
import com.sample.android.base.BaseFragment
import com.sample.android.model.CategoryModel
import com.sample.android.model.ProductListModel
import com.sample.android.model.ProductListUIModel
import com.sample.android.preferences.ProductPreference
import com.sample.android.utils.OnCategorySelectionListener
import kotlinx.android.synthetic.main.fragment_product_list.*

class ProductListingFragment : BaseFragment(), OnCategorySelectionListener  {

    companion object {
        private val TAG = ProductListingFragment::class.java.simpleName

        fun newInstance():ProductListingFragment{
            val productListingFragment = ProductListingFragment()
            return productListingFragment
        }
    }
    private var productItemAdapter : ProductListItemAdapter?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar.visibility = View.VISIBLE

        setupRecyclerView()
    }

    override fun onCategorySelected(prodListUIModelList: MutableList<ProductListUIModel>?) {
        if (prodListUIModelList != null && prodListUIModelList.isNotEmpty()) {
            rvProductList.visibility = View.VISIBLE
            tvNoData.visibility = View.GONE
            productItemAdapter?.setProductList(prodListUIModelList)
        } else {
            rvProductList.visibility = View.GONE
            tvNoData.visibility = View.VISIBLE
            tvNoData.text = mBaseActivity?.getString(R.string.txt_no_data_available)
        }
    }


    /**
     * This method is used to render data on UI using recylerview & adapters
     */
    private fun setupRecyclerView() {
        try {
            progressBar.visibility = View.GONE
            mBaseActivity?.let { baseActivity ->
                val linearLayoutManager = LinearLayoutManager(baseActivity)
                rvProductList.layoutManager = linearLayoutManager
                productItemAdapter =
                    ProductListItemAdapter(baseActivity)
                rvProductList.adapter = productItemAdapter
            }
        } catch (e: Exception) {
            Log.e(TAG, "setupRecyclerView: " + e.message)
        }
    }
}