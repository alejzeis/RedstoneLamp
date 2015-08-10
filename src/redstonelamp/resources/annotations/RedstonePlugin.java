package redstonelamp.resources.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RedstonePlugin {
	String name() default "";
	
	String version();
	
	double api();
	
	String author() default "";
	
	String description() default "";
	
	String website() default "";
}
