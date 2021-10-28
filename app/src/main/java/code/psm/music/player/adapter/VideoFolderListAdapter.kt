/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.psm.music.player.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import code.psm.music.appthemehelper.util.ATHUtil
import code.psm.music.player.R
import code.psm.music.player.interfaces.IFolderClickListener
import code.psm.music.player.model.VideoModel
import code.psm.music.player.util.FileUtil
import code.psm.music.player.util.RetroUtil
import java.util.*

/**
 * @author Hemanth S (h4h13).
 */

class VideoFolderListAdapter(
    private val activity: AppCompatActivity,
    var dataSet: ArrayList<VideoModel>,
    var listener: IFolderClickListener
) : RecyclerView.Adapter<VideoFolderListAdapter.ViewHolder>() {

    init {
        this.setHasStableIds(true)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.row_video_folder_grid, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val videoData = dataSet[position]
        holder.tvFolder?.text = FileUtil.getVideoName(videoData.folderName)

        if(videoData.videoPath.size>0) {


            val iconColor = ATHUtil.resolveColor(activity, R.attr.colorControlNormal)
            val error = RetroUtil.getTintedVectorDrawable(
                activity, R.drawable.ic_file_music, iconColor
            )
//            GlideApp.with(activity)
//                .load(url)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .error(error)
//                .placeholder(error)
//                .transition(RetroGlideExtension.getDefaultTransition())
////            .signature(MediaStoreSignature("", file.lastModified(), 0))
//                .into(holder.ivImage!!)
        }else{

        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvFolder: AppCompatTextView? = null
        var ivImage: AppCompatImageView? = null
        var rlRoot: RelativeLayout? = null

        init {
            tvFolder = itemView.findViewById(R.id.tvVideoTitle)
            ivImage = itemView.findViewById(R.id.selectedImage)
            rlRoot = itemView.findViewById(R.id.rlRoot)
            rlRoot?.setOnClickListener {  listener.onItemClick(layoutPosition)  }
        }

    }
}
