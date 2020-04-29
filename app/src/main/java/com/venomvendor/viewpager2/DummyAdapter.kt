package com.venomvendor.viewpager2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.children
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.venomvendor.viewpager2.DummyAdapter.ViewType.values

/**
 * This adapter is to generate dummy views
 */
class DummyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class ViewType(@LayoutRes val layout: Int) {
        PAGER(R.layout.junk_pager),
        TEXT(R.layout.junk_text)
    }

    override fun getItemViewType(position: Int) = position

    override fun getItemCount() = values().size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (val type = values()[viewType]) {
            ViewType.PAGER -> ViewHolderPager(inflate(type.layout, parent))
            ViewType.TEXT -> ViewHolderText(inflate(type.layout, parent))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (values()[position] == ViewType.PAGER) {
            (holder as ViewHolderPager).onBind()
        }
    }

    private fun inflate(@LayoutRes resource: Int, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(resource, parent, false)
    }

    internal class ViewHolderText(view: View) : RecyclerView.ViewHolder(view)

    internal class ViewHolderPager(private val view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var viewPager2: ViewPager2

        fun onBind() {
            viewPager2 = view.findViewById(R.id.view_pager2)

            val list = (1..5).map { it.toString() }
            val adapter = PagerAdapter()
            adapter.submitList(list)

            viewPager2.adapter = adapter

            val innerRecyclerView = viewPager2.children.first { it is RecyclerView } as RecyclerView
            with(innerRecyclerView) {
                // https://issuetracker.google.com/issues/150533070
                overScrollMode = View.OVER_SCROLL_NEVER
            }
        }
    }

    internal class PagerAdapter :
        ListAdapter<String, ViewHolderText>(object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderText {
            val inflater = LayoutInflater.from(parent.context)
            return ViewHolderText(inflater.inflate(R.layout.junk_text, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolderText, position: Int) {
//            TODO("Not yet implemented")
        }

    }
}
