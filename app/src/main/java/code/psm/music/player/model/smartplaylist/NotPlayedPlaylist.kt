package code.psm.music.player.model.smartplaylist

import code.psm.music.player.App
import code.psm.music.player.R
import code.psm.music.player.model.Song
import kotlinx.parcelize.Parcelize

@Parcelize
class NotPlayedPlaylist : AbsSmartPlaylist(
    name = App.getContext().getString(R.string.not_recently_played),
    iconRes = R.drawable.ic_watch_later
) {
    override fun songs(): List<Song> {
        return topPlayedRepository.notRecentlyPlayedTracks()
    }
}