package fr.utarwyn.superjukebox.nbs.decode;

import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.music.model.Note;
import fr.utarwyn.superjukebox.nbs.NBSDecodeException;

import java.io.DataInputStream;
import java.io.IOException;

import static fr.utarwyn.superjukebox.nbs.InputStreamUtil.*;

/**
 * Decode NBS files with the new format (used by Open NoteBlockStudio).
 * All explanations about the format here:
 * https://hielkeminecraft.github.io/OpenNoteBlockStudio/nbs
 *
 * @author Utarwyn <maxime.malgorn@laposte.net>
 * @since 0.3.0
 */
public class OpenNBSDecoder implements NBSDecoder {

    /**
     * Parent NBS decoder for basic processing
     */
    private NBSDecoder parentNBSDecoder;

    /**
     * Construct this decoder.
     *
     * @param parentNBSDecoder parent NBS decoder
     */
    public OpenNBSDecoder(NBSDecoder parentNBSDecoder) {
        this.parentNBSDecoder = parentNBSDecoder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decodeHeader(Music music, DataInputStream dis) throws NBSDecodeException {
        try {
            // First headers added in this format
            dis.readByte(); // version of the file
            dis.readByte(); // vanilla instrument count
            music.setLength(readShort(dis)); // music length

            // Decode other headers with the parent decoder
            this.parentNBSDecoder.decodeHeader(music, dis);

            // Decode extra headers in this version
            dis.readByte(); // loop on/off
            dis.readByte(); // max loop count
            readShort(dis); // loop start tick
        } catch (IOException e) {
            // Argh! An error in the format of the file maybe?
            throw new NBSDecodeException(music.getFilename(), "Cannot read NBS headers", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decodeNoteblocks(Music music, DataInputStream dis) throws NBSDecodeException {
        this.parentNBSDecoder.decodeNoteblocks(music, dis);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Note decodeNote(DataInputStream dis) throws IOException {
        byte instrument = dis.readByte();
        byte note = dis.readByte();

        dis.readByte(); // note velocity
        dis.readByte(); // note panning
        dis.readShort(); // note pitch (signed short)

        return new Note(instrument, note);
    }

}
