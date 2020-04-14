package com.takhaki.schoolfoodnavigator.memberList.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.memberList.view_model.MemberListViewModel
import kotlinx.android.synthetic.main.activity_member_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class MemberListActivity : AppCompatActivity() {

    companion object {

        /**
         * 遷移用のインテントを作成する
         */
        fun makeIntent(activity: AppCompatActivity): Intent {
            return Intent(activity, MemberListActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = resources.getString(R.string.member_list_title)

        viewModel.activity(this)
        lifecycle.addObserver(viewModel)

        val adapter = MemberListAdapter()
        adapter.setOnClickListener(viewModel)
        membersList.adapter = adapter

        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        membersList.addItemDecoration(itemDecoration)


        viewModel.memberList.observe({ lifecycle }, {
            adapter.setMemberList(it)
        })

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Private

    private val viewModel: MemberListViewModel by viewModel()
}
