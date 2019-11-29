package com.takhaki.schoolfoodnavigator.register


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.databinding.FragmentNameRegisterBinding
import kotlinx.android.synthetic.main.fragment_name_register.*

/**
 * A simple [Fragment] subclass.
 */
class NameRegisterFragment : Fragment() {

    lateinit var viewModel: NameRegisterViewModel
    private val args: NameRegisterFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding: FragmentNameRegisterBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_name_register, container, false
        )

        viewModel = ViewModelProviders.of(this).get(NameRegisterViewModel::class.java)
        binding.nameRegisterViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        finishEditUserName.setOnClickListener {
            val content = viewModel.nameTextView.value.toString()
            val action =
                NameRegisterFragmentDirections.actionUserNameResigterFragmentToUserIconRegisterFragment(
                    content, args.teamID
                )
            findNavController().navigate(action)
        }
    }


}
