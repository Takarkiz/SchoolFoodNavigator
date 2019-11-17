package com.takhaki.schoolfoodnavigator.mainList


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
import kotlinx.android.synthetic.main.fragment_main_list.*


class MainListFragment : Fragment() {

    private lateinit var viewModel: ShopListViewModel

    companion object {
        fun newInstance(tabNumber: Int): MainListFragment = MainListFragment().apply {
            arguments = Bundle().apply {
                putString("text", tabNumber.toString())
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

        //val application = requireNotNull(this.activity).application
        viewModel = ViewModelProviders.of(
            this
        ).get(ShopListViewModel::class.java)
        binding.lifecycleOwner = this
        binding.listViewModel = viewModel
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = ShopListAdapter()
        viewModel.loadListShopItem()
        adapter.data = listOf(
            ShopListItemModel(
                id = "",
                name = "帯広豚丼王国",
                shopGenre = "豚丼ハウス",
                imageUrl = "https://1.bp.blogspot.com/-ZwMp9KerB-0/XRHQ2s89EFI/AAAAAAABTaY/i1QNxsIg2D8wHRN3M-vDyDTbefi3mLi_wCLcBGAs/s800/food_butadon_obihiro.png",
                score = 3.5f
            )
        )
        shopList.adapter = adapter


//        viewModel.shopItemList.observe(this, Observer { items ->
//            adapter.data = items
//            shopList.adapter = adapter
//        })

        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        shopList.addItemDecoration(itemDecoration)
    }
}
