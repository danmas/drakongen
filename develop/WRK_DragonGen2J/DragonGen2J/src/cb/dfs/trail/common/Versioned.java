package cb.dfs.trail.common;

import java.lang.annotation.Annotation;

public abstract class Versioned {

	//@Override
	public String getVersion() {
		String str;
		Class cl = this.getClass();
		if (!cl.isAnnotationPresent(Version.class)) {
			str = "no version for class Trail";
		} else {
			Version v = (Version)cl.getAnnotation(Version.class); 
			str = cl.getSimpleName() + " version " + v.major() + "."
					+ v.minor() + "."
					+ v.revision();
		}
		return str;
	}

	
	public String to_string() {
		return getVersion();
	}
	
}
