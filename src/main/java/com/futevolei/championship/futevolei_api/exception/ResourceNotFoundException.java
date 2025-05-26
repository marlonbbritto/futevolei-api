package com.futevolei.championship.futevolei_api.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message){
        super(message);
    }

    public ResourceNotFoundException(String resourceName,String fieldName,Object fieldValue ){
        super(String.format("%s não encontrado com %s : '%s'", resourceName, fieldName,fieldValue));
    }

    public ResourceNotFoundException(String resourceName, Long id){
        super(String.format("%s com  ID %d não encontrado.",resourceName,id));
    }
}
