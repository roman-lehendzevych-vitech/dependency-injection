package org.example.lib;

import org.example.service.FileReaderService;
import org.example.service.ProductParser;
import org.example.service.ProductService;
import org.example.service.impl.FileReaderServiceImpl;
import org.example.service.impl.ProductParserImpl;
import org.example.service.impl.ProductServiceImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Injector {
    private static final Injector injector = new Injector();
    private final Map<Class<?>, Object> instances = new HashMap<>();

    public static Injector getInjector() {
        return injector;
    }

    public Object getInstance(Class<?> interfaceClazz) {
        Object clazzImplementationInstance = null;
        Class<?> clazz = findImplementation(interfaceClazz);
        if (!clazz.isAnnotationPresent(Component.class)) {
            throw new RuntimeException("Injection failed. Annotation @Component is missed in " + clazz.getName());
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            clazzImplementationInstance = getClazzImplementationInstance(clazzImplementationInstance, clazz, field);
        }
        return clazzImplementationInstance != null ? clazzImplementationInstance : createNewInstance(clazz);
    }

    private Object getClazzImplementationInstance(
            Object clazzImplementationInstance, Class<?> clazz, Field field) {
        if (field.isAnnotationPresent(Inject.class)) {
            Object fieldInstance = getInstance(field.getType());
            clazzImplementationInstance = createNewInstance(clazz);
            try {
                field.setAccessible(true);
                field.set(clazzImplementationInstance, fieldInstance);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Can`t initialize field " + field.getName() + "Class: " + clazz.getName(), e);
            }
        }
        return clazzImplementationInstance;
    }

    private Object createNewInstance(Class<?> clazz) {
        if (instances.containsKey(clazz)) {
            return instances.get(clazz);
        }
        try {
            Constructor<?> constructor = clazz.getConstructor();
            Object instance = constructor.newInstance();
            instances.put(clazz, instance);
            return instance;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Cant create a new instance of " + clazz.getName(), e);
        }
    }

    private Class<?> findImplementation(Class<?> interfaceClazz) {
        Map<Class<?>, Class<?>> interfaceImplementations = Map.of(
                FileReaderService.class, FileReaderServiceImpl.class,
                ProductParser.class, ProductParserImpl.class,
                ProductService.class, ProductServiceImpl.class
        );
        if (interfaceClazz.isInterface()) {
            return interfaceImplementations.get(interfaceClazz);
        }
        return interfaceClazz;
    }
}
