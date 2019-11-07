package fr.utarwyn.superjukebox.nbs.decode;

import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.music.model.Note;
import fr.utarwyn.superjukebox.nbs.NBSDecodeException;

import java.io.DataInputStream;
import java.io.IOException;

import static fr.utarwyn.superjukebox.nbs.InputStreamUtil.*;

/**
 * Decode NBS files with the legacy format.
 * All explanations about the format here: https://www.stuffbydavid.com/mcnbs/format
 *
 * @author Utarwyn
 * @since 1.2.0
 */
public class LegacyNBSDecoder implements NBSDecoder {

    @Override
    public void decodeHeader(Music music, DataInputStream dis) throws NBSDecodeException {
        try {
            music.setHeight(readShort(dis));
            music.setName(readString(dis));
            music.setAuthor(readString(dis));
            music.setOriginalAuthor(readString(dis));
            music.setDescription(readString(dis));
            music.setTempo(readShort(dis) / 100f);
            music.setDelay(20 / music.getTempo());

            // Useless data in the header...
            dis.readByte(); // auto-saving
            dis.readByte(); // auto-saving duration
            dis.readByte(); // time signature
            readInt(dis); // minutes spent on project
            readInt(dis); // amount of left clicks
            readInt(dis); // amount of right clicks
            readInt(dis); // amount of blocks added
            readInt(dis); // amount of blocks removed
            readString(dis); // midi/schematic file name
        } catch (IOException e) {
            // Argh! An error in the format of the file maybe?
            throw new NBSDecodeException(music.getFilename(), "Cannot read NBS description!", e);
        }
    }

    @Override
    public void decodeNoteblocks(Music music, DataInputStream dis) throws NBSDecodeException {
        short tick = -1;

        try {
            while (true) {
                // Read all layers of the song
                short jumpsT = readShort(dis);
                if (jumpsT == 0) break;

                tick += jumpsT;

                short layer = -1;
                while (true) {
                    // Read all notes for this layer
                    short jumpsL = readShort(dis);
                    if (jumpsL == 0) break;
                    layer += jumpsL;

                    // Read the instrument and the note pitch!
                    byte instrument = dis.readByte();
                    byte note = dis.readByte();

                    music.getLayerOrDefault(layer).addNote(tick, new Note(instrument, note));
                }
            }
        } catch (IOException e) {
            // Argh! Cannot read layers and notes... Failure.
            throw new NBSDecodeException(music.getFilename(), "Cannot read NBS music notes!", e);
        }
    }

}
