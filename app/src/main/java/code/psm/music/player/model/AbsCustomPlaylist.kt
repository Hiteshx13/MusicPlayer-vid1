package code.psm.music.player.model

import code.psm.music.player.repository.LastAddedRepository
import code.psm.music.player.repository.SongRepository
import code.psm.music.player.repository.TopPlayedRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class AbsCustomPlaylist(
    id: Long,
    name: String
) : Playlist(id, name), KoinComponent {

    abstract fun songs(): List<Song>

    protected val songRepository by inject<SongRepository>()

    protected val topPlayedRepository by inject<TopPlayedRepository>()

    protected val lastAddedRepository by inject<LastAddedRepository>()
}