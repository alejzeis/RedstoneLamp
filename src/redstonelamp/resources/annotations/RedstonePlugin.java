package redstonelamp.resources.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RedstonePlugin {
	/**
	 * The plugin name
	 */
	String name();
	
	/**
	 * The current version of the plugin
	 */
	String version();
	
	/**
	 * The most recent compatible API version your plugin works with
	 */
	double api();
	
	/**
	 * The creator of the plugin
	 */
	String author() default "";
	
	/**
	 * Your plugins description
	 */
	String description() default "";
	
	/**
	 * Your personal website or a support website for your plugin
	 */
	String website() default "";
	
	/**
	 * The URL to the exact file of your plugin that will get updates<br><br>
	 * This is for any plugins that may check or update installed plugins automatically
	 */
	String updateUrl() default "";
}
