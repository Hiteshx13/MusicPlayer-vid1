package code.psm.music.player.interfaces

import code.psm.music.player.model.Album
import code.psm.music.player.model.Artist
import code.psm.music.player.model.Genre

interface IHomeClickListener {
    fun onAlbumClick(album: Album)

    fun onArtistClick(artist: Artist)

    fun onGenreClick(genre: Genre)
}