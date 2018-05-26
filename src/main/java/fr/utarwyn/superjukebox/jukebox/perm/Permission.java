package fr.utarwyn.superjukebox.jukebox.perm;

import fr.utarwyn.superjukebox.util.JUtil;
import org.bukkit.entity.Player;

/**
 * Permission node to use a feature of a super jukebox!
 * These permissions are editable in the jukebox's settings.
 *
 * @author Utarwyn
 * @version 1.0.0
 */
public class Permission {

	/**
	 * Type of the permission
	 */
	private PermissionType type;

	/**
	 * The string Bukkit permission
	 */
	private String bukkitPermission;

	/**
	 * Create a Jukebox permission with a type and a bukkit permission.
	 *
	 * @param type             Permission type
	 * @param bukkitPermission The bukkit permission
	 */
	public Permission(PermissionType type, String bukkitPermission) {
		this.type = type;
		this.bukkitPermission = bukkitPermission;
	}

	/**
	 * Returns the type of this permission
	 * @return Type of the permission
	 */
	public PermissionType getType() {
		return this.type;
	}

	/**
	 * Returns the bukkit permission
	 * @return Bukkit permission
	 */
	public String getBukkitPermission() {
		return this.bukkitPermission;
	}

	/**
	 * Redefines the type of this permission
	 * @param type Permission type
	 */
	public void setType(PermissionType type) {
		this.type = type;
	}

	/**
	 * Check if a player has the permission specified.
	 *
	 * @return True if the player has the permission
	 */
	public boolean has(Player player) {
		switch (this.type) {
			case OP:
				return player.isOp();
			case WITH_PERM:
				return JUtil.playerHasPerm(player, this.bukkitPermission);
			case EVERYONE:
			default:
				return true;
		}
	}

}
