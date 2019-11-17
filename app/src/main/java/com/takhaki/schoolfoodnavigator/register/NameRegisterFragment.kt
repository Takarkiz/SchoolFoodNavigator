package com.takhaki.schoolfoodnavigator.register


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.takhaki.schoolfoodnavigator.R
import kotlinx.android.synthetic.main.fragment_name_register.*

/**
 * A simple [Fragment] subclass.
 */
class NameRegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_name_register, container, false)
    }

    override fun onStart() {
        super.onStart()
        finishEditUserName.setOnClickListener {
            findNavController().navigate(R.id.action_userNameResigterFragment_to_userIconRegisterFragment)
        }
    }


}
