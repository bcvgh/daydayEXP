package com.bcvgh.core.unser.annotation;

import com.bcvgh.controller.VulManagerController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class AnnotationUtils {
    private static final Logger LOGGER = LogManager.getLogger(VulManagerController.class.getName());
    public static <T extends Annotation> T getAnnotation(AnnotatedElement element, Class<T> annotationClass) {
        if (element.isAnnotationPresent(annotationClass)){
            return element.getAnnotation(annotationClass);
        }
        return null;
    }

    public static <T extends Annotation> Object getAnnotationValue(AnnotatedElement element, Class<T> annotationClass, String propertyName) {
        try {
            T annotation = getAnnotation(element, annotationClass);
            if (annotation != null) {

//                String version = (String) annotation.annotationType().getMethod(propertyName).invoke(annotation);
//                return version.split(",");
                    return annotation.annotationType().getMethod(propertyName).invoke(annotation);
            }
        }catch (Exception e){
            LOGGER.warn(e.getMessage());
        }
        return null;
    }
}
