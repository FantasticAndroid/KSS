package com.sample.android.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.android.R
import com.sample.android.model.ProductListUIModel

class ProductListItemAdapter(
    private val context: Context) : RecyclerView.Adapter<ProductListItemAdapter.ProductListViewHolder>() {
    companion object {
        private val TAG = ProductListItemAdapter::class.java.simpleName
    }

    private val mInflater = LayoutInflater.from(context)
    private var productListUIModelList: MutableList<ProductListUIModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        val view = mInflater.inflate(R.layout.layout_adapter_product_list_item, parent, false)
        return ProductListViewHolder(view)
    }

    fun setProductList(prodListUIModelList: MutableList<ProductListUIModel>?){
        prodListUIModelList?.let {
            notifyItemRangeRemoved(0, productListUIModelList.size)
            productListUIModelList.clear()
            productListUIModelList.addAll(it)
            notifyItemRangeInserted(0, productListUIModelList.size)
        }
    }

    override fun getItemCount(): Int {
        return productListUIModelList?.size ?: run { 0 }
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        try {
            val productListUIModel = productListUIModelList?.get(position)
            holder.tvProductName.text = productListUIModel?.productName
            holder.tvMostViewed.text =
                productListUIModel?.productRankingModel?.mostViewed.toString()
            holder.tvMostOrdered.text =
                productListUIModel?.productRankingModel?.mostOrdered.toString()
            holder.tvMostShared.text =
                productListUIModel?.productRankingModel?.mostShared.toString()
            prepareVariantListAdapter(holder, productListUIModel)
        } catch (e: Exception) {
            Log.e(TAG, "onBindViewHolder: " + e.message)
        }
    }

    /**
     * This method is used to render product variant list using adapter
     *
     * @param holder
     * @param productListUIModel
     */
    private fun prepareVariantListAdapter(
        holder: ProductListViewHolder,
        productListUIModel: ProductListUIModel?
    ) {
        try {
            val manager =  LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
            holder.rvProductVariant.layoutManager = manager
            val productVariantItemAdapter =
                ProductListVariantItemAdapter(context, productListUIModel?.productVariantList)
            holder.rvProductVariant.adapter = productVariantItemAdapter
        } catch (e: Exception) {
            Log.e(TAG, "prepareVariantListAdapter: " + e.message)
        }
    }

    class ProductListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvProductName = view.findViewById(R.id.tvProductName) as TextView
        var tvMostViewed = view.findViewById(R.id.tvMostViewedCount) as TextView
        var tvMostOrdered = view.findViewById(R.id.tvMostOrderedCount) as TextView
        var tvMostShared = view.findViewById(R.id.tvMostSharedCount) as TextView
        var rvProductVariant = view.findViewById(R.id.rvProductVariant) as RecyclerView
    }
}