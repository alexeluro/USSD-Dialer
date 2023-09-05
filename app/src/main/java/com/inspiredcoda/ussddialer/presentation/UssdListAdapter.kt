package com.inspiredcoda.ussddialer.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.inspiredcoda.ussddialer.databinding.UssdHomeItemBinding
import com.inspiredcoda.ussddialer.model.UssdCode

class UssdListAdapter(
    val action: (UssdCode) -> Unit
) : Adapter<UssdListAdapter.UssdViewHolder>() {

    private var ussdCodesList: MutableList<UssdCode> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UssdViewHolder {
        val binding = UssdHomeItemBinding.inflate(LayoutInflater.from(parent.context))
        return UssdViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UssdViewHolder, position: Int) {
        holder.bind(ussdCodesList[position])
    }

    override fun getItemCount(): Int {
        return ussdCodesList.size
    }

    fun updateUssdCodes(newCodes: List<UssdCode>) {
        ussdCodesList.clear()
        ussdCodesList.addAll(newCodes)
        notifyDataSetChanged()
    }


    inner class UssdViewHolder(private val binding: UssdHomeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UssdCode) {
            binding.ussdIcon.setImageResource(item.icon)
            binding.ussdTitle.text = item.title
            binding.root.setOnClickListener {
                action(item)
            }
        }

    }

}