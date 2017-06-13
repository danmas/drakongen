package cb.dfs.trail.utils;

import java.util.Map;

public class Enviroment {

	
	public static String getEnvVar(String var_name) {
		String value = System.getenv(var_name);
        if (value != null) {
        	return value;
        } else {
        	return ""; 
        }
	}
	
	
	public static void print_all_env_vars() {
		System.out.println("--- all enviroment vars:");
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n",
                              envName,
                              env.get(envName));
        }
    }
	
    public static void main (String[] args) {
        for (String env: args) {
            String value = System.getenv(env);
            if (value != null) {
                System.out.format("%s=%s%n",
                                  env, value);
            } else {
                System.out.format("%s is"
                    + " not assigned.%n", env);
            }
        }
	}
}
