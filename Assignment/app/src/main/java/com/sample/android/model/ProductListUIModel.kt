package com.sample.android.model

class ProductListUIModel(prodName: String?, prodRankingModel: ProductRanking?, prodVariantList: List<ProductVariant>?) {
    var productName: String? = prodName
    var productRankingModel: ProductRanking? = prodRankingModel
    var productVariantList: List<ProductVariant>? = prodVariantList

    class ProductRanking {
        var mostViewed: Int? = 0
        var mostOrdered: Int? = 0
        var mostShared: Int? = 0
    }

    class ProductVariant {
        var prodColorName: String? = null
        var prodSize: String? = ""
        var prodPrice: Int? = 0
    }
}