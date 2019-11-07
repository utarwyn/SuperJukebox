package fr.utarwyn.superjukebox.nbs.decode;

import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.nbs.NBSDecodeException;

import java.io.DataInputStream;

/**
 * This interface allows the creation of classes which decode a given NBS file
 * to create a Music object with layers of notes and info about it (name, length, tempo).
 *
 * @author Utarwyn
 * @since 1.2.0
 */
public interface NBSDecoder {

    /**
     * Decode the header bytes of the music.
     * It includes the name, author, length, etc.
     *
     * @param music music object which will be filled with header data
     * @param inputStream file input stream which will be read to get header data
     * @throws NBSDecodeException throwed if the header is misencoded
     */
    void decodeHeader(Music music, DataInputStream inputStream) throws NBSDecodeException;

    /**
     * Decode the noteblocks of the music.
     * It includes notes, layers, instruments, etc.
     *
     * @param music music object which will be filled with noteblocks data
     * @param inputStream file input stream which will be read to get noteblocks data
     * @throws NBSDecodeException throwed if noteblocks data are misencoded
     */
    void decodeNoteblocks(Music music, DataInputStream inputStream) throws NBSDecodeException;

}
