package com.edutech.msvc.cursos.controllers;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class CursoControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    @DisplayName("Debe entregar un listado con todos los cursos")
    public void shouldReturnAllCursoWhenListIsRequired(){
        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/cursos", String.class);
        assertThat (response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int cursoCount = documentContext.read("$.lenght()");
        assertThat(cursoCount).isEqualTo(1000);
    }
}
