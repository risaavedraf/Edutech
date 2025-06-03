package com.edutech.msvc.inscripcion.exceptions;

import com.edutech.msvc.inscripcion.dtos.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Se crea metodo privado que permite generar el error DTO con los elementos basicos del error
    private ErrorDTO createErrorDTO(int status, Date date, Map<String, String> errorMap) {
        ErrorDTO errorDTO = new ErrorDTO();

        errorDTO.setStatus(status);
        errorDTO.setDate(date);
        errorDTO.setErrors(errorMap);

        return errorDTO;
    }

    /**
     * De esta forma se permite capturar los mensajes de error en caso que los argumenteos que fueron solicitado, no
     * cumplan con la condicion de valitaion que fue generadoa futuro.
     * @param exception
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationFields(MethodArgumentNotValidException exception){
        Map<String,String> errorMap = new HashMap<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(this.createErrorDTO(HttpStatus.BAD_REQUEST.value(), new Date(), errorMap));
    }

    @ExceptionHandler(InscripcionException.class)
    public ResponseEntity<ErrorDTO> handleAlumnoException(InscripcionException exception){

        if(exception.getMessage().contains("no se encuentra en la base de datos")) {
            // Esto nos sirve para cuando no existe en la base de datos el alumno
            Map<String, String> errorMap = Collections.singletonMap("Inscripcion no encontrada", exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(this.createErrorDTO(HttpStatus.NOT_FOUND.value(), new Date(), errorMap));


        }else{
            // Esto nos sirve para cuando el alumno ya existe en nuestra base de datos
            Map<String, String> errorMap = Collections.singletonMap("Inscripcion existente", exception.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(this.createErrorDTO(HttpStatus.CONFLICT.value(), new Date(), errorMap));
        }
    }
}
