package com.homework.servlet;

import com.homework.connection.ConnectionManager;
import com.homework.connection.ContainerConnectionManager;
import com.homework.dto.PositionIncomingDto;
import com.homework.dto.PositionUpdateDto;
import com.homework.dto.mappers.impl.PositionDtoMapperImpl;
import com.homework.exception.NotFoundException;
import com.homework.repository.TestUtil;
import com.homework.repository.impl.PositionRepositoryImpl;
import com.homework.repository.impl.UserRepositoryImpl;
import com.homework.service.PositionService;
import com.homework.service.impl.PositionServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(
        MockitoExtension.class
)
@Testcontainers
class PositionServletTest {

    @Container
    static PostgreSQLContainer<?> postgres = TestUtil.testUtil();

    ConnectionManager connectionManager = new ContainerConnectionManager(postgres.getJdbcUrl(),
            postgres.getUsername(), postgres.getPassword());

    private static final PositionService mockPositionService = mock(PositionService.class);

    @InjectMocks
    private PositionServlet servlet = new PositionServlet(new PositionServiceImpl(new PositionRepositoryImpl(connectionManager),
            new PositionDtoMapperImpl(new UserRepositoryImpl(connectionManager))));

    private final HttpServletResponse response = mock(HttpServletResponse.class);

    private final HttpServletRequest request = mock(HttpServletRequest.class);

    private final BufferedReader mockBufferedReader = mock(BufferedReader.class);

    @BeforeEach
    void setUp() throws IOException {
        Mockito.doReturn(new PrintWriter(Writer.nullWriter())).when(response).getWriter();
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockPositionService);
    }

    @Test
    void doGetById() throws IOException, NotFoundException {
        Mockito.when(request.getParameter("id")).thenReturn("1");
        servlet.doGet(request, response);
        Mockito.verify(mockPositionService).findById(Mockito.anyLong());
    }

    @Test
    void doGetAll() throws IOException {
        Mockito.when(request.getParameter("id")).thenReturn("all");
        servlet.doGet(request, response);
        Mockito.verify(mockPositionService).findAll();
    }

    @Test
    void doGetNotFoundException() throws IOException, NotFoundException {
        Mockito.when(request.getParameter("id")).thenReturn("50");
        Mockito.doThrow(new NotFoundException("Должность не найдена")).when(mockPositionService).findById(50L);
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
        Mockito.verify(mockPositionService).delete(Mockito.anyLong());
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void doDeleteNotFound() throws IOException, NotFoundException {
        Mockito.when(request.getParameter("id")).thenReturn("50");
        Mockito.doThrow(new NotFoundException("Позиция не найдена")).when(mockPositionService).delete(50L);
        servlet.doDelete(request, response);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        Mockito.verify(mockPositionService).delete(50L);
    }

    @Test
    void doDeleteBadRequest() throws IOException {
        Mockito.when(request.getParameter("id")).thenReturn("position/>5i");
        servlet.doDelete(request, response);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPost() throws IOException {
        String name = "Администратор";
        String jsonRequest = "{\"name\":\"" + name + "\"" + ",\"userIds\":[1,5]}";
        when(request.getReader()).thenReturn(mockBufferedReader);
        when(mockBufferedReader.readLine()).thenReturn(jsonRequest, null);
        servlet.doPost(request, response);
        ArgumentCaptor<PositionIncomingDto> argumentCaptor = ArgumentCaptor.forClass(PositionIncomingDto.class);
        Mockito.verify(mockPositionService).save(argumentCaptor.capture());
        PositionIncomingDto result = argumentCaptor.getValue();
        Assertions.assertEquals(name, result.getName());

    }

    @Test
    void doPut() throws IOException, NotFoundException {
        String name = "Admin";
        String jsonRequest = "{\"id\": 1," + "\"name\":\"" + name + "\"" + ",\"userIds\":[1,5]}";

        when(request.getReader()).thenReturn(mockBufferedReader);
        when(mockBufferedReader.readLine()).thenReturn(jsonRequest, null);
        servlet.doPut(request, response);
        ArgumentCaptor<PositionUpdateDto> argumentCaptor = ArgumentCaptor.forClass(PositionUpdateDto.class);
        Mockito.verify(mockPositionService).update(argumentCaptor.capture());

        PositionUpdateDto result = argumentCaptor.getValue();
        Assertions.assertEquals(name, result.getName());
    }
}
