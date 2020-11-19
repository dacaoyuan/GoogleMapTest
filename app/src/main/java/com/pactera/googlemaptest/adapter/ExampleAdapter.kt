package com.pactera.googlemaptest.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.pactera.googlemaptest.R
import com.pactera.googlemaptest.model.ExampleBean

public class ExampleAdapter(mList: MutableList<ExampleBean>?) :
    BaseQuickAdapter<ExampleBean, BaseViewHolder>(
        R.layout.item_example,
        mList
    ) {

    override fun convert(helper: BaseViewHolder, item: ExampleBean) {
        helper.setText(R.id.tvNumber, "" + helper.layoutPosition + 1)

        helper.setText(R.id.tvName, "" + item.name)

        helper.addOnClickListener(R.id.tvdelete)
    }
}