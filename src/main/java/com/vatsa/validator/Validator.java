package com.vatsa.validator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Validator {

    public static <T> Counter validate(T entity) {
        if (Objects.isNull(entity)) return new Counter();
        List<Field> fields = getAllFields(entity.getClass());

        return fields.stream().parallel()
                .filter(Validator::shouldProcess)
                .peek(field -> field.setAccessible(true))
                .map(field -> {
                    try {
                        return getCounterForEntity(field, field.get(entity));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return new Counter();
                })
                .reduce(new Counter(), Counter::combine);

    }

    public static List<Field> getAllFields(Class<?> type) {
        List<Field> result = new ArrayList<>();

        Class<?> i = type;
        while (i != null && i != Object.class) {
            Collections.addAll(result, i.getDeclaredFields());
            i = i.getSuperclass();
        }

        return result;
    }

    private static boolean shouldProcess(Field field) {
        return field.isAnnotationPresent(Validate.class) || field.isAnnotationPresent(HasValidations.class);
    }

    /**
     * @param field  the field of the parent object in consideration
     * @param entity the value of the field of the parent object in consideration
     * @param <T>    the datatype of the entity in consideration.  this should be the same as field.getType
     * @return the Counter for the validations and errors
     * <p>
     * Logic:  If an annotation has both the Validate and the HasValidation annotation,
     * then the validate annotation is used as a qualifier,
     * i.e. if the validate annotation is fully valid, then the counter would be the validation of the HasValidate annotation processing
     * if the validate is not fully valid, then the validate annotation's counter is used as the the counter for the field.
     */
    private static <T> Counter getCounterForEntity(Field field, T entity) {
        Counter counter = null;
        boolean isValid = true;
        if (field.isAnnotationPresent(Validate.class)) {
            counter = processValidationAnnotation(field, entity);
            isValid = (counter.valid == counter.total);
        }

        if (isValid && field.isAnnotationPresent(HasValidations.class)) {
            counter = processHasValidationAnnotation(field, entity);
        }
        return counter;
    }

    private static <T> Counter processValidationAnnotation(Field field, T entity) {
        Validate annotation = field.getAnnotation(Validate.class);
        Counter counter = new Counter(0, 1, annotation.message());
        try {
            Method validationMethod = annotation.clazz().getMethod(annotation.method(), annotation.params());
            if (annotation.returnType() == ReturnType.BOOLEAN && (boolean) validationMethod.invoke(null, entity)) {
                counter.update(1, 1);
            } else if (annotation.returnType() == ReturnType.COUNTER) {
                counter = (Counter) validationMethod.invoke(null, entity);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return counter;

    }

    private static <T> Counter processHasValidationAnnotation(Field field, T entity) {
        Counter counter = validate(entity);
        HasValidations annotation = field.getAnnotation(HasValidations.class);
        Counter.reduceOnPolicy(counter, annotation.reductionPolicy());
        return counter;
    }


}
