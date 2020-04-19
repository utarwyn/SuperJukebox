package fr.utarwyn.superjukebox.nbs.decode;

import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.music.model.Note;
import fr.utarwyn.superjukebox.nbs.NBSDecodeException;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * This interface allows to decode a NBS file.
 * It creates a music object with layers of notes
 * and info about it (name, length, tempo).
 *
 * @author Utarwyn <maxime.malgorn@laposte.net>
 * @since 0.3.0
 */
public interface NBSDecoder {

    /**
     * Decode the header bytes of the music.
     * It includes the name, author, length, etc.
     *
     * @param music music object which will be filled with header data
     * @param inputStream file input stream which will be read to get header data
     * @throws NBSDecodeException thrown if the header is misencoded
     */
    void decodeHeader(Music music, DataInputStream inputStream) throws NBSDecodeException;

    /**
     * Decode the noteblocks of the music.
     * It includes notes, layers, instruments, etc.
     *
     * @param music music object which will be filled with noteblocks data
     * @param inputStream file input stream which will be read to get noteblocks data
     * @throws NBSDecodeException thrown if noteblocks data are misencoded
     */
    void decodeNoteblocks(Music music, DataInputStream inputStream) throws NBSDecodeException;

    /**
     * Decode a note from an input stream.
     *
     * @param inputStream file input stream which contains the next note to decode
     * @return the decoded note
     * @throws IOException thrown if the note cannot be decoded from the stream
     */
    Note decodeNote(DataInputStream inputStream) throws IOException;

}
