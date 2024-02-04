package com.bcvgh.core.unser.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DependenciesVersion {
	String value();

//	public static class Utils {
//		public static String getDependenciesVersion(AnnotatedElement annotated) {
//			DependenciesVersion deps = annotated.getAnnotation(DependenciesVersion.class);
//			if (deps != null && deps.value() != null) {
//				return deps.value();
//			} else {
//				return null;
//			}
//		}

//	}
}
