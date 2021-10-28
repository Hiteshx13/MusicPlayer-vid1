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
package code.psm.music.player.adapter.playlist

import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import code.psm.music.player.R
import code.psm.music.player.adapter.base.AbsMultiSelectAdapter
import code.psm.music.player.adapter.base.MediaEntryViewHolder
import code.psm.music.player.db.PlaylistEntity
import code.psm.music.player.db.PlaylistWithSongs
import code.psm.music.player.db.toSongs
import code.psm.music.player.extensions.hide
import code.psm.music.player.extensions.show
import code.psm.music.player.glide.GlideApp
import code.psm.music.player.glide.playlistPreview.PlaylistPreview
import code.psm.music.player.helper.menu.PlaylistMenuHelper
import code.psm.music.player.helper.menu.SongsMenuHelper
import code.psm.music.player.interfaces.ICabHolder
import code.psm.music.player.interfaces.IPlaylistClickListener
import code.psm.music.player.model.Song
import code.psm.music.player.util.MusicUtil

class PlaylistAdapter(
    private val activity: FragmentActivity,
    private var dataSet: List<PlaylistWithSongs>,
    private var itemLayoutRes: Int,
    ICabHolder: ICabHolder?,
    private val listener: IPlaylistClickListener
) : AbsMultiSelectAdapter<PlaylistAdapter.ViewHolder, PlaylistWithSongs>(
    activity,
    ICabHolder,
    R.menu.menu_playlists_selection
) {

    init {
        setHasStableIds(true)
    }

    fun swapDataSet(dataSet: List<PlaylistWithSongs>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return dataSet[position].playlistEntity.playListId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false)
        return createViewHolder(view)
    }

    fun createViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }

    private fun getPlaylistTitle(playlist: PlaylistEntity): String {
        return if (TextUtils.isEmpty(playlist.playlistName)) "-" else playlist.playlistName
    }

    private fun getPlaylistText(playlist: PlaylistWithSongs): String {
        return MusicUtil.getPlaylistInfoString(activity, playlist.songs.toSongs())
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playlist = dataSet[position]
        holder.itemView.isActivated = isChecked(playlist)
        holder.title?.text = getPlaylistTitle(playlist.playlistEntity)
        holder.text?.text = getPlaylistText(playlist)
        val isChecked = isChecked(playlist)
        if (isChecked) {
            holder.menu?.hide()
        } else {
            holder.menu?.show()
        }
        GlideApp.with(activity)
            .load(PlaylistPreview(playlist))
            .playlistOptions()
            .into(holder.image!!)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getIdentifier(position: Int): PlaylistWithSongs {
        return dataSet[position]
    }

    override fun getName(playlist: PlaylistWithSongs): String {
        return playlist.playlistEntity.playlistName
    }

    override fun onMultipleItemAction(menuItem: MenuItem, selection: List<PlaylistWithSongs>) {
        when (menuItem.itemId) {
            else -> SongsMenuHelper.handleMenuClick(
                activity,
                getSongList(selection),
                menuItem.itemId
            )
        }
    }

    private fun getSongList(playlists: List<PlaylistWithSongs>): List<Song> {
        val songs = mutableListOf<Song>()
        playlists.forEach {
            songs.addAll(it.songs.toSongs())
        }
        return songs
    }

    inner class ViewHolder(itemView: View) : MediaEntryViewHolder(itemView) {
        init {
            menu?.setOnClickListener { view ->
                val popupMenu = PopupMenu(activity, view)
                popupMenu.inflate(R.menu.menu_item_playlist)
                popupMenu.setOnMenuItemClickListener { item ->
                    PlaylistMenuHelper.handleMenuClick(activity, dataSet[layoutPosition], item)
                }
                popupMenu.show()
            }

            imageTextContainer?.apply {
                cardElevation = 0f
                setCardBackgroundColor(Color.TRANSPARENT)
            }
        }

        override fun onClick(v: View?) {
            if (isInQuickSelectMode) {
                toggleChecked(layoutPosition)
            } else {
                ViewCompat.setTransitionName(itemView, "playlist")
                listener.onPlaylistClick(dataSet[layoutPosition], itemView)
            }
        }

        override fun onLongClick(v: View?): Boolean {
            toggleChecked(layoutPosition)
            return true
        }
    }

    companion object {
        val TAG: String = PlaylistAdapter::class.java.simpleName
    }
}
