package com.grig.danish.tools

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

class OneShotAudioPlayer(private val context: Context) {

    fun play(url: String) {
        val exoPlayer = ExoPlayer.Builder(context).build()
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    exoPlayer.release()
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                exoPlayer.release()
            }
        })

        val mediaItem = MediaItem.fromUri(url)
        exoPlayer.setMediaItem(mediaItem)

        exoPlayer.prepare()
        exoPlayer.play()
    }
}