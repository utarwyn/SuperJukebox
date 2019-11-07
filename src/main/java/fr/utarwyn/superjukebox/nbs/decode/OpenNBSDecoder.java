package fr.utarwyn.superjukebox.nbs.decode;

import fr.utarwyn.superjukebox.music.Music;

import java.io.DataInputStream;

/**
 * Decode NBS files with the new format (used by Open NoteBlockStudio).
 * All explanations about the format here:
 * https://hielkeminecraft.github.io/OpenNoteBlockStudio/nbs
 *
 * @author Utarwyn
 * @since 1.2.0
 */
public class OpenNBSDecoder implements NBSDecoder {

    @Override
    public void decodeHeader(Music music, DataInputStream dis) {
        // todo implement this method
        throw new UnsupportedOperationException();
    }

    @Override
    public void decodeNoteblocks(Music music, DataInputStream dis) {
        // todo implement this method
        throw new UnsupportedOperationException();
    }

}
