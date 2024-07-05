package com.homework.servlet;

import com.homework.dto.CompanyIncomingDto;
import com.homework.dto.CompanyUpdateDto;
import com.homework.exception.NotFoundException;
import com.homework.service.CompanyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(
        MockitoExtension.class
)

class CompanyServletTest {
    @Mock
    private CompanyService mockCompanyService;
    @InjectMocks
    private CompanyServlet servlet;

    private final HttpServletResponse response = mock(HttpServletResponse.class);

    private final HttpServletRequest request = mock(HttpServletRequest.class);

    private final BufferedReader mockBufferedReader = mock(BufferedReader.class);

    @BeforeEach
    void setUp() throws IOException {
        Mockito.doReturn(new PrintWriter(Writer.nullWriter())).when(response).getWriter();
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockCompanyService);
    }

    @Test
    void doGetById() throws IOException, NotFoundException {
        Mockito.when(request.getParameter("id")).thenReturn("1");
        servlet.doGet(request, response);
        Mockito.verify(mockCompanyService).findById(Mockito.anyLong());
    }

    @Test
    void doGetAll() throws IOException {
        Mockito.when(request.getParameter("id")).thenReturn("all");
        servlet.doGet(request, response);
        Mockito.verify(mockCompanyService).findAll();
    }

    @Test
    void doGetNotFoundException() throws IOException, NotFoundException {
        Mockito.when(request.getParameter("id")).thenReturn("50");
        Mockito.doThrow(new NotFoundException("Компания не найдена")).when(mockCompanyService).findById(50L);
        servlet.doGet(request, response);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void doGetBadRequest() throws IOException {
        Mockito.doReturn("user/>5i").when(request).getParameter("id");
        servlet.doGet(request, response);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doDelete() throws IOException, NotFoundException {
        Mockito.when(request.getParameter("id")).thenReturn("1");
        servlet.doDelete(request, response);
        Mockito.verify(mockCompanyService).delete(Mockito.anyLong());
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void doDeleteNotFound() throws IOException, NotFoundException {
        Mockito.when(request.getParameter("id")).thenReturn("50");
        Mockito.doThrow(new NotFoundException("Компания не найдена")).when(mockCompanyService).delete(50L);
        servlet.doDelete(request, response);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        Mockito.verify(mockCompanyService).delete(50L);
    }

    @Test
    void doDeleteBadRequest() throws IOException {
        Mockito.when(request.getParameter("id")).thenReturn("position/>5i");
        servlet.doDelete(request, response);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPost() throws IOException {
        String name = "Яндекс";
        String jsonRequest = "{\"name\":\"" + name + "\"}";
        when(request.getReader()).thenReturn(mockBufferedReader);
        when(mockBufferedReader.readLine()).thenReturn(jsonRequest, null);
        servlet.doPost(request, response);
        ArgumentCaptor<CompanyIncomingDto> argumentCaptor = ArgumentCaptor.forClass(CompanyIncomingDto.class);
        Mockito.verify(mockCompanyService).save(argumentCaptor.capture());
        CompanyIncomingDto result = argumentCaptor.getValue();
        Assertions.assertEquals(name, result.getName());
    }

    @Test
    void doPut() throws IOException, NotFoundException {
        String name = "Яндекс";
        String jsonRequest = "{\"id\": 1," + "\"name\":\"" + name + "\"}";
        when(request.getReader()).thenReturn(mockBufferedReader);
        when(mockBufferedReader.readLine()).thenReturn(jsonRequest, null);
        servlet.doPut(request, response);
        ArgumentCaptor<CompanyUpdateDto> argumentCaptor = ArgumentCaptor.forClass(CompanyUpdateDto.class);
        Mockito.verify(mockCompanyService).update(argumentCaptor.capture());
        CompanyUpdateDto result = argumentCaptor.getValue();
        Assertions.assertEquals(name, result.getName());
    }
}
