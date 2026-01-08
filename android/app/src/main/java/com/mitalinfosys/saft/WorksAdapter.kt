package com.mitalinfosys.saft

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WorksAdapter(
    private var worksList: MutableList<Work>,
    private val onItemClick: (Work) -> Unit
) : RecyclerView.Adapter<WorksAdapter.WorkViewHolder>() {
    
    class WorkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPartyName: TextView = itemView.findViewById(R.id.tvPartyName)
        val tvWorkName: TextView = itemView.findViewById(R.id.tvWorkName)
        val tvWorkId: TextView = itemView.findViewById(R.id.tvWorkId)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_work, parent, false)
        return WorkViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: WorkViewHolder, position: Int) {
        val work = worksList[position]
        
        holder.tvPartyName.text = work.partyName
        holder.tvWorkName.text = work.workName
        holder.tvWorkId.text = work.workId
        
        holder.itemView.setOnClickListener {
            onItemClick(work)
        }
    }
    
    override fun getItemCount(): Int = worksList.size
    
    fun updateList(newList: List<Work>) {
        worksList.clear()
        worksList.addAll(newList)
        notifyDataSetChanged()
    }
}