package fr.utarwyn.superjukebox.bukkit.util;

import fr.utarwyn.superjukebox.bukkit.Config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation Configurable
 * Used to fill attributes in {@link Config Config class}.
 *
 * @author Utarwyn
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Configurable {

	/**
	 * Custom key in the config YML file
	 *
	 * @return The config key
	 */
	String key() default "";

}