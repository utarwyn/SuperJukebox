package fr.utarwyn.superjukebox.music.nbs;

import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.music.model.Note;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * This object decodes given NBS files to create a readable Music object
 * with layers of notes and all metadata of it (name, length, tempo).
 *
 * @author Utarwyn
 * @since 0.1.0
 */
public class NBSDecoder {

    /**
     * It's an utility class, so we don't have to construct anything here.
     */
    private NBSDecoder() {

    }

    /**
     * The main method of the class! Decodes a NBS file into a Music object!
     * Uses Java binary streams and some binary manipulations to decode the given file.
     *
     * @param file File to be decoded
     * @return Readable and formatted Music object
     * @throws NBSDecodeException Throwed if the NBS cannot be decoded correctly.
     */
    public static Music decode(File file) throws NBSDecodeException {
        Music music;

        // Create streams to read the file in a binary mode
        try (FileInputStream fis = new FileInputStream(file); DataInputStream dis = new DataInputStream(fis)) {
            short length;
            short height;
            short tempo;
            String name;
            String author;
            String originalAuthor;
            String description;

            // Read metadata of the music first
            // All explanations of this part of the code is on the Minecraft Note Block Studio's Wiki here:
            // https://www.stuffbydavid.com/mcnbs/format
            // Thank you David for this good help!
            try {
                // Max length of the music
                length = readShort(dis);
                // Max height of the music
                height = readShort(dis);
                // Music name
                name = readString(dis);
                // Author(s)
                author = readString(dis);
                originalAuthor = readString(dis);
                // Description
                description = readString(dis);
                // Tempo
                tempo = readShort(dis);
                // Auto-save and more data not needed
                dis.readByte();
                dis.readByte();
                dis.readByte();
                readInt(dis);
                readInt(dis);
                readInt(dis);
                readInt(dis);
                readInt(dis);
                readString(dis);
            } catch (IOException ex) {
                // Argh! An error in the format of the file maybe?
                throw new NBSDecodeException(file, "Cannot read NBS description!", ex);
            }

            // Instanciate a new music object with all data
            music = new Music(
                    file.getName(), length, height, name,
                    author, originalAuthor, description,
                    tempo / 100f
            );

            // Reading notes and layers...
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

                        addNoteToLayer(music, layer, tick, instrument, note);
                    }
                }
            } catch (IOException ex) {
                // Argh! Cannot read layers and notes... Failure.
                throw new NBSDecodeException(file, "Cannot read NBS music notes!", ex);
            }
        } catch (IOException e) {
            // We cannot open the file :(
            throw new NBSDecodeException(file, "Cannot open the file!", e);
        }

        // All seems to be good!
        return music;
    }

    /**
     * Adds a note to a specific Layer Java object.
     * If the layer does not exist, this method will create a new one.
     *
     * @param music      Music where the layer has to be referenced
     * @param layer      Specific layer level aimed
     * @param tick       Tick where the note is played
     * @param instrument Instrument of the note
     * @param note       The decimal note
     */
    private static void addNoteToLayer(Music music, int layer, int tick, byte instrument, byte note) {
        music.getLayerOrDefault(layer).addNote(tick, new Note(instrument, note));
    }

    /**
     * Read a short value in little-endian (32bit)
     *
     * @param dis The data input stream
     * @return Read value in the stream
     * @throws IOException Throwed if the method cannot read a short at the stream cursor
     */
    private static short readShort(DataInputStream dis) throws IOException {
        int b1 = dis.readUnsignedByte();
        int b2 = dis.readUnsignedByte();

        return (short) (b1 + (b2 << 8));
    }

    /**
     * Read an integer value in little-endian (32bit)
     *
     * @param dis The data input stream
     * @return Read value in the stream
     * @throws IOException Throwed if the method cannot read an integer at the stream cursor
     */
    private static int readInt(DataInputStream dis) throws IOException {
        int b1 = dis.readUnsignedByte();
        int b2 = dis.readUnsignedByte();
        int b3 = dis.readUnsignedByte();
        int b4 = dis.readUnsignedByte();

        return b1 + (b2 << 8) + (b3 << 16) + (b4 << 24);
    }

    /**
     * Read a string, byte by byte.
     *
     * @param dis The data input stream
     * @return Read value in the stream
     * @throws IOException Throwed if the method cannot read a string at the stream cursor
     */
    private static String readString(DataInputStream dis) throws IOException {
        int length = readInt(dis);

        StringBuilder sb = new StringBuilder();

        while (length > 0) {
            char c = (char) dis.readByte();
            sb.append(c);
            length--;
        }

        return sb.toString();
    }

}
