package com.sandeepprabhakula.smartindiahackathon

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.sandeepprabhakula.smartindiahackathon.models.Worker

class WorkersAdapter(options: FirestoreRecyclerOptions<Worker>,private val listener:ServiceRequest):FirestoreRecyclerAdapter<Worker,WorkersAdapter.WorkerViewHolder>(options) {
    class WorkerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val workerName:TextView = itemView.findViewById(R.id.workerName)
        val workerAge:TextView = itemView.findViewById(R.id.workerAge)
        val workerSkills:TextView = itemView.findViewById(R.id.workerSkills)
        val workerLocation:TextView = itemView.findViewById(R.id.workerLocation)
        val requestAService: Button = itemView.findViewById(R.id.requestService)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerViewHolder {
        val view = WorkerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.worker_layout,parent,false))
        view.requestAService.setOnClickListener {
            listener.onServiceRequest(snapshots.getSnapshot(view.absoluteAdapterPosition).id)
        }
        return view
    }

    override fun onBindViewHolder(holder: WorkerViewHolder, position: Int, model: Worker) {
        holder.workerName.text = model.workerName
        holder.workerAge.text = model.workerAge
        holder.workerSkills.text = model.workerSkills
        holder.workerLocation.text = model.workerLocation
    }
}
interface ServiceRequest{
    fun onServiceRequest(workerId:String)
}

