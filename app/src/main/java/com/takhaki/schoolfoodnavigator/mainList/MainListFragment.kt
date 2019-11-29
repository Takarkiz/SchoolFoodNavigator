package com.takhaki.schoolfoodnavigator.mainList


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.databinding.FragmentMainListBinding
import com.takhaki.schoolfoodnavigator.detail.DetailActivity
import kotlinx.android.synthetic.main.fragment_main_list.*


class MainListFragment : Fragment() {

    private lateinit var viewModel: ShopListViewModel
    private var tabIndex = 0

    companion object {
        fun newInstance(tabNumber: Int): MainListFragment = MainListFragment().apply {
            arguments = Bundle().apply {
                tabIndex = tabNumber
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentMainListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main_list, container, false
        )

        viewModel = ViewModelProviders.of(
            this
        ).get(ShopListViewModel::class.java)
        viewModel.putTabNumber(tabIndex)
        binding.lifecycleOwner = this
        binding.listViewModel = viewModel
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = ShopListAdapter()
        viewModel.loadListShopItem()

        viewModel.shopItemList.observe(this, Observer { items ->
            adapter.data = items
            shopList.adapter = adapter
        })

        adapter.setOnItemClickListener(object : ShopListAdapter.OnItemClickListener {
            override fun onClick(view: View, shopId: String, name: String) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("shopId", shopId)
                intent.putExtra("shopName", name)
                startActivity(intent)
            }
        })

        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        shopList.addItemDecoration(itemDecoration)
    }
}
