package com.sample.android.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sample.android.R
import com.sample.android.model.ProductListUIModel

class ProductListVariantItemAdapter(
    private val context: Context,
    private val variantList: List<ProductListUIModel.ProductVariant>?
) : RecyclerView.Adapter<ProductListVariantItemAdapter.ProductListVariantItemViewHolder>() {
    companion object {
        private val TAG = ProductListVariantItemAdapter::class.java.simpleName
    }

    private val mInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListVariantItemViewHolder {
        val view =
            mInflater.inflate(R.layout.layout_adapter_product_list_variant_item, parent, false)
        return ProductListVariantItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return variantList?.size ?: run { 0 }
    }

    override fun onBindViewHolder(holder: ProductListVariantItemViewHolder, position: Int) {
        try {
            val variantModel = variantList?.get(position)
            holder.tvColorName.text = variantModel?.prodColorName
            variantModel?.prodSize?.let{
                val size = "S: $it"
                holder.tvSize.text = size
            }?:run { holder.tvSize.text = "-" }

            val prodPrice = "P: " + variantModel?.prodPrice + "/-"
            holder.tvPrice.text = prodPrice
        } catch (e: Exception) {
            Log.e(TAG, "onBindViewHolder: " + e.message)
        }
    }

    class ProductListVariantItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvColorName = view.findViewById(R.id.tvColorName) as TextView
        var tvSize = view.findViewById(R.id.tvSize) as TextView
        var tvPrice = view.findViewById(R.id.tvPrice) as TextView
    }
}