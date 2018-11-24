package fr.utarwyn.superjukebox.bukkit.commands;

import fr.utarwyn.superjukebox.bukkit.SuperJukebox;
import fr.utarwyn.superjukebox.bukkit.commands.parameter.Parameter;
import fr.utarwyn.superjukebox.bukkit.util.JUtil;
import fr.utarwyn.superjukebox.bukkit.util.Log;
import fr.utarwyn.superjukebox.bukkit.util.Messages;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.StringUtil;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;

/**
 * Represents a SuperJukebox's command, that's all!
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public abstract class AbstractCommand extends Command implements TabCompleter, CommandExecutor, Listener {

	/**
	 * The Bukkit command map retrieved with reflection
	 */
	private static CommandMap commandMap;

	/**
	 * Permission needed to type the command (for a player)
	 */
	private String permission;

	/**
	 * List all parameters of this command
	 */
	private List<Parameter> parameters;

	/**
	 * List all sub-commands of this command
	 */
	private List<AbstractCommand> subCommands;

	/**
	 * List all arguments for this command
	 */
	private List<String> args;

	/**
	 * Cursor for the next argument count
	 */
	private int nextArg;

	/**
	 * Construct a command!
	 *
	 * @param name    Name of ths command
	 * @param aliases Aliases of the command
	 */
	public AbstractCommand(String name, String... aliases) {
		super(name);

		this.parameters = new ArrayList<>();
		this.subCommands = new ArrayList<>();

		// Add all aliases
		Collections.addAll(this.getAliases(), aliases);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command == null || sender == null || !command.getName().equalsIgnoreCase(getName())) return false;

		// Have we sub-commands for this command?
		if (args.length > 0)
			for (AbstractCommand subCommand : this.subCommands)
				if (subCommand.getName().equalsIgnoreCase(args[0]) || subCommand.getAliases().contains(args[0]))
					return subCommand.onCommand(sender, subCommand, label, Arrays.copyOfRange(args, 1, args.length));


		// Check argument count
		if (!this.checkArgLength(args.length)) {
			sender.sendMessage(Messages.PREFIX + "§cWrong argument count. §6/sj help §cto show the plugin's help!");
			return true;
		}

		// Check also each parameter
		int i = 0;
		for (Parameter param : this.parameters) {
			if (i < args.length && !param.check(args[i])) {
				sender.sendMessage(Messages.PREFIX + "§cParameter §6" + args[i] + " §cis not valid.");
				return true;
			}

			i++;
		}

		// Save all used arguments in memory
		this.args = Arrays.asList(args);

		// Run the command execution for the appropriated entity
		if (sender instanceof Player) {
			Player player = (Player) sender;

			// Player permission
			if (!this.hasRequiredPermission(player)) {
				player.sendMessage(Messages.PREFIX + "§cYou don't have the permission to use this command.");
				return true;
			}

			this.nextArg = 0;
			this.perform(sender);

			this.nextArg = 0;
			this.performPlayer(player);
		} else if (sender instanceof ConsoleCommandSender) {
			this.nextArg = 0;
			this.perform(sender);

			this.nextArg = 0;
			this.performConsole(sender);
		}

		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalStateException {
		List<String> customCompletions = new ArrayList<>();

		// Check sub-commands completions first
		if (args.length > 0)
			for (AbstractCommand subCommand : this.subCommands)
				if (subCommand.getName().equalsIgnoreCase(args[0]) || subCommand.getAliases().contains(args[0]))
					return subCommand.tabComplete(sender, alias, Arrays.copyOfRange(args, 1, args.length));

		// Now check completions of this command
		if (!this.subCommands.isEmpty()) {
			String lastWord = args[args.length - 1];
			List<String> matchedCommands = new ArrayList<>();
			Iterator<AbstractCommand> commandIterator = this.subCommands.iterator();

			AbstractCommand command;
			String cmdName;

			while (true) {
				if (!commandIterator.hasNext()) {
					matchedCommands.sort(String.CASE_INSENSITIVE_ORDER);
					customCompletions = matchedCommands;
					break;
				}

				command = commandIterator.next();
				cmdName = command.getName();

				if (StringUtil.startsWithIgnoreCase(cmdName, lastWord))
					matchedCommands.add(cmdName);
			}
		}

		// Try to add into the list all completions for the current paramter
		int nbCurParam = args.length - 1;
		if (nbCurParam >= 0 && nbCurParam < parameters.size()) {
			Parameter param = parameters.get(nbCurParam);
			boolean hasAccess = !(sender instanceof Player) || this.hasRequiredPermission((Player) sender);

			if (param != null && hasAccess) {
				String lastWord = args[args.length - 1];
				List<String> completions = param.getTabCompletions();


				if (completions != null) {
					// Don't forget to sort all completions :)
					Iterator<String> completionsIterator = completions.iterator();
					List<String> matchedCompletions = new ArrayList<>();
					String completion;

					while (true) {
						if (!completionsIterator.hasNext()) {
							matchedCompletions.sort(String.CASE_INSENSITIVE_ORDER);
							customCompletions.addAll(matchedCompletions);
							break;
						}

						completion = completionsIterator.next();

						if (StringUtil.startsWithIgnoreCase(completion, lastWord))
							matchedCompletions.add(completion);
					}
				} else
					customCompletions.addAll(super.tabComplete(sender, alias, args));
			}
		}

		return customCompletions;
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		return this.onCommand(sender, this, commandLabel, args);
	}

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
		return this.tabComplete(commandSender, s, strings);
	}

	public abstract void perform(CommandSender sender);

	public abstract void performPlayer(Player player);

	public abstract void performConsole(CommandSender sender);

	int getParametersLength() {
		return this.parameters.size();
	}

	@Override
	public void setPermission(String permission) {
		this.permission = permission;
	}

	private boolean hasRequiredPermission(Player player) {
		return this.permission == null || player.hasPermission(this.permission);
	}

	public void setParameters(Parameter... parameters) {
		this.parameters = new ArrayList<>();
		Collections.addAll(this.parameters, parameters);
	}

	public void addParameter(Parameter parameter) {
		this.parameters.add(parameter);
	}

	private boolean checkArgLength(int n) {
		if (n == this.parameters.size()) return true;

		// Count optional parameters instead
		int nb = 0;

		for (Parameter param : this.parameters)
			if (!param.isOptional())
				nb++;

		return n == nb;
	}

	public void setSubCommands(AbstractCommand... commands) {
		this.subCommands = new ArrayList<>();

		for (AbstractCommand command : commands)
			this.addSubCommand(command);
	}

	public void addSubCommand(AbstractCommand command) {
		this.subCommands.add(command);
	}

	public <T> T readArg() {
		return this.readArgAt(this.nextArg);
	}

	public <T> T readArgOrDefault(T def) {
		return this.readArgAtOrDefault(this.nextArg, def);
	}

	@SuppressWarnings("unchecked")
	public <T> T readArgAt(int idx) {
		if (this.args.size() <= idx)
			throw new IllegalArgumentException(idx + " is not a valid arguments count! Registered arguments count: " + this.args.size());

		return this.readArgument(idx);
	}

	@SuppressWarnings("unchecked")
	public <T> T readArgAtOrDefault(int idx, T def) {
		if (this.args.size() <= idx) return def;

		return this.readArgument(idx);
	}

	@SuppressWarnings("unchecked")
	private <T> T readArgument(int idx) {
		this.nextArg = idx + 1;

		String arg = this.args.get(idx);

		if (this.parameters.get(idx).equalsTo(Parameter.STRING))
			return (T) arg;

		return (T) this.parseArg(arg);
	}

	@SuppressWarnings("unchecked")
	private <T> T parseArg(String arg) {
		if (JUtil.isInteger(arg)) return (T) new Integer(arg);
		else if (JUtil.isDouble(arg)) return (T) new Double(arg);
		else if (JUtil.isFloat(arg)) return (T) new Float(arg);

		return (T) arg;
	}

	/**
	 * Register an abstract command directly inside the server's command map.
	 * This method is called by the AsbtractCommand class.
	 *
	 * @param command Object to register inside the Bukkit server
	 */
	public static void register(AbstractCommand command) {
		// Get the command map of the server first!
		if (commandMap == null) {
			try {
				Server server = SuperJukebox.getInstance().getServer();
				Field fMap = server.getClass().getDeclaredField("commandMap");

				fMap.setAccessible(true);
				commandMap = (SimpleCommandMap) fMap.get(server);
				fMap.setAccessible(false);
			} catch (Exception ex) {
				Log.log(Level.WARNING, "Cannot fetch the command map from the server!", ex);
				return;
			}
		}

		commandMap.register("superjukebox", command);
	}

}
