package com.takhaki.schoolfoodnavigator.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.takhaki.schoolfoodnavigator.MainActivity
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.Repository.UserAuth
import kotlinx.android.synthetic.main.fragment_start.*


class StartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onStart() {
        super.onStart()
        nextFabButton.setOnClickListener {
            findNavController().navigate(R.id.action_start_fragment_to_userNameResigterFragment)
        }

        if (checkLoginUser()) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }
    }

    private fun checkLoginUser(): Boolean {
        val auth = UserAuth()
        return auth.currentUser != null
    }

}
