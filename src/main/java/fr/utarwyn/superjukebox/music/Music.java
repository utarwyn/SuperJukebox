package fr.utarwyn.superjukebox.music;

import fr.utarwyn.superjukebox.music.model.Layer;
import fr.utarwyn.superjukebox.util.MaterialHelper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a music which can be played by a MusicPlayer object.
 *
 * @author Utarwyn
 * @since 0.1.0
 */
public class Music {

    private final String filename;

    private short length;

    private short height;

    private String name;

    private String author;

    private String originalAuthor;

    private String description;

    private float tempo;

    private float delay;

    private final Map<Integer, Layer> layers;

    private int version;

    private boolean stereo;

    private ItemStack icon;

    public Music(String filename) {
        this.filename = filename;
        this.name = this.formatFilename();
        this.author = ChatColor.RED.toString() + ChatColor.UNDERLINE + "No author";
        this.originalAuthor = ChatColor.RED.toString() + ChatColor.UNDERLINE + "No original author";
        this.description = ChatColor.RED.toString() + ChatColor.UNDERLINE + "No description";
        this.layers = new HashMap<>();
        this.version = 0;

        this.setIconWithMaterial(MaterialHelper.findMaterial("RECORD_10", "MUSIC_DISC_13"));
    }

    public Map<Integer, Layer> getLayers() {
        return this.layers;
    }

    public String getFilename() {
        return this.filename;
    }

    public short getLength() {
        return this.length;
    }

    public short getHeight() {
        return this.height;
    }

    public String getName() {
        return this.name;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getOriginalAuthor() {
        return this.originalAuthor;
    }

    public String getDescription() {
        return this.description;
    }

    public float getTempo() {
        return this.tempo;
    }

    public float getDelay() {
        return this.delay;
    }

    public Layer getLayer(int index) {
        return this.layers.get(index);
    }

    public Layer getLayerOrCreate(int index) {
        if (!this.layers.containsKey(index)) {
            this.layers.put(index, new Layer());
        }
        return this.layers.get(index);
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isStereo() {
        return this.stereo;
    }

    public ItemStack getIcon() {
        return this.icon;
    }

    public void setLength(short length) {
        this.length = length;
    }

    public void setHeight(short height) {
        this.height = height;
    }

    public void setStereo(boolean stereo) {
        this.stereo = stereo;
    }

    public void setName(String name) {
        if (!name.isEmpty()) {
            this.name = name;
        }
    }

    public void setTempo(float tempo) {
        this.tempo = tempo;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public void setAuthor(String author) {
        if (!author.isEmpty()) {
            this.author = author;
        }
    }

    public void setOriginalAuthor(String originalAuthor) {
        if (!originalAuthor.isEmpty()) {
            this.originalAuthor = originalAuthor;
        }
    }

    public void setDescription(String description) {
        if (!description.isEmpty()) {
            this.description = description;
        }
    }

    void setIconWithMaterial(Material material) {
        this.icon = new ItemStack(material);
        this.updateIconMetadatas();
    }

    /**
     * Updates the icon item to insert all music datas on it.
     */
    private void updateIconMetadatas() {
        ItemMeta iconMeta = this.icon.getItemMeta();

        // Set all metadatas on the Bukkit itemstack
        iconMeta.setDisplayName(ChatColor.GREEN + "♫ " + this.name);
        iconMeta.setLore(Arrays.asList(
                ChatColor.DARK_GRAY + "*-------------------------*",
                ChatColor.GRAY + "Duration: " + ChatColor.YELLOW + this.getFormattedLength(),
                ChatColor.GRAY + "Author: " + ChatColor.AQUA + this.author,
                ChatColor.GRAY + "Original author: " + ChatColor.AQUA + this.originalAuthor,
                ChatColor.GRAY + "Description: " + ChatColor.WHITE + this.description,
                ChatColor.DARK_GRAY + "*-------------------------*",
                "",
                ChatColor.GOLD + "Click to play this music!"
        ));

        // Hide real music name from ths disc!
        iconMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        this.icon.setItemMeta(iconMeta);
    }

    private String formatFilename() {
        return this.filename.replace(".nbs", "")
                .replace("-", " ")
                .replace("_", " ");
    }

    private String getFormattedLength() {
        // Convert the music length into seconds ...
        int seconds = (int) (this.length / this.tempo);
        // ... and into a readable time!
        return (seconds / 60) + ":" + String.format("%02d", seconds % 60);
    }

}
