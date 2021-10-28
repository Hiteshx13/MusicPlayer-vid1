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
package code.psm.music.player.fragments.player.full

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import code.psm.music.appthemehelper.util.ToolbarContentTintHelper
import code.psm.music.player.R
import code.psm.music.player.databinding.FragmentFullBinding
import code.psm.music.player.extensions.hide
import code.psm.music.player.extensions.show
import code.psm.music.player.extensions.whichFragment
import code.psm.music.player.fragments.base.AbsPlayerFragment
import code.psm.music.player.fragments.base.goToArtist
import code.psm.music.player.fragments.player.PlayerAlbumCoverFragment
import code.psm.music.player.glide.GlideApp
import code.psm.music.player.glide.RetroGlideExtension
import code.psm.music.player.glide.RetroMusicColoredTarget
import code.psm.music.player.helper.MusicPlayerRemote
import code.psm.music.player.model.Song
import code.psm.music.player.util.color.MediaNotificationProcessor

class FullPlayerFragment : AbsPlayerFragment(R.layout.fragment_full) {
    private var _binding: FragmentFullBinding? = null
    private val binding get() = _binding!!

    override fun playerToolbar(): Toolbar {
        return binding.playerToolbar
    }

    private var lastColor: Int = 0
    override val paletteColor: Int
        get() = lastColor
    private lateinit var controlsFragment: FullPlaybackControlsFragment

    private fun setUpPlayerToolbar() {
        binding.playerToolbar.apply {
            setNavigationOnClickListener { requireActivity().onBackPressed() }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFullBinding.bind(view)

        setUpSubFragments()
        setUpPlayerToolbar()
        setupArtist()
        binding.nextSong.isSelected = true
    }

    private fun setupArtist() {
        binding.artistImage.setOnClickListener {
            goToArtist(mainActivity)
        }
    }

    private fun setUpSubFragments() {
        controlsFragment = whichFragment(R.id.playbackControlsFragment)
        val coverFragment: PlayerAlbumCoverFragment = whichFragment(R.id.playerAlbumCoverFragment)
        coverFragment.setCallbacks(this)
        coverFragment.removeSlideEffect()
    }

    override fun onShow() {
    }

    override fun onHide() {
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    override fun toolbarIconColor(): Int {
        return Color.WHITE
    }

    override fun onColorChanged(color: MediaNotificationProcessor) {
        lastColor = color.backgroundColor
        binding.mask.backgroundTintList = ColorStateList.valueOf(color.backgroundColor)
        controlsFragment.setColor(color)
        libraryViewModel.updateColor(color.backgroundColor)
        ToolbarContentTintHelper.colorizeToolbar(binding.playerToolbar, Color.WHITE, activity)
    }

    override fun onFavoriteToggled() {
        toggleFavorite(MusicPlayerRemote.currentSong)
        controlsFragment.onFavoriteToggled()
    }

    override fun toggleFavorite(song: Song) {
        super.toggleFavorite(song)
        if (song.id == MusicPlayerRemote.currentSong.id) {
            updateIsFavorite()
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        updateArtistImage()
        updateLabel()
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        updateArtistImage()
        updateLabel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateArtistImage() {
        libraryViewModel.artist(MusicPlayerRemote.currentSong.artistId)
            .observe(viewLifecycleOwner, { artist ->
                GlideApp.with(requireActivity()).asBitmapPalette().artistImageOptions(artist)
                    .load(RetroGlideExtension.getArtistModel(artist))
                    .into(object : RetroMusicColoredTarget(binding.artistImage) {
                        override fun onColorReady(colors: MediaNotificationProcessor) {
                        }
                    })
            })
    }

    override fun onQueueChanged() {
        super.onQueueChanged()
        if (MusicPlayerRemote.playingQueue.isNotEmpty()) updateLabel()
    }

    private fun updateLabel() {
        (MusicPlayerRemote.playingQueue.size - 1).apply {
            if (this == (MusicPlayerRemote.position)) {
                binding.nextSongLabel.setText(R.string.last_song)
                binding.nextSong.hide()
            } else {
                val title = MusicPlayerRemote.playingQueue[MusicPlayerRemote.position + 1].title
                binding.nextSongLabel.setText(R.string.next_song)
                binding.nextSong.apply {
                    text = title
                    show()
                }
            }
        }
    }
}
