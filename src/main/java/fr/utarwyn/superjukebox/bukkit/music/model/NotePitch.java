package fr.utarwyn.superjukebox.bukkit.music.model;

/**
 * A note pitch, that's all.
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public enum NotePitch {

	NOTE_00(0, 0.500000F),
	NOTE_01(1, 0.529732F),
	NOTE_02(2, 0.561231F),
	NOTE_03(3, 0.594604F),
	NOTE_04(4, 0.629961F),
	NOTE_05(5, 0.667420F),
	NOTE_06(6, 0.707107F),
	NOTE_07(7, 0.749154F),
	NOTE_08(8, 0.793701F),
	NOTE_09(9, 0.840896F),
	NOTE_10(10, 0.890899F),
	NOTE_11(11, 0.943874F),
	NOTE_12(12, 1.000000F),
	NOTE_13(13, 1.059463F),
	NOTE_14(14, 1.122462F),
	NOTE_15(15, 1.189207F),
	NOTE_16(16, 1.259921F),
	NOTE_17(17, 1.334840F),
	NOTE_18(18, 1.414214F),
	NOTE_19(19, 1.498307F),
	NOTE_20(20, 1.587401F),
	NOTE_21(21, 1.681793F),
	NOTE_22(22, 1.781797F),
	NOTE_23(23, 1.887749F),
	NOTE_24(24, 2.000000F);

	private int key;

	private float pitch;

	NotePitch(int key, float pitch) {
		this.key = key;
		this.pitch = pitch;
	}

	public float getPitch() {
		return this.pitch;
	}

	public static NotePitch get(int key) {
		for (NotePitch np : NotePitch.values())
			if (np.key == key)
				return np;

		return null;
	}

}
