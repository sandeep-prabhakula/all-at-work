package com.sandeepprabhakula.smartindiahackathon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sandeepprabhakula.smartindiahackathon.models.Worker

class UserUsedServicesAdapter(private val list:ArrayList<Worker>,private val listener:IComplete): RecyclerView.Adapter<UserUsedServicesAdapter.ServicesViewHolder>() {
    class ServicesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val workerName: TextView = itemView.findViewById(R.id.workerName)
        val workerAge: TextView = itemView.findViewById(R.id.workerAge)
        val workerSkills: TextView = itemView.findViewById(R.id.workerSkills)
        val workerLocation: TextView = itemView.findViewById(R.id.workerLocation)
        val requestAService: Button = itemView.findViewById(R.id.requestService)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesViewHolder {
        val view = ServicesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.worker_layout,parent,false))
        view.requestAService.setOnClickListener {
            listener.onClickOnComplete(list[view.absoluteAdapterPosition].workerAadharID,view.absoluteAdapterPosition)
            view.requestAService.text = "Service Done"
            list.remove(list[view.absoluteAdapterPosition])
        }
        return view
    }

    override fun onBindViewHolder(holder: ServicesViewHolder, position: Int) {
        val model  = list[position]
        holder.workerName.text = model.workerName
        holder.workerAge.text = model.workerAge
        holder.workerLocation.text = model.workerLocation
        holder.workerSkills.text = model.workerSkills
        holder.requestAService.text = "Mark as complete"
        holder.requestAService.setBackgroundColor(0xF0AD4E)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

interface IComplete{
    fun onClickOnComplete(mobile:String,position:Int)
}