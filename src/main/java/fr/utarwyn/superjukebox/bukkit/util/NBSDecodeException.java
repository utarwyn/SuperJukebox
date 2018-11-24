package fr.utarwyn.superjukebox.bukkit.util;

import java.io.File;

public class NBSDecodeException extends Exception {

	NBSDecodeException(File file, String message, Exception ex) {
		super("ERROR during the analysis of " + file.getName() + "! " + message, ex);
	}

}
