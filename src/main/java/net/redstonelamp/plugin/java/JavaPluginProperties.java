package net.redstonelamp.plugin.java;

import lombok.Getter;
import lombok.Setter;

public class JavaPluginProperties {
	@Getter @Setter private String main = null;
	@Getter @Setter private String version = null;
	@Getter @Setter private String name = null;
	@Getter @Setter private String url = null;
	@Getter @Setter private String[] authors = null;
	@Getter @Setter private String[] depend = null;
	@Getter @Setter private String[] softdepend = null;
}
