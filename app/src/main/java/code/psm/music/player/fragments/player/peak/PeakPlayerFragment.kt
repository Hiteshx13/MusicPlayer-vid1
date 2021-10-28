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
package code.psm.music.player.fragments.player.peak

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import code.psm.music.appthemehelper.util.ATHUtil
import code.psm.music.appthemehelper.util.ToolbarContentTintHelper
import code.psm.music.player.R
import code.psm.music.player.databinding.FragmentPeakPlayerBinding
import code.psm.music.player.extensions.hide
import code.psm.music.player.extensions.show
import code.psm.music.player.fragments.base.AbsPlayerFragment
import code.psm.music.player.fragments.base.goToAlbum
import code.psm.music.player.fragments.base.goToArtist
import code.psm.music.player.fragments.player.PlayerAlbumCoverFragment
import code.psm.music.player.helper.MusicPlayerRemote
import code.psm.music.player.util.PreferenceUtil
import code.psm.music.player.util.color.MediaNotificationProcessor

/**
 * Created by hemanths on 2019-10-03.
 */

class PeakPlayerFragment : AbsPlayerFragment(R.layout.fragment_peak_player) {

    private lateinit var controlsFragment: PeakPlayerControlFragment
    private var lastColor: Int = 0
    private var _binding: FragmentPeakPlayerBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPeakPlayerBinding.bind(view)
        setUpPlayerToolbar()
        setUpSubFragments()
        binding.title.isSelected = true
        binding.title.setOnClickListener {
            goToAlbum(requireActivity())
        }
        binding.text.setOnClickListener {
            goToArtist(requireActivity())
        }
    }

    private fun setUpSubFragments() {
        controlsFragment =
            childFragmentManager.findFragmentById(R.id.playbackControlsFragment) as PeakPlayerControlFragment

        val coverFragment =
            childFragmentManager.findFragmentById(R.id.playerAlbumCoverFragment) as PlayerAlbumCoverFragment
        coverFragment.setCallbacks(this)
    }

    private fun setUpPlayerToolbar() {
        binding.playerToolbar.apply {
            inflateMenu(R.menu.menu_player)
            setNavigationOnClickListener { requireActivity().onBackPressed() }
            setOnMenuItemClickListener(this@PeakPlayerFragment)
            ToolbarContentTintHelper.colorizeToolbar(
                this,
                ATHUtil.resolveColor(context, R.attr.colorControlNormal),
                requireActivity()
            )
        }
    }

    override fun playerToolbar(): Toolbar {
        return binding.playerToolbar
    }

    override fun onShow() {
    }

    override fun onHide() {
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    override fun toolbarIconColor(): Int {
        return ATHUtil.resolveColor(requireContext(), R.attr.colorControlNormal)
    }

    override val paletteColor: Int
        get() = lastColor

    override fun onColorChanged(color: MediaNotificationProcessor) {
        lastColor = color.primaryTextColor
        libraryViewModel.updateColor(color.primaryTextColor)
        controlsFragment.setColor(color)
    }

    override fun onFavoriteToggled() {
    }

    private fun updateSong() {
        val song = MusicPlayerRemote.currentSong
        binding.title.text = song.title
        binding.text.text = song.artistName

        if (PreferenceUtil.isSongInfo) {
            binding.songInfo.text = getSongInfo(song)
            binding.songInfo.show()
        } else {
            binding.songInfo.hide()
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        updateSong()
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        updateSong()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
