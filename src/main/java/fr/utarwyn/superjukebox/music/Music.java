package fr.utarwyn.superjukebox.music;

import fr.utarwyn.superjukebox.music.model.Layer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a music which can be played by a MusicPlayer object.
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public class Music {

    private String filename;

    private short length;

    private short height;

    private String name;

    private String author;

    private String originalAuthor;

    private String description;

    private float tempo;

    private float delay;

    private List<Layer> layers;

    private ItemStack icon;

    public Music(String filename) {
        this.filename = filename;
        this.name = ChatColor.RED.toString() + ChatColor.UNDERLINE + "No name";
        this.author = ChatColor.RED.toString() + ChatColor.UNDERLINE + "No author";
        this.originalAuthor = ChatColor.RED.toString() + ChatColor.UNDERLINE + "No original author";
        this.description = ChatColor.RED.toString() + ChatColor.UNDERLINE + "No description";
        this.layers = new ArrayList<>();

        this.setIconWithMaterialId("RECORD_10");
    }

    public List<Layer> getLayers() {
        return new ArrayList<>(this.layers);
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

    public Layer getLayerOrDefault(int key) {
        for (Layer l : this.layers) {
            if (l.getKey() == key) {
                return l;
            }
        }

        Layer layer = new Layer(key);
        this.layers.add(layer);
        return layer;
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

    public void setName(String name) {
    	if (!name.isEmpty()) {
			this.name = name;
		}
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

    public void setTempo(float tempo) {
        this.tempo = tempo;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    void setIconWithMaterialId(String material) {
        this.icon = new ItemStack(Material.valueOf(material));
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

    private String getFormattedLength() {
        // Convert the music length into seconds ...
        int seconds = (int) (this.length / this.tempo);
        // ... and into a readable time!
        return (seconds / 60) + ":" + String.format("%02d", seconds % 60);
    }

}
