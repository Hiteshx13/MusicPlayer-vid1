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
package code.psm.music.player.activities

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.view.ViewCompat
import code.psm.music.player.R
import code.psm.music.player.activities.base.AbsMusicServiceActivity
import code.psm.music.player.databinding.ActivityLockScreenBinding
import code.psm.music.player.extensions.whichFragment
import code.psm.music.player.fragments.player.lockscreen.LockScreenControlsFragment
import code.psm.music.player.glide.GlideApp
import code.psm.music.player.glide.RetroGlideExtension
import code.psm.music.player.glide.RetroMusicColoredTarget
import code.psm.music.player.helper.MusicPlayerRemote
import code.psm.music.player.util.color.MediaNotificationProcessor
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrListener
import com.r0adkll.slidr.model.SlidrPosition

class LockScreenActivity : AbsMusicServiceActivity() {
    private lateinit var binding: ActivityLockScreenBinding
    private var fragment: LockScreenControlsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        super.onCreate(savedInstanceState)
        lockScreenInit()
        binding = ActivityLockScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideStatusBar()
        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setTaskDescriptionColorAuto()
        setLightNavigationBar(true)

        val config = SlidrConfig.Builder().listener(object : SlidrListener {
            override fun onSlideStateChanged(state: Int) {
            }

            override fun onSlideChange(percent: Float) {
            }

            override fun onSlideOpened() {
            }

            override fun onSlideClosed(): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val keyguardManager =
                        getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                    keyguardManager.requestDismissKeyguard(this@LockScreenActivity, null)
                }
                finish()
                return true
            }
        }).position(SlidrPosition.BOTTOM).build()

        Slidr.attach(this, config)

        fragment = whichFragment<LockScreenControlsFragment>(R.id.playback_controls_fragment)

        findViewById<View>(R.id.slide).apply {
            translationY = 100f
            alpha = 0f
            ViewCompat.animate(this).translationY(0f).alpha(1f).setDuration(1500).start()
        }
    }

    private fun lockScreenInit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            val keyguardManager: KeyguardManager = getSystemService(KeyguardManager::class.java)
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            this.window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
            )
        }
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        updateSongs()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        updateSongs()
    }

    private fun updateSongs() {
        val song = MusicPlayerRemote.currentSong
        GlideApp.with(this)
            .asBitmapPalette()
            .songCoverOptions(song)
            .load(RetroGlideExtension.getSongModel(song))
            .dontAnimate()
            .into(object : RetroMusicColoredTarget(binding.image) {
                override fun onColorReady(colors: MediaNotificationProcessor) {
                    fragment?.setColor(colors)
                }
            })
    }
}
