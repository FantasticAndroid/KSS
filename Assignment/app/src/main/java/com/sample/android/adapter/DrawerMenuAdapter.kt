//package com.sample.android.adapter
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.graphics.Typeface
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.BaseExpandableListAdapter
//import android.widget.TextView
//import com.sample.android.R
//
//class DrawerMenuAdapter(
//    private var context: Context,
//    private var listGroupTitle: List<String>?,
//    private var menuChildItem: HashMap<String, List<String>>?
//) : BaseExpandableListAdapter() {
//
//
//    override fun getGroup(groupPosition: Int): Any {
//        return listGroupTitle?.get(groupPosition)!!
//    }
//
//    @SuppressLint("InflateParams")
//    override fun getGroupView(
//        groupPosition: Int,
//        isExpanded: Boolean,
//        convertView: View?,
//        parent: ViewGroup?
//    ): View {
//        var view = convertView
//        val title = getGroup(groupPosition) as String
//        if (convertView == null) {
//            val inflater =
//                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            view = inflater.inflate(R.layout.layout_drawer_menu_group, null)
//        }
//        val listTitleTextView = convertView?.findViewById(R.id.tvGroupTitle) as TextView
//        listTitleTextView.setTypeface(null, Typeface.BOLD)
//        listTitleTextView.text = title
//        return view!!
//    }
//
//    override fun getGroupCount(): Int {
//        return listGroupTitle?.size!!
//    }
//
//    override fun getChildrenCount(groupPosition: Int): Int {
//        return menuChildItem?.get(listGroupTitle?.get(groupPosition))?.size!!
//    }
//
//    override fun getChild(groupPosition: Int, childPosition: Int): Any {
//        return menuChildItem?.get(listGroupTitle?.get(groupPosition))?.get(childPosition)!!
//    }
//
//    override fun getGroupId(groupPosition: Int): Long {
//        return groupPosition.toLong()
//    }
//
//    @SuppressLint("InflateParams")
//    override fun getChildView(
//        groupPosition: Int,
//        childPosition: Int,
//        isLastChild: Boolean,
//        convertView: View?,
//        parent: ViewGroup?
//    ): View {
//        var view = convertView
//        val title = getChild(groupPosition, childPosition) as String
//        if (convertView == null) {
//            val inflater =
//                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            view = inflater.inflate(R.layout.layout_drawer_menu_child, null)
//        }
//        val childTitleTextView = convertView?.findViewById(R.id.tvChildTitle) as TextView
//        childTitleTextView.text = title
//        return view!!
//    }
//
//    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
//        return childPosition.toLong()
//    }
//
//    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
//        return true
//    }
//
//    override fun hasStableIds(): Boolean {
//        return false
//    }
//}