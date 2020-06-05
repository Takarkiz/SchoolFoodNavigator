package com.takhaki.schoolfoodnavigator.screen.register

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.databinding.FragmentCreateRoomBinding
import kotlinx.android.synthetic.main.fragment_create_room.*
import kotlinx.android.synthetic.main.fragment_create_room.view.*

class CreateRoomFragment : Fragment() {

    private lateinit var viewModel: CreateRoomViewModel
    private val args: CreateRoomFragmentArgs by navArgs()
    private lateinit var rootView: View

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
        rootView = binding.root

        val isJoin = args.join
        viewModel.putJoin(isJoin)
        rootView.qrPhotoButton.setOnClickListener {
            FragmentIntentIntegrator(
                this
            ).apply {
                setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                setBeepEnabled(false)
                setOrientationLocked(false)
                initiateScan()
            }
        }
        rootView.qrPhotoButton.isVisible = isJoin

        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            val readContent = result.contents
            if (readContent == null) {
                // キャンセル時
            } else {
                joinTeam(readContent)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onStart() {
        super.onStart()
        if (args.join) {
            textEditTeamName.inputType = InputType.TYPE_CLASS_NUMBER
        }

        viewModel.signInAuth()

        finishEditTeamName.setOnClickListener {
            if (args.join) {
                viewModel.contentEditText.value?.let { text ->
                    joinTeam(text)
                }
            } else {
                createNewTeam()
            }
        }
    }

    private fun joinTeam(text: String) {
        // 成功した場合は入力したIDを送るようにする
        viewModel.searchTeam(text.toInt()) { result ->
            if (result.isSuccess) {
                result.getOrNull()?.let { isSuccess ->
                    if (isSuccess) {
                        Snackbar.make(rootView, "チームに参加完了", Snackbar.LENGTH_SHORT).show()
                        val action =
                            CreateRoomFragmentDirections.actionCreateTeamFragmentToUserNameResigterFragment(
                                text.toInt()
                            )
                        findNavController().navigate(action)
                    } else {
                        Snackbar.make(rootView, "存在しないチームです", Snackbar.LENGTH_SHORT).show()
                    }
                }

            } else {
                Snackbar.make(rootView, "存在しないチームです", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun createNewTeam() {
        viewModel.createRoom { teamID ->
            val action =
                CreateRoomFragmentDirections.actionCreateTeamFragmentToUserNameResigterFragment(
                    teamID
                )
            findNavController().navigate(action)
        }
    }

}

class FragmentIntentIntegrator(private val fragment: Fragment) :
    IntentIntegrator(fragment.activity) {
    override fun startActivityForResult(intent: Intent, code: Int) {
        fragment.startActivityForResult(intent, code)
    }

}
