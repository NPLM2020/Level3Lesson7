package runtime;

import annotations.AfterSuite;
import annotations.BeforeSuite;
import annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class TestEngine {

    private static final int MAX_ORDER = 10;
    private static final int MIN_ORDER = 1;

    public static void start(Class aClass) {
        System.out.printf("Tests run for the %s", aClass);
        runSuite(aClass, BeforeSuite.class);
        runTests(aClass);
        runSuite(aClass, AfterSuite.class);
        System.out.printf("Tests have completed for the %s", aClass);
    }

    private static void runMethod(Class aClass, Method method) {
        System.out.printf("\nTest %s is invoked", method);
        try {
            System.out.println(method.invoke(aClass.getDeclaredConstructor().newInstance()));
        } catch (IllegalAccessException e) {
            System.out.println("Test is failed. Method is not accessible.");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            System.out.println("Test is failed. Method throws an exception.");
            e.printStackTrace();
        } catch (InstantiationException e) {
            System.out.println("Test is failed. Unable to create object by default constructor.");
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            System.out.println("Test is failed. Method is not present in the class.");
            e.printStackTrace();
        }
    }

    private static void runSuite(Class aClass, Class<?> annotClass) {
        boolean isSuitePresented = false;
        for (Method method : aClass.getDeclaredMethods()) {
            for (Annotation annotation : method.getDeclaredAnnotations()) {
                if (annotClass.isInstance(annotation)) {
                    if (isSuitePresented) {
                        throw new RuntimeException(String.format("There should be only one %s a annotation per class",
                                annotClass.getName()));
                    }
                    isSuitePresented = true;
                    runMethod(aClass, method);
                }
            }
        }
    }

    private static void runTests(Class aClass) {
        Map<Integer, List<Method>> methodsMap = new TreeMap<>();
        for (Method method : aClass.getDeclaredMethods()) {
            for (Annotation annotation : method.getDeclaredAnnotations()) {
                if (annotation instanceof Test) {
                    int key = ((Test) annotation).order();
                    if (key < MIN_ORDER || key > MAX_ORDER) {
                        System.out.printf("\nThe method %s has incorrect order %s. The method will not run.",
                                method, key);
                    } else {
                        if (!methodsMap.containsKey(key)) {
                            List<Method> methodsList = new ArrayList<>();
                            methodsList.add(method);
                            methodsMap.put(key, methodsList);
                        } else {
                            methodsMap.get(key).add(method);
                        }
                    }
                }
            }
        }
        for(Map.Entry<Integer, List<Method>> entry : methodsMap.entrySet()) {
            for (Method m : entry.getValue()) {
                runMethod(aClass, m);
            }
        }
    }


}
