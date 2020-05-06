package com.jstudio.app.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jstudio.app.R
import com.jstudio.app.entity.Record
import com.jstudio.jkit.adapter.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {

    companion object {
        fun startListActivity(context: Context) = context.startActivity(Intent(context, ListActivity::class.java))

        class RecordAdapter(diffCallback: DiffUtil.ItemCallback<Record>) : PagedListAdapter<Record, BaseRecyclerAdapter.Holder>(diffCallback) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerAdapter.Holder {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_record, parent, false)
                return BaseRecyclerAdapter.Holder(itemView, viewType)
            }

            override fun onBindViewHolder(holder: BaseRecyclerAdapter.Holder, position: Int) {
                holder.setTextByString(R.id.diagnosis, "${position + 1}" + (getItem(position)?.diagnosis ?: "空空"))
            }
        }
    }

    private lateinit var listViewModel: ListViewModel
    private lateinit var adapter: RecordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        adapter = RecordAdapter(object : DiffUtil.ItemCallback<Record>() {
            override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean = true
            override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean = true
        })
        listViewModel.getConvertList().observe(this, Observer {
            adapter.submitList(it)
        })
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recyclerView.adapter = adapter
    }
}