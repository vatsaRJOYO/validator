# validator

## @Validate Annotation

This annotation to be applied on each field that needs to be validated.

This annotation takes as input, the function to validate the field over validates the field.

### the validation function

the validation function must be a *publicly accessible static method* that 
takes the field's value as input and return either 

- `boolean/Boolean` : Use `ReturnType.BOOLEAN`, where true if valid
- `com.vatsa.validator.Counter` : use `ReturnType.COUNTER`  

| Parameter| Type | Default | Description | 
| -------------| -- | ------------- | --- | 
| returnType | com.vatsa.validator.ReturnType | ReturnType.BOOLEAN  | the return type of the validation function specified. |
| clazz  | Class<?> | *Mandatory field* | the class of the validator method. in case of `Objects.nonNull`, `class = Objects.class` |
| method | String | *Mandatory field* | the name of the validation method to invoke from the `class`  parameter in case of `Objects.nonNull`, `method = "String"`|
| params | Class<?>[] | *Mandatory field* | the list of parameter-types for the validation function. in case of `Objects.nonNull`, `class = Object.class` |
| message | String | "" | the message to be propagated with the count when the validation is false.  only used when `returnType` is `ReturnType.BOOLEAN`. While using `ReturnType.Counter`, message assignment is the responsibility of validation function |

## @HasValidation Annotation

This annotation is to be applied when the Field is a User defined class with nested fields which have validations.  

when a data structure has multiple fields with validations in it, addition operation is used to find the final validation of the complex data structure.  It is possible that the parent expects the while complex data structure to be counted as a single validation, in which case, a reduction policy is to be used to reduce the cummulative validation to a single validation.
this is handled by `reductionPolicy` parameter.  

| Parameter| Type | Default | Description | 
| -------------| -- | ------------- | --- | 
| reductionPolicy | com.vatsa.validator.ReductionPolicy | ReductionPolicy.PROPAGATE | the reduction policy of the validation of the complex data structure |

### ReductionPolicy enum 

| Policy Enum | Description |
| --- | --- |
| PROPAGATE | the Counter received from the complex data structure is sent to parent |
| BINARY_AT_LEAST_ONE | binary counter, valid if atleast one of the child's `counter.valid > 0`  |
| BINARY_MAJORITY | binary counter, valid if `2*counter.valid >= counter.total` |
| BINARY_ALL | binary counter, valid of `counter.valid == counter.total` |


## Both annotations on same field

When both annotations are used on the same field, the `@Validate` annotation acts as a qualifier.  
i.e.  if the `@Validate` annotation's counter is valid ( counter.valid == counter.total ), then the @HasValidation is executed and the counter of @HasValidation is returned as the counter of the field.
