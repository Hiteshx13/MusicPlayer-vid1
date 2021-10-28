package code.psm.music.player.model.smartplaylist

import code.psm.music.player.App
import code.psm.music.player.R
import code.psm.music.player.model.Song
import kotlinx.parcelize.Parcelize

@Parcelize
class TopTracksPlaylist : AbsSmartPlaylist(
    name = App.getContext().getString(R.string.my_top_tracks),
    iconRes = R.drawable.ic_trending_up
) {
    override fun songs(): List<Song> {
        return topPlayedRepository.topTracks()
    }
}