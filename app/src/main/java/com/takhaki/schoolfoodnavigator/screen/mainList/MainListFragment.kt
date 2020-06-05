package com.takhaki.schoolfoodnavigator.screen.mainList


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.databinding.FragmentMainListBinding
import kotlinx.android.synthetic.main.fragment_main_list.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


class MainListFragment : Fragment() {

    private val viewModel: ShopListViewModelContract by sharedViewModel<ShopListViewModelBase>()
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
        viewModel.putTabNumber(tabIndex)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = ShopListAdapter()

        viewModel.shopItemLists.observe({ lifecycle }, { items ->
            adapter.data = items[tabIndex]
            shopList.adapter = adapter
        })

        adapter.setOnItemClickListener(object : ShopListAdapter.OnItemClickListener {
            override fun onClick(view: View, shopId: String, name: String) {
                viewModel.didTapShopItem(shopId, name)
            }
        })

        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        shopList.addItemDecoration(itemDecoration)
    }
}
