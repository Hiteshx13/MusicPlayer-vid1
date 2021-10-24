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
package code.name.monkey.retromusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.interfaces.IFolderClickListener
import code.name.monkey.retromusic.model.VideoModel
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
        holder.tvFolder?.text = videoData.folderName
//        loadGenreImage(genre, holder)
    }

//    private fun loadGenreImage(genre: Genre, holder: VideoListAdapter.ViewHolder) {
//        val genreSong = MusicUtil.songByGenre(genre.id)
//        GlideApp.with(activity)
//            .asBitmapPalette()
//            .load(RetroGlideExtension.getSongModel(genreSong))
//            .songCoverOptions(genreSong)
//            .into(object : RetroMusicColoredTarget(holder.image!!) {
//                override fun onColorReady(colors: MediaNotificationProcessor) {
//                    setColors(holder, colors)
//                }
//            })
//        // Just for a bit of shadow around image
//        holder.image?.outlineProvider = ViewOutlineProvider.BOUNDS
//    }
//

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
