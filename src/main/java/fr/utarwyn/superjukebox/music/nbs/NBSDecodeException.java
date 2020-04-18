package fr.utarwyn.superjukebox.music.nbs;

import java.io.File;

public class NBSDecodeException extends Exception {

    NBSDecodeException(File file, String message, Exception ex) {
        super("ERROR during the analysis of " + file.getName() + "! " + message, ex);
    }

}
