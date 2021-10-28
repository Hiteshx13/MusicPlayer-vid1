package code.psm.music.player.interfaces

import android.view.View
import code.psm.music.player.db.PlaylistWithSongs

interface IPlaylistClickListener {
    fun onPlaylistClick(playlistWithSongs: PlaylistWithSongs, view: View)
}