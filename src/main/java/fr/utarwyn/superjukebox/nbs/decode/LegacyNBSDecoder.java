package fr.utarwyn.superjukebox.nbs.decode;

import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.music.model.Layer;
import fr.utarwyn.superjukebox.music.model.Note;
import fr.utarwyn.superjukebox.nbs.NBSDecodeException;

import java.io.DataInputStream;
import java.io.IOException;

import static fr.utarwyn.superjukebox.nbs.InputStreamUtil.*;

/**
 * Decode NBS files with the legacy format.
 * All explanations about the format here:
 * https://www.stuffbydavid.com/mcnbs/format
 *
 * @author Utarwyn <maxime.malgorn@laposte.net>
 * @since 0.3.0
 */
public class LegacyNBSDecoder implements NBSDecoder {

    /**
     * {@inheritDoc}
     */
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
            throw new NBSDecodeException(music, "cannot decode file headers", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short decodeNoteblocks(Music music, DataInputStream dis) throws NBSDecodeException {
        short tick = -1;

        try {
            while (true) {
                // Read all layers of the song
                short tickGap = readShort(dis);
                if (tickGap == 0) break;

                tick += tickGap;

                short layer = -1;
                while (true) {
                    // Read the number of jumps to the next layer
                    short layerGap = readShort(dis);
                    if (layerGap == 0) break;
                    layer += layerGap;

                    // Read the current note
                    Note note = this.decodeNote(dis, music.getVersion() >= 4);

                    if (note.getPanning() != 100) {
                        music.setStereo(true);
                    }

                    music.getLayerOrCreate(layer).addNote(tick, note);
                }
            }
        } catch (IOException e) {
            throw new NBSDecodeException(music, "cannot decode noteblocks", e);
        }

        return tick;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Note decodeNote(DataInputStream dis, boolean additionnalData) throws IOException {
        byte instrument = dis.readByte();
        byte key = dis.readByte();
        return new Note(instrument, key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decodeLayers(Music music, DataInputStream dis) throws NBSDecodeException {
        try {
            for (int i = 0; i < music.getHeight(); i++) {
                Layer layer = music.getLayer(i);
                readString(dis); // layer name
                byte volume = dis.readByte();

                if (layer != null) {
                    layer.setVolume(volume);
                    layer.setPanning(100);
                }
            }
        } catch (IOException e) {
            throw new NBSDecodeException(music, "cannot decode layers", e);
        }
    }

}
