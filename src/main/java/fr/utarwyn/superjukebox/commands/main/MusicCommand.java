package fr.utarwyn.superjukebox.commands.main;

import fr.utarwyn.superjukebox.commands.AbstractCommand;
import fr.utarwyn.superjukebox.commands.music.ImportCommand;
import fr.utarwyn.superjukebox.commands.music.ListCommand;
import fr.utarwyn.superjukebox.commands.music.RemoveCommand;
import fr.utarwyn.superjukebox.util.PluginMsg;
import org.bukkit.command.CommandSender;

import static fr.utarwyn.superjukebox.commands.main.HelpCommand.formatCommandFor;

public class MusicCommand extends AbstractCommand {

    public MusicCommand() {
        super("music", "m");

        this.addSubCommand(new ImportCommand());
        this.addSubCommand(new ListCommand());
        this.addSubCommand(new RemoveCommand());
    }

    @Override
    public void perform(CommandSender sender) {
        PluginMsg.pluginBar(sender);

        sender.sendMessage(" ");
        sender.sendMessage("  " + formatCommandFor(sender, "§6/sj music import §e<URL/filename>", "music.import"));
        sender.sendMessage("  §7   ⏩ Import a music file into jukeboxes song list");
        sender.sendMessage("  " + formatCommandFor(sender, "§6/sj music list", "music.list"));
        sender.sendMessage("  §7   ⏩ Show list of all available musics");
        sender.sendMessage("  " + formatCommandFor(sender, "§6/sj music remove §e<filename>", "music.remove"));
        sender.sendMessage("  §7   ⏩ Remove a music from jukeboxes song list");
        sender.sendMessage(" ");
    }

}
