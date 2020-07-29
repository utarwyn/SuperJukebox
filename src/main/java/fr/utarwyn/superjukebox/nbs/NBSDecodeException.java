package fr.utarwyn.superjukebox.nbs;

import fr.utarwyn.superjukebox.music.Music;

/**
 * Represents an exception during
 * the decoding process of a NBS file.
 *
 * @author Utarwyn <maxime.malgorn@laposte.net>
 * @since 0.3.0
 */
public class NBSDecodeException extends Exception {

    public NBSDecodeException(Music music, String message, Throwable cause) {
        super(String.format(
                "Cannot decode the music file %s (v%d): %s",
                music.getFilename(), music.getVersion(), message
        ), cause);
    }

}
