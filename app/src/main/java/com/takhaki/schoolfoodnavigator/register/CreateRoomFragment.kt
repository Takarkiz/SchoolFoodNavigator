package com.takhaki.schoolfoodnavigator.register

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.databinding.FragmentCreateRoomBinding
import kotlinx.android.synthetic.main.fragment_create_room.*

class CreateRoomFragment : Fragment() {

    private lateinit var viewModel: CreateRoomViewModel
    private val args: CreateRoomFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding: FragmentCreateRoomBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_room, container, false
        )

        viewModel = ViewModelProviders.of(this).get(CreateRoomViewModel::class.java)
        binding.createRoomViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.putJoin(args.join)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        if (args.join) {
            textEditTeamName.inputType = InputType.TYPE_CLASS_NUMBER
        }

        viewModel.signInAuth()

        finishEditTeamName.setOnClickListener {
            if (args.join) {
                // 成功した場合は入力したIDを送るようにする
                viewModel.contentEditText.value?.let { text ->
                    viewModel.searchTeam(text.toInt()) { result ->
                        if (result.isSuccess) {
                            Snackbar.make(it, "チームに参加完了", Snackbar.LENGTH_SHORT).show()
                            val action = CreateRoomFragmentDirections.actionCreateTeamFragmentToUserNameResigterFragment(
                                text.toInt()
                            )
                            findNavController().navigate(action)
                        } else {
                            Snackbar.make(it, "存在しないチームです", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }

            } else {
                viewModel.createRoom { teamID ->
                    val action =
                        CreateRoomFragmentDirections.actionCreateTeamFragmentToUserNameResigterFragment(
                            teamID
                        )
                    findNavController().navigate(action)
                }
            }
        }
    }

}
