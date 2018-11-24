package fr.utarwyn.superjukebox.bukkit.jukebox.perm;

import fr.utarwyn.superjukebox.bukkit.util.Log;

/**
 * Type that a permission can have.
 *
 * @author Utarwyn
 * @version 1.0.0
 */
public enum PermissionType {

	OP,
	WITH_PERM,
	EVERYONE;

	public static PermissionType getByName(String permissionTypeName) {
		try {
			return PermissionType.valueOf(permissionTypeName);
		} catch (Exception ex) {
			Log.error("Impossible to resolve permission " + permissionTypeName + "! Back to default one.");
			return getDefault();
		}
	}

	/**
	 * Returns the default permission for all features of a jukebox.
	 *
	 * @return The default permission, that's all!
	 */
	public static PermissionType getDefault() {
		return WITH_PERM;
	}

	/**
	 * Returns the next permission type in the list
	 *
	 * @return Next permission type
	 */
	public PermissionType next() {
		boolean next = false;

		for (PermissionType value : values()) {
			if (value == this) {
				next = true;
			} else if (next) {
				return value;
			}
		}

		return values()[0];
	}

}
