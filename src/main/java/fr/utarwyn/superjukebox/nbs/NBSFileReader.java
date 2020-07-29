package fr.utarwyn.superjukebox.nbs;

import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.nbs.decode.LegacyNBSDecoder;
import fr.utarwyn.superjukebox.nbs.decode.NBSDecoder;
import fr.utarwyn.superjukebox.nbs.decode.OpenNBSDecoder;

import java.io.*;

import static fr.utarwyn.superjukebox.nbs.InputStreamUtil.readShort;

/**
 * Reads a given NBS file and
 * choose the right object to decode it.
 *
 * @author Utarwyn <maxime.malgorn@laposte.net>
 * @since 0.3.0
 */
public class NBSFileReader {

    /**
     * The new NBS reader used for the Open NoteBlockStudio format
     */
    private final NBSDecoder openReader;

    /**
     * The legacy NBS reader used for the depreacated version of NoteBlockStudio
     */
    private final NBSDecoder legacyReader;

    /**
     * Create a new reader for NBS files.
     */
    public NBSFileReader() {
        this.legacyReader = new LegacyNBSDecoder();
        this.openReader = new OpenNBSDecoder();
    }

    /**
     * Decode a music file by reading its bytes stream.
     *
     * @param file file to be decoded
     * @return a music object with all data of the file
     * @throws NBSDecodeException throwed if the nbs cannot be read
     */
    public Music read(File file) throws NBSDecodeException {
        Music music = new Music(file.getName());
        try (DataInputStream stream = this.fileToStream(file)) {
            this.decodeMusic(music, stream, readShort(stream));
            return music;
        } catch (IOException e) {
            throw new NBSDecodeException(music, "Cannot open the file!", e);
        }
    }

    /**
     * Read a file and create a data input stream for it.
     *
     * @param file file to be read
     * @return input stream to read bytes of the file
     * @throws FileNotFoundException throwed if the file cannot be read
     */
    private DataInputStream fileToStream(File file) throws FileNotFoundException {
        return new DataInputStream(new FileInputStream(file));
    }

    /**
     * Decode a music stream by choosing the right decoder.
     * It will determinates the good one thanks to the two first bytes of the file.
     *
     * @param music       music object which stores all song information
     * @param inputStream stream of the music file
     * @param firstBytes  first two bytes of the file
     * @throws NBSDecodeException throwed if the nbs cannot be decoded
     */
    private void decodeMusic(Music music, DataInputStream inputStream, short firstBytes)
            throws NBSDecodeException {
        // Is this file using the new nbs format...
        if (firstBytes == 0) {
            this.openReader.decodeHeader(music, inputStream);
            this.openReader.decodeNoteblocks(music, inputStream);
            this.openReader.decodeLayers(music, inputStream);
        }
        // ... or the legacy one?
        else {
            music.setLength(firstBytes);
            this.legacyReader.decodeHeader(music, inputStream);
            this.legacyReader.decodeNoteblocks(music, inputStream);
            this.legacyReader.decodeLayers(music, inputStream);
        }
    }

}
