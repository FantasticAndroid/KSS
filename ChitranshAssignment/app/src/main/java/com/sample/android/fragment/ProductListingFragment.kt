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
import com.sample.android.model.ProductListModel
import com.sample.android.model.ProductListUIModel
import com.sample.android.preferences.ProductPreference
import kotlinx.android.synthetic.main.fragment_product_list.*

class ProductListingFragment : BaseFragment() {

    companion object {
        private val TAG = ProductListingFragment::class.java.simpleName
    }

    private var mProductListModel: ProductListModel? = null
    private var mProductListUIModelList: MutableList<ProductListUIModel>? = ArrayList()

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
        prepareDataForUI()
        setupRecyclerView()
    }

    /**
     * This method is used to prepare data for UI rendering
     */
    private fun prepareDataForUI() {
        try {
            mProductListModel =
                mBaseActivity?.let { ProductPreference.getInstance(it).getObjectFromString() }

//        val productRankingList: MutableList<ProductListUIModel.ProductRanking> = ArrayList()
            val productRankingModel = ProductListUIModel.ProductRanking()
            val productVariantList: MutableList<ProductListUIModel.ProductVariant> = ArrayList()

            // Parse category & it's product related stuff i.e. product, variant
            mProductListModel?.let { productListModel ->
                // Below code for parsing product related information to be shown in the list
                val categoryList = productListModel.categoryList
                if (null != categoryList && categoryList.isNotEmpty()) {
                    for (categoryModel in categoryList) {
                        val productList = categoryModel.productListModel
                        if (null != productList && productList.isNotEmpty()) {
                            for (productModel in productList) {
                                val variantList = productModel.variantList
                                if (null != variantList && variantList.isNotEmpty()) {
                                    for (variantModel in variantList) {
                                        val productVariant = ProductListUIModel.ProductVariant()
                                        productVariant.prodColorName = variantModel.color
                                        productVariant.prodSize = variantModel.size as Int
                                        productVariant.prodPrice = variantModel.price
                                        productVariantList.add(productVariant)
                                    }
                                }
                                // Below code for parsing ranking of products to be shown in the list
                                val rankingList = productListModel.rankingList
                                if (null != rankingList && rankingList.isNotEmpty()) {
                                    val mostViewedProdList = rankingList[0].productDetailList
                                    val mostOrderedProdList = rankingList[1].productDetailList
                                    val mostSharedProdList = rankingList[2].productDetailList
                                    //                                val productRankingModel = ProductListUIModel.ProductRanking()
                                    if (null != mostViewedProdList && mostViewedProdList.isNotEmpty()) {
                                        for (mostViewedProdModel in mostViewedProdList) {
                                            if (mostViewedProdModel.id == productModel.id) {
                                                productRankingModel.mostViewed =
                                                    mostViewedProdModel.viewCount
                                                //                                            productRankingList.add(productRankingModel)
                                            }
                                        }
                                    }
                                    if (null != mostOrderedProdList && mostOrderedProdList.isNotEmpty()) {
                                        for (mostOrderedModel in mostOrderedProdList) {
                                            if (mostOrderedModel.id == productModel.id) {
                                                productRankingModel.mostOrdered =
                                                    mostOrderedModel.orderCount
                                                //                                            productRankingList.add(productRankingModel)
                                            }
                                        }
                                    }
                                    if (null != mostSharedProdList && mostSharedProdList.isNotEmpty()) {
                                        for (mostSharedModel in mostSharedProdList) {
                                            if (mostSharedModel.id == productModel.id) {
                                                productRankingModel.mostShared =
                                                    mostSharedModel.shareCount
                                                //                                            productRankingList.add(productRankingModel)
                                            }
                                        }
                                    }
                                }
                                val productListUIModel =
                                    ProductListUIModel(
                                        productModel.name,
                                        productRankingModel,
                                        productVariantList
                                    )
                                mProductListUIModelList?.add(productListUIModel)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "prepareDataForUI: " + e.message)
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
                val productItemAdapter =
                    ProductListItemAdapter(baseActivity, mProductListUIModelList)
                rvProductList.adapter = productItemAdapter
            }
        } catch (e: Exception) {
            Log.e(TAG, "setupRecyclerView: " + e.message)
        }
    }
}