package cb.dfs.trail.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value= RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Version {
	
	public int minor() default 0;
	public int major() default 0;
	public int revision()  default 1;

}
