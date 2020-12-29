package com.jstudio.example

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jstudio.jkit.adapter.ListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_rv_number.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = Adapter(this)
        recyclerView.adapter = adapter

        addBatch.setOnClickListener { adapter.addData(3, arrayListOf("abc", "bca", "cab", "ddd", "eeeeeeeee")) }

        removeLast.setOnClickListener { adapter.removeData(adapter.collection!!.lastIndex) }
    }

    companion object {
        class Adapter(context: Context) : ListAdapter<String>(context, null) {
            override fun setViewLayout(type: Int): Int = R.layout.item_rv_number

            override fun fillContent(holder: Holder, position: Int, entity: String) {
                holder.num.text = position.toString()
                holder.content.text = entity
                holder.add.setOnClickListener {
                    addData(holder.adapterPosition, arrayListOf("add from $position"))
                }
                holder.remove.setOnClickListener {
                    removeData(holder.adapterPosition)
                }
            }
        }
    }
}
