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

import android.R.attr.path
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
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
import java.io.File
import java.util.*


class VideoListAdapter(
     private val activity: AppCompatActivity,
     var  VideoPath: ArrayList<String?>? = null,
     var listener: IFolderClickListener
) : RecyclerView.Adapter<VideoListAdapter.ViewHolder>() {

    init {
        this.setHasStableIds(true)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.row_video_grid, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val url = VideoPath?.get(position) ?:""
        holder.tvFolder?.text = url
//        holder.ivImage?.setImageBitmap(getPreview(Uri.parse(url)))
//        val thumb = ThumbnailUtils.createVideoThumbnail(
//            path,
//            MediaStore.Images.Thumbnails.MINI_KIND
//        )
//
//        val thumb: Bitmap = ThumbnailUtils.createVideoThumbnail(
//            url,
//            MediaStore.Images.Thumbnails.MINI_KIND
//        )
    }

    override fun getItemCount(): Int {
        return VideoPath?.size?:0
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

//    private fun getPreview(uri: Uri): Bitmap? {
//        val image = File(uri)
//        val bounds: BitmapFactory.Options = BitmapFactory.Options()
//        bounds.inJustDecodeBounds = true
//        BitmapFactory.decodeFile(image.getPath(), bounds)
//        if (bounds.outWidth === -1 || bounds.outHeight === -1) return null
//        val originalSize: Int =
//            if (bounds.outHeight > bounds.outWidth) bounds.outHeight else bounds.outWidth
//        val opts: BitmapFactory.Options = BitmapFactory.Options()
//        //opts.inSampleSize = originalSize / THUMBNAIL_SIZE;
//        return BitmapFactory.decodeFile(image.getPath(), opts)
//    }
}
