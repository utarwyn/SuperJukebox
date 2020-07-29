package fr.utarwyn.superjukebox.music;

/**
 * Allows the plugin to known the result of a music importation.
 *
 * @author Utarwyn
 * @since 0.1.0
 */
public enum MusicImportResult {
    GOOD,
    MALFORMATED_URL,
    UNKNOWN_FILE,
    ALREADY_IMPORTED,
    DECODING_ERROR
}
