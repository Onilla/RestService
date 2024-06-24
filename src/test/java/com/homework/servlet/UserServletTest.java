package com.homework.servlet;

import com.homework.dto.UserIncomingDto;
import com.homework.dto.UserUpdateDto;
import com.homework.exception.NotFoundException;
import com.homework.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;

import static org.mockito.Mockito.*;

@ExtendWith(
        MockitoExtension.class
)
class UserServletTest {
    @Mock
    private UserService mockUserService;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpServletRequest request;
    @Mock
    private BufferedReader mockBufferedReader;

    @InjectMocks
    private UserServlet servlet;

    @BeforeEach
    void setUp() throws IOException {
        Mockito.doReturn(new PrintWriter(Writer.nullWriter())).when(response).getWriter();
    }

    @Test
    void doGetById() throws IOException, NotFoundException {
        Mockito.when(request.getParameter("id")).thenReturn("2");
        servlet.doGet(request, response);
        Mockito.verify(mockUserService).findById(Mockito.anyLong());
    }

    @Test
    void doGetAll() throws IOException {
        Mockito.when(request.getParameter("id")).thenReturn("all");
        servlet.doGet(request, response);
        Mockito.verify(mockUserService).findAll();
    }

    @Test
    void doGetNotFoundException() throws IOException, NotFoundException {
        Mockito.when(request.getParameter("id")).thenReturn("50");
        Mockito.doThrow(new NotFoundException("Пользователь не найден")).when(mockUserService).findById(50L);
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
        Mockito.verify(mockUserService).delete(Mockito.anyLong());
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void doDeleteNotFound() throws IOException, NotFoundException {
        Mockito.when(request.getParameter("id")).thenReturn("50");
        Mockito.doThrow(new NotFoundException("Пользователь не найден")).when(mockUserService).delete(50L);
        servlet.doDelete(request, response);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        Mockito.verify(mockUserService).delete(50L);
    }

    @Test
    void doDeleteBadRequest() throws IOException {
        Mockito.when(request.getParameter("id")).thenReturn("user/>5i");
        servlet.doDelete(request, response);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPost() throws IOException {
        String firstname = "Иван";
        String lastname = "Иванов";
        String jsonRequest = "{\"firstname\":\"" + firstname + "\"" + ",\"lastname\":\"" + lastname + "\"" + ",\"companyId\":123,\"positions\":[1,2,3]}";
        when(request.getReader()).thenReturn(mockBufferedReader);
        when(mockBufferedReader.readLine()).thenReturn(jsonRequest, null);
        servlet.doPost(request, response);
        ArgumentCaptor<UserIncomingDto> argumentCaptor = ArgumentCaptor.forClass(UserIncomingDto.class);
        Mockito.verify(mockUserService).save(argumentCaptor.capture());
        UserIncomingDto result = argumentCaptor.getValue();
        Assertions.assertEquals(firstname, result.getFirstname());
        Assertions.assertEquals(lastname, result.getLastname());

    }

    @Test
    void doPut() throws IOException, NotFoundException {
        String firstname = "Иван";
        String lastname = "Иванов";
        String jsonRequest = "{\"id\": 1," + "\"firstname\":\"" + firstname + "\"" + ",\"lastname\":\"" + lastname + "\"" + ",\"companyId\":123,\"positions\":[1,2,3]}";
        when(request.getReader()).thenReturn(mockBufferedReader);
        when(mockBufferedReader.readLine()).thenReturn(jsonRequest, null);
        servlet.doPut(request, response);
        ArgumentCaptor<UserUpdateDto> argumentCaptor = ArgumentCaptor.forClass(UserUpdateDto.class);
        Mockito.verify(mockUserService).update(argumentCaptor.capture());
        UserUpdateDto result = argumentCaptor.getValue();
        Assertions.assertEquals(firstname, result.getFirstname());
        Assertions.assertEquals(lastname, result.getLastname());
    }
}
