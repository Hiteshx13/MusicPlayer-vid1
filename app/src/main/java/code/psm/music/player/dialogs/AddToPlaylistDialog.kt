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
package code.psm.music.player.dialogs

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import code.psm.music.player.EXTRA_PLAYLISTS
import code.psm.music.player.EXTRA_SONG
import code.psm.music.player.R
import code.psm.music.player.db.PlaylistEntity
import code.psm.music.player.db.toSongsEntity
import code.psm.music.player.extensions.colorButtons
import code.psm.music.player.extensions.extraNotNull
import code.psm.music.player.extensions.materialDialog
import code.psm.music.player.fragments.LibraryViewModel
import code.psm.music.player.fragments.ReloadType.Playlists
import code.psm.music.player.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AddToPlaylistDialog : DialogFragment() {
    private val libraryViewModel by sharedViewModel<LibraryViewModel>()

    companion object {
        fun create(playlistEntities: List<PlaylistEntity>, song: Song): AddToPlaylistDialog {
            val list: MutableList<Song> = mutableListOf()
            list.add(song)
            return create(playlistEntities, list)
        }

        fun create(playlistEntities: List<PlaylistEntity>, songs: List<Song>): AddToPlaylistDialog {
            return AddToPlaylistDialog().apply {
                arguments = bundleOf(
                    EXTRA_SONG to songs,
                    EXTRA_PLAYLISTS to playlistEntities
                )
            }
        }
    }

    private fun playlistAdapter(playlists: List<String>): ArrayAdapter<String> {
        val adapter = ArrayAdapter<String>(requireContext(), R.layout.item_simple_text, R.id.title)
        adapter.addAll(playlists)
        return adapter
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val playlistEntities = extraNotNull<List<PlaylistEntity>>(EXTRA_PLAYLISTS).value
        val songs = extraNotNull<List<Song>>(EXTRA_SONG).value
        val playlistNames = mutableListOf<String>()
        playlistNames.add(requireContext().resources.getString(R.string.action_new_playlist))
        for (entity: PlaylistEntity in playlistEntities) {
            playlistNames.add(entity.playlistName)
        }
        return materialDialog(R.string.add_playlist_title)
            .setAdapter(
                playlistAdapter(playlistNames)
            ) { dialog, which ->
                if (which == 0) {
                    showCreateDialog(songs)
                } else {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val songEntities = songs.toSongsEntity(playlistEntities[which - 1])
                        libraryViewModel.insertSongs(songEntities)
                        libraryViewModel.forceReload(Playlists)
                    }
                }
                dialog.dismiss()
            }
            .create().colorButtons()
    }

    private fun showCreateDialog(songs: List<Song>) {
        CreatePlaylistDialog.create(songs).show(requireActivity().supportFragmentManager, "Dialog")
    }
}
