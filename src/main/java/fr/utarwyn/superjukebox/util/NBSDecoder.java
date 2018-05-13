package fr.utarwyn.superjukebox.util;

import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.music.model.Note;

import java.io.*;

public class NBSDecoder {

	private NBSDecoder() {

	}

	public static Music decode(File file) throws NBSDecodeException {
		FileInputStream fis = null;
		DataInputStream dis = null;

		try {
			fis = new FileInputStream(file);
			dis = new DataInputStream(fis);
		} catch (FileNotFoundException e) {
			throw new NBSDecodeException(file, "Cannot open the file!", e);
		}

		short length, height, tempo;
		String name, author, description;

		try {
			// Longueur de la musique
			length = readShort(dis);
			// Hauteur max de la musique
			height = readShort(dis);
			// Nom de la musique
			name = readString(dis);
			// Auteur
			author = readString(dis);
			readString(dis);
			// Description
			description = readString(dis);
			// Tempo
			tempo = readShort(dis);
			// Auto-save + ...
			dis.readByte();
			dis.readByte();
			dis.readByte();
			readInt(dis);
			readInt(dis);
			readInt(dis);
			readInt(dis);
			readInt(dis);
			readString(dis);
		} catch (IOException e) {
			throw new NBSDecodeException(file, "Cannot read NBS description!", e);
		}

		Music music = new Music(length, height, name, author, description, tempo/100f);

		// Lecture des notes
		short tick = -1;

		try {
			while (true) {
				short jumpsT = readShort(dis);
				if (jumpsT == 0) break;

				tick += jumpsT;

				short layer = -1;
				while (true) {
					short jumpsL = readShort(dis);
					if (jumpsL == 0) break;
					layer += jumpsL;

					byte instrument = dis.readByte();
					byte note = dis.readByte();

					addNoteToLayer(music, layer, tick, instrument, note);
				}
			}
		} catch (IOException e) {
			throw new NBSDecodeException(file, "Cannot read NBS music notes!", e);
		}

		return music;
	}

	private static void addNoteToLayer(Music music, int layer, int tick, byte instrument, byte note) {
		music.getLayerOrDefault(layer).addNote(tick, new Note(instrument, note));
	}

	private static short readShort(DataInputStream dis) throws IOException {
		int b1 = dis.readUnsignedByte();
		int b2 = dis.readUnsignedByte();

		return (short) (b1 + (b2 << 8));
	}

	private static int readInt(DataInputStream dis) throws IOException {
		int b1 = dis.readUnsignedByte();
		int b2 = dis.readUnsignedByte();
		int b3 = dis.readUnsignedByte();
		int b4 = dis.readUnsignedByte();

		return b1 + (b2 << 8) + (b3 << 16) + (b4 << 24);
	}

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
