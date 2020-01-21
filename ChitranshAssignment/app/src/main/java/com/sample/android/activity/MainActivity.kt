package com.sample.android.activity

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.android.R
import com.sample.android.utils.Utils
import com.sample.android.adapter.DrawerMenuItemAdapter
import com.sample.android.base.BaseActivity
import com.sample.android.fragment.ProductListingFragment
import com.sample.android.model.CategoryModel
import com.sample.android.model.ProductListModel
import com.sample.android.model.ProductListUIModel
import com.sample.android.network.ApiClient
import com.sample.android.network.ApiInterface
import com.sample.android.preferences.ProductPreference
import com.sample.android.utils.OnCategorySelectionListener
import kotlinx.android.synthetic.main.layout_activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : BaseActivity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
    private var catProductMap = HashMap<String,MutableList<ProductListUIModel>>()
    //    private var mExpListAdapter: ExpandableListAdapter? = null
    //    private var mExpMenuChildTitle: HashMap<String, List<String>>? = null
    private var mActionBarDrawerToggle: ActionBarDrawerToggle? = null
    private var productListingFragment:ProductListingFragment?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_main)

        setupToolbar()

        if (Utils.isConnectedToNetwork(this)) {
            getProductDetailsFromServer()
        } else {
            Utils.showToastMessage(this, getString(R.string.no_internet_connection))
        }
    }

    private fun setupToolbar() {
        try {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setHomeButtonEnabled(true)
        } catch (e: Exception) {
            Log.e(TAG, "setupToolbar: " + e.message)
        }
    }

    /**
     * This method is used to get product list from server
     */
    private fun getProductDetailsFromServer() {
        try {
            progress.visibility = View.VISIBLE
            val call =
                ApiClient().getClient()?.create(ApiInterface::class.java)
                    ?.getProductListFromServer()
            call?.enqueue(object : Callback<ProductListModel?> {
                override fun onResponse(
                    call: Call<ProductListModel?>,
                    response: Response<ProductListModel?>
                ) {
                    val productListModel = response.body()
                    if (null != productListModel) {
                        prepareDataForUI(productListModel)
                        prepareRecyclerView(catProductMap.keys)
                        /*val categoryList = productListModel.categoryList
                        categoryList?.let { list ->
                            if (list.isNotEmpty()) {
                                ProductPreference.getInstance(this@MainActivity).addObjectToString(productListModel)
                                prepareDrawerMenuData(list)
                            }
                        }*/
                    }
                }

                override fun onFailure(call: Call<ProductListModel?>, t: Throwable) {
                    progress.visibility = View.GONE
                    Log.e(TAG, "onFailure: " + t.message)
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "getProductDetailsFromServer: " + e.message)
        }
    }

    /**
     * This method is used to prepare data for UI rendering
     */
    private fun prepareDataForUI(productListModel:ProductListModel) {
        try {
            // Parse category & it's product related stuff i.e. product, variant
            productListModel?.let { productListModel ->
                // Below code for parsing product related information to be shown in the list
                val categoryList = productListModel.categoryList
                if (null != categoryList && categoryList.isNotEmpty()) {
                    for (categoryModel in categoryList) {
                        var productListUIModelList: MutableList<ProductListUIModel> = ArrayList()

                        val productList = categoryModel.productListModel
                        if (null != productList && productList.isNotEmpty()) {
                            for (productModel in productList) {
                                val productRankingModel = ProductListUIModel.ProductRanking()
                                val productVariantList: MutableList<ProductListUIModel.ProductVariant> = ArrayList()
                                val variantList = productModel.variantList
                                if (null != variantList && variantList.isNotEmpty()) {
                                    for (variantModel in variantList) {
                                        val productVariant = ProductListUIModel.ProductVariant()
                                        productVariant.prodColorName = variantModel.color
                                        productVariant.prodSize = variantModel.size
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
                                productListUIModelList.add(productListUIModel)

                            }

                        }
                        catProductMap[categoryModel.name!!] = productListUIModelList
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "prepareDataForUI: " + e.message)
        }
    }


    /**
     * This method is used to prepare recylerview item
     *
     * @param menuTitleList: This list contains category names which will be showing in drawer menu as menu title
     */
    private fun prepareRecyclerView(menuTitleList: MutableSet<String>) {
        progress.visibility = View.GONE
        try {
            val linearLayoutManager = LinearLayoutManager(this)
            rvMenu.layoutManager = linearLayoutManager
            val drawerMenuItemAdapter = DrawerMenuItemAdapter(
                this,
                ArrayList(menuTitleList),
                object : DrawerMenuItemAdapter.OnMenuClickListener {
                    override fun onMenuItemClicked(menuTitle: String, position: Int) {
                        catProductMap.get(menuTitle)?.let {
                            toolbar.title = menuTitle
                            productListingFragment?.onCategorySelected(it)
                            drawerLayout.closeDrawer(GravityCompat.START)
                        }
                    }
                })
            rvMenu.adapter = drawerMenuItemAdapter

            setupActionBarDrawer()
        } catch (e: Exception) {
            Log.e(TAG, "prepareRecyclerView: " + e.message)
        }
    }

    /**
     * This method is used to setup actionbar drawer toggle state
     */
    private fun setupActionBarDrawer() {
        try {
            mActionBarDrawerToggle = ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.app_name,
                R.string.app_name
            )
            mActionBarDrawerToggle?.let { actionBarDrawerToggle ->
                drawerLayout.addDrawerListener(actionBarDrawerToggle)
                actionBarDrawerToggle.syncState()
            }

            loadFragment(catProductMap.keys.first())

        } catch (e: Exception) {
            Log.e(TAG, "prepareRecyclerView: " + e.message)
        }
    }

    /**
     * This method is used to create fragment using product list data
     *
     * @param menuTitle To show as a title
     */
    private fun loadFragment(menuTitle: String) {
        try {
            productListingFragment = ProductListingFragment.newInstance()
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction().replace(R.id.content_frame, productListingFragment!!).commit()
            toolbar.title = menuTitle
            drawerLayout.closeDrawer(GravityCompat.START)
            productListingFragment?.onCategorySelected(catProductMap[menuTitle])
        } catch (e: Exception) {
            Log.e(TAG, "loadFragment: " + e.message)
        }
    }


    //        mExpMenuChildTitle = getData()
//        mExpGroupTitle = mExpMenuChildTitle?.keys?.let { ArrayList<String>(it) }
//        mExpListAdapter = DrawerMenuAdapter(this, mExpGroupTitle, mExpMenuChildTitle)
//        lvMenu.setAdapter(mExpListAdapter)
//
//        lvMenu.setOnGroupExpandListener {
//            Toast.makeText(this, mExpGroupTitle?.get(it) + " List Expanded.", Toast.LENGTH_SHORT)
//                .show()
//        }
//
//        lvMenu.setOnGroupCollapseListener {
//            Toast.makeText(this, mExpGroupTitle?.get(it) + " List Collapsed.", Toast.LENGTH_SHORT)
//                .show()
//        }

//    fun getData(): HashMap<String, List<String>>? {
//        val expandableListDetail =
//            HashMap<String, List<String>>()
//        val cricket: MutableList<String> = ArrayList()
//        cricket.add("India")
//        cricket.add("Pakistan")
//        cricket.add("Australia")
//        cricket.add("England")
//        cricket.add("South Africa")
//        val football: MutableList<String> = ArrayList()
//        football.add("Brazil")
//        football.add("Spain")
//        football.add("Germany")
//        football.add("Netherlands")
//        football.add("Italy")
//        val basketball: MutableList<String> = ArrayList()
//        basketball.add("United States")
//        basketball.add("Spain")
//        basketball.add("Argentina")
//        basketball.add("France")
//        basketball.add("Russia")
//        expandableListDetail["CRICKET TEAMS"] = cricket
//        expandableListDetail["FOOTBALL TEAMS"] = football
//        expandableListDetail["BASKETBALL TEAMS"] = basketball
//        return expandableListDetail
//    }
}
