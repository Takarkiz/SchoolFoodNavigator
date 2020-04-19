package com.takhaki.schoolfoodnavigator.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.takhaki.schoolfoodnavigator.MainActivity
import com.takhaki.schoolfoodnavigator.Model.CompanyData
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.Repository.UserAuth
import kotlinx.android.synthetic.main.fragment_create_room.*
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
        joinTeamButton.setOnClickListener {
            val content = true
            val action = StartFragmentDirections.actionStartFragmentToCreateTeamFragment(content)
            findNavController().navigate(action)
        }
        
        createTeamButton.setOnClickListener {
            val content = false
            val action = StartFragmentDirections.actionStartFragmentToCreateTeamFragment(content)
            findNavController().navigate(action)
        }
        context?.let { context ->
            val companyID = CompanyData.getCompanyId(context)

            // 既にログインしている かつ カンパニーIDが端末に保存済の場合はメインメニューに移動
            if (checkLoginUser() && companyID != 0) {
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }

    }

    private fun checkLoginUser(): Boolean {
        val context = requireContext()
        val auth = UserAuth(context)
        return auth.currentUser != null
    }

}
