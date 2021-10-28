package code.psm.music.player.model.smartplaylist

import code.psm.music.player.App
import code.psm.music.player.R
import code.psm.music.player.model.Song
import kotlinx.parcelize.Parcelize
import org.koin.core.KoinComponent

@Parcelize
class HistoryPlaylist : AbsSmartPlaylist(
    name = App.getContext().getString(R.string.history),
    iconRes = R.drawable.ic_history
), KoinComponent {

    override fun songs(): List<Song> {
        return topPlayedRepository.recentlyPlayedTracks()
    }
}