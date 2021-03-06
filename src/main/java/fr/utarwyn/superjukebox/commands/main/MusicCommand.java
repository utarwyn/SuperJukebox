package fr.utarwyn.superjukebox.commands.main;

import fr.utarwyn.superjukebox.commands.AbstractCommand;
import fr.utarwyn.superjukebox.commands.music.ImportCommand;
import fr.utarwyn.superjukebox.commands.music.ListCommand;
import fr.utarwyn.superjukebox.commands.music.RemoveCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MusicCommand extends AbstractCommand {

    public MusicCommand() {
        super("music", "m");

        this.addSubCommand(new ImportCommand());
        this.addSubCommand(new ListCommand());
        this.addSubCommand(new RemoveCommand());
    }

    @Override
    public void perform(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7&m--------------------&r&8[&3SuperJukebox&8]&7&m--------------------\n" +
                        "&b/&2sj &6music&7/&6m &6import&7/&6i &7<&6URL&7/&6Local_File_Name&7> &b- &aImport a song.\n" +
                        "&b/&2sj &6music&7/&6m &6list&7/&6l &b- &aShow all the imported songs.\n" +
                        "&b/&2sj &6music&7/&6m &6remove&7/&6rm &7<&6Local_File_Name&7> &b- &aRemove a song from the plugin.\n" +
                        "&7&m----------------------------------------------------"));
    }

    @Override
    public void performPlayer(Player player) {
        // Not implemented
    }

    @Override
    public void performConsole(CommandSender sender) {
        // Not implemented
    }

}
