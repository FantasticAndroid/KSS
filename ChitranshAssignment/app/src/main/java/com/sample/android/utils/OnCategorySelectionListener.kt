package com.sample.android.utils

import com.sample.android.model.ProductListUIModel

interface OnCategorySelectionListener {

    fun onCategorySelected(prodListUIModelList: MutableList<ProductListUIModel>?)
}