package fr.utarwyn.superjukebox.nbs.decode;

import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.music.model.Layer;
import fr.utarwyn.superjukebox.music.model.Note;
import fr.utarwyn.superjukebox.nbs.NBSDecodeException;
import fr.utarwyn.superjukebox.util.JUtil;

import java.io.DataInputStream;
import java.io.IOException;

import static fr.utarwyn.superjukebox.nbs.InputStreamUtil.readShort;
import static fr.utarwyn.superjukebox.nbs.InputStreamUtil.readString;

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
    private final NBSDecoder parentNBSDecoder;

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
            // version of the file
            music.setVersion(dis.readByte());
            // vanilla instrument count
            music.setCustomInstrumentOffset(JUtil.getCustomInstrumentOffset() - dis.readByte());

            if (music.getVersion() >= 3) {
                music.setLength(readShort(dis)); // music length
            }

            // Decode other headers with the parent decoder
            this.parentNBSDecoder.decodeHeader(music, dis);

            // Decode extra headers in this version
            if (music.getVersion() >= 4) {
                dis.readByte(); // loop on/off
                dis.readByte(); // max loop count
                readShort(dis); // loop start tick
            }
        } catch (IOException e) {
            throw new NBSDecodeException(music, "cannot decode file headers", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short decodeNoteblocks(Music music, DataInputStream dis) throws NBSDecodeException {
        short length = this.parentNBSDecoder.decodeNoteblocks(music, dis);
        if (music.getVersion() < 3) {
            music.setLength(length);
        }
        return length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Note decodeNote(DataInputStream dis, int customInstrumentOffset,
                           boolean additionnalData) throws IOException {
        byte instrument = dis.readByte();
        byte key = dis.readByte();

        if (additionnalData) {
            byte velocity = dis.readByte();
            int panning = 200 - dis.readUnsignedByte();
            short pitch = readShort(dis);

            return new Note(instrument, key, velocity, panning, pitch);
        } else {
            return new Note(instrument, key);
        }
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
                if (music.getVersion() >= 4) {
                    dis.readByte(); // layer lock
                }

                byte volume = dis.readByte();
                int panning = 100;
                if (music.getVersion() >= 2) {
                    panning = 200 - dis.readUnsignedByte();
                }

                if (panning != 100) {
                    music.setStereo(true);
                }

                if (layer != null) {
                    layer.setVolume(volume);
                    layer.setPanning(panning);
                }
            }
        } catch (IOException e) {
            throw new NBSDecodeException(music, "cannot decode layers", e);
        }
    }

}
