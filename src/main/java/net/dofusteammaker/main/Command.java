package net.dofusteammaker.main;

import java.lang.annotation.*;

@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

public @interface Command {
	
	public String name();
	public String description() default "Sans description";
	public ExecutorType type() default ExecutorType.ALL;
	
	public enum ExecutorType{
		ALL, USER, CONSOLE;
	}
	
}
