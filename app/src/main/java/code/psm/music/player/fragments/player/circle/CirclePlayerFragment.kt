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
package code.psm.music.player.fragments.player.circle

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.media.AudioManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import androidx.appcompat.widget.Toolbar
import code.psm.music.appthemehelper.ThemeStore
import code.psm.music.appthemehelper.util.ATHUtil
import code.psm.music.appthemehelper.util.ColorUtil
import code.psm.music.appthemehelper.util.TintHelper
import code.psm.music.appthemehelper.util.ToolbarContentTintHelper
import code.psm.music.player.R
import code.psm.music.player.databinding.FragmentCirclePlayerBinding
import code.psm.music.player.extensions.accentColor
import code.psm.music.player.extensions.applyColor
import code.psm.music.player.extensions.hide
import code.psm.music.player.extensions.show
import code.psm.music.player.fragments.base.AbsPlayerControlsFragment
import code.psm.music.player.fragments.base.AbsPlayerFragment
import code.psm.music.player.fragments.base.goToAlbum
import code.psm.music.player.fragments.base.goToArtist
import code.psm.music.player.helper.MusicPlayerRemote
import code.psm.music.player.helper.MusicProgressViewUpdateHelper
import code.psm.music.player.helper.MusicProgressViewUpdateHelper.Callback
import code.psm.music.player.helper.PlayPauseButtonOnClickHandler
import code.psm.music.player.misc.SimpleOnSeekbarChangeListener
import code.psm.music.player.util.MusicUtil
import code.psm.music.player.util.PreferenceUtil
import code.psm.music.player.util.ViewUtil
import code.psm.music.player.util.color.MediaNotificationProcessor
import code.psm.music.player.views.SeekArc
import code.psm.music.player.views.SeekArc.OnSeekArcChangeListener
import code.psm.music.player.volume.AudioVolumeObserver
import code.psm.music.player.volume.OnAudioVolumeChangedListener

/**
 * Created by hemanths on 2020-01-06.
 */

class CirclePlayerFragment : AbsPlayerFragment(R.layout.fragment_circle_player), Callback,
    OnAudioVolumeChangedListener,
    OnSeekArcChangeListener {

    private lateinit var progressViewUpdateHelper: MusicProgressViewUpdateHelper
    private var audioVolumeObserver: AudioVolumeObserver? = null

    private val audioManager: AudioManager?
        get() = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private var _binding: FragmentCirclePlayerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressViewUpdateHelper = MusicProgressViewUpdateHelper(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCirclePlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        binding.title.isSelected = true
        binding.title.setOnClickListener {
            goToAlbum(requireActivity())
        }
        binding.text.setOnClickListener {
            goToArtist(requireActivity())
        }
    }

    private fun setUpPlayerToolbar() {
        binding.playerToolbar.apply {
            inflateMenu(R.menu.menu_player)
            setNavigationOnClickListener { requireActivity().onBackPressed() }
            setOnMenuItemClickListener(this@CirclePlayerFragment)
            ToolbarContentTintHelper.colorizeToolbar(
                this,
                ATHUtil.resolveColor(requireContext(), R.attr.colorControlNormal),
                requireActivity()
            )
        }
    }

    private fun setupViews() {
        setUpProgressSlider()
        ViewUtil.setProgressDrawable(
            binding.progressSlider,
            ThemeStore.accentColor(requireContext()),
            false
        )
        binding.volumeSeekBar.progressColor = accentColor()
        binding.volumeSeekBar.arcColor = ColorUtil.withAlpha(accentColor(), 0.25f)
        setUpPlayPauseFab()
        setUpPrevNext()
        setUpPlayerToolbar()
    }

    private fun setUpPrevNext() {
        updatePrevNextColor()
        binding.nextButton.setOnClickListener { MusicPlayerRemote.playNextSong() }
        binding.previousButton.setOnClickListener { MusicPlayerRemote.back() }
    }

    private fun updatePrevNextColor() {
        val accentColor = ThemeStore.accentColor(requireContext())
        binding.nextButton.setColorFilter(accentColor, PorterDuff.Mode.SRC_IN)
        binding.previousButton.setColorFilter(accentColor, PorterDuff.Mode.SRC_IN)
    }

    private fun setUpPlayPauseFab() {
        TintHelper.setTintAuto(
            binding.playPauseButton,
            ThemeStore.accentColor(requireContext()),
            false
        )
        binding.playPauseButton.setOnClickListener(PlayPauseButtonOnClickHandler())
    }

    override fun onResume() {
        super.onResume()
        progressViewUpdateHelper.start()
        if (audioVolumeObserver == null) {
            audioVolumeObserver = AudioVolumeObserver(requireActivity())
        }
        audioVolumeObserver?.register(AudioManager.STREAM_MUSIC, this)

        val audioManager = audioManager
        if (audioManager != null) {
            binding.volumeSeekBar.max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            binding.volumeSeekBar.progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        }
        binding.volumeSeekBar.setOnSeekArcChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        progressViewUpdateHelper.stop()
    }

    override fun playerToolbar(): Toolbar {
        return binding.playerToolbar
    }

    override fun onShow() {
    }

    override fun onHide() {
    }

    override fun onBackPressed(): Boolean = false

    override fun toolbarIconColor(): Int =
        ATHUtil.resolveColor(requireContext(), android.R.attr.colorControlNormal)

    override val paletteColor: Int
        get() = Color.BLACK

    override fun onColorChanged(color: MediaNotificationProcessor) {
    }

    override fun onFavoriteToggled() {
    }

    override fun onPlayStateChanged() {
        updatePlayPauseDrawableState()
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        updateSong()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        updateSong()
        updatePlayPauseDrawableState()
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

    private fun updatePlayPauseDrawableState() {
        when {
            MusicPlayerRemote.isPlaying -> binding.playPauseButton.setImageResource(R.drawable.ic_pause)
            else -> binding.playPauseButton.setImageResource(R.drawable.ic_play_arrow)
        }
    }

    override fun onAudioVolumeChanged(currentVolume: Int, maxVolume: Int) {
        _binding?.volumeSeekBar?.max = maxVolume
        _binding?.volumeSeekBar?.progress = currentVolume
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (audioVolumeObserver != null) {
            audioVolumeObserver!!.unregister()
        }
        _binding = null
    }

    override fun onProgressChanged(seekArc: SeekArc?, progress: Int, fromUser: Boolean) {
        val audioManager = audioManager
        audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
    }

    override fun onStartTrackingTouch(seekArc: SeekArc?) {
    }

    override fun onStopTrackingTouch(seekArc: SeekArc?) {
    }

    fun setUpProgressSlider() {
        binding.progressSlider.applyColor(accentColor())
        binding.progressSlider.setOnSeekBarChangeListener(object : SimpleOnSeekbarChangeListener() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MusicPlayerRemote.seekTo(progress)
                    onUpdateProgressViews(
                        MusicPlayerRemote.songProgressMillis,
                        MusicPlayerRemote.songDurationMillis
                    )
                }
            }
        })
    }

    override fun onUpdateProgressViews(progress: Int, total: Int) {
        binding.progressSlider.max = total

        val animator = ObjectAnimator.ofInt(binding.progressSlider, "progress", progress)
        animator.duration = AbsPlayerControlsFragment.SLIDER_ANIMATION_TIME
        animator.interpolator = LinearInterpolator()
        animator.start()

        binding.songTotalTime.text = MusicUtil.getReadableDurationString(total.toLong())
        binding.songCurrentProgress.text = MusicUtil.getReadableDurationString(progress.toLong())
    }
}
