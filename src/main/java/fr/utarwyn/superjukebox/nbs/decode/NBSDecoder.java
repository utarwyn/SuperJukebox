package fr.utarwyn.superjukebox.nbs.decode;

import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.music.model.Note;
import fr.utarwyn.superjukebox.nbs.NBSDecodeException;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Decodes a NBS file by reading a data input stream.
 * It creates a music object with layers of notes and info about it (name, length, tempo).
 *
 * @author Utarwyn <maxime.malgorn@laposte.net>
 * @since 0.3.0
 */
public interface NBSDecoder {

    /**
     * Decodes header bytes of the song.
     * It includes the name, author, length, etc.
     *
     * @param music       music object which will be filled with header data
     * @param inputStream file input stream which will be read to get header data
     * @throws NBSDecodeException thrown if the header is misencoded
     */
    void decodeHeader(Music music, DataInputStream inputStream) throws NBSDecodeException;

    /**
     * Decodes noteblocks of the song.
     * It includes notes, layers, instruments, etc.
     *
     * @param music       music object which will be filled with noteblocks data
     * @param inputStream file input stream which will be read to get noteblocks data
     * @return amount of loaded ticks of noteblocks
     * @throws NBSDecodeException thrown if noteblocks data are misencoded
     */
    short decodeNoteblocks(Music music, DataInputStream inputStream) throws NBSDecodeException;

    /**
     * Decode a note from an input stream.
     *
     * @param inputStream     file input stream which contains the next note to decode
     * @param additionnalData tell the method to retrieve additionnal data about the note
     * @return the decoded note
     * @throws IOException thrown if the note cannot be decoded from the stream
     */
    Note decodeNote(DataInputStream inputStream, boolean additionnalData) throws IOException;

    /**
     * Decodes layers of the song file.
     *
     * @param music       music object which will be filled with layers data
     * @param inputStream file input stream which contains the next note to decode
     * @throws NBSDecodeException thrown if layers data are misencoded
     */
    void decodeLayers(Music music, DataInputStream inputStream) throws NBSDecodeException;

}
