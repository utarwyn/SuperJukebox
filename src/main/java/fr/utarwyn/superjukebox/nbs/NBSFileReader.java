package fr.utarwyn.superjukebox.nbs;

import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.nbs.decode.LegacyNBSDecoder;
import fr.utarwyn.superjukebox.nbs.decode.NBSDecoder;
import fr.utarwyn.superjukebox.nbs.decode.OpenNBSDecoder;

import java.io.*;

import static fr.utarwyn.superjukebox.nbs.InputStreamUtil.readShort;

/**
 * This object reads a given NBS file and
 * choose the right object to decode it.
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public class NBSFileReader {

    /**
     * The new NBS reader used for the Open NoteBlockStudio format
     */
    private NBSDecoder openReader;

    /**
     * The legacy NBS reader used for the depreacated version of NoteBlockStudio
     */
    private NBSDecoder legacyReader;

    /**
     * Create a new reader for NBS files.
     */
    public NBSFileReader() {
        this.openReader = new OpenNBSDecoder();
        this.legacyReader = new LegacyNBSDecoder();
    }

    /**
     * Decode a music file by reading its bytes stream.
     *
     * @param file file to be decoded
     * @return a music object with all data of the file
     * @throws NBSDecodeException throwed if the nbs cannot be read
     */
    public Music read(File file) throws NBSDecodeException {
        try (DataInputStream stream = this.fileToStream(file)) {
            return this.decodeMusic(file.getName(), stream, readShort(stream));
        } catch (IOException e) {
            throw new NBSDecodeException(file.getName(), "Cannot open the file!", e);
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
     * @param filename name of the music file
     * @param inputStream stream of the music file
     * @param firstBytes first two bytes of the file
     * @return the decoded music with all data
     * @throws NBSDecodeException throwed if the nbs cannot be decoded
     */
    private Music decodeMusic(String filename, DataInputStream inputStream, short firstBytes) throws NBSDecodeException {
        Music music = new Music(filename);

        // Is this file using the new nbs format...
        if (firstBytes == 0) {
            this.openReader.decodeHeader(music, inputStream);
            this.openReader.decodeNoteblocks(music, inputStream);
        }
        // ... or the legacy one?
        else {
            music.setLength(firstBytes);
            this.legacyReader.decodeHeader(music, inputStream);
            this.legacyReader.decodeNoteblocks(music, inputStream);
        }

        return music;
    }

}
