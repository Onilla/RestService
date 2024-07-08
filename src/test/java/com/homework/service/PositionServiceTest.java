package com.homework.service;

import com.homework.connection.ConnectionManager;
import com.homework.connection.ContainerConnectionManager;
import com.homework.dto.*;
import com.homework.dto.mappers.impl.PositionDtoMapperImpl;
import com.homework.entity.Position;
import com.homework.entity.User;
import com.homework.exception.NotFoundException;
import com.homework.repository.TestUtil;
import com.homework.repository.impl.PositionRepositoryImpl;
import com.homework.repository.impl.UserRepositoryImpl;
import com.homework.service.impl.PositionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(
        MockitoExtension.class
)
@Testcontainers
class PositionServiceTest {

    @Container
    static PostgreSQLContainer<?> postgres = TestUtil.testUtil();

    ConnectionManager connectionManager = new ContainerConnectionManager(postgres.getJdbcUrl(),
            postgres.getUsername(), postgres.getPassword());

    @Mock
    private PositionRepositoryImpl mockRepository;

    @InjectMocks
    public PositionServiceImpl service = new PositionServiceImpl(new PositionRepositoryImpl(connectionManager),
            new PositionDtoMapperImpl(new UserRepositoryImpl(connectionManager)));

    @Test
    void delete() throws NotFoundException {
        Long id = 1L;
        Mockito.doReturn(true).when(mockRepository).existById(1L);
        service.delete(id);
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(mockRepository).deleteById(argumentCaptor.capture());
        Long result = argumentCaptor.getValue();
        Assertions.assertEquals(id, result);
    }

    @Test
    void savePosition() {
        Long id = 1L;
        String positionName = "Admin";
        List<Long> users = new ArrayList<>();
        List<User> usersList = new ArrayList<>();
        Position position = new Position(id, positionName, usersList);
        PositionIncomingDto positionIncomingDto = new PositionIncomingDto(positionName, users);
        when(mockRepository.save(Mockito.any(Position.class))).thenReturn(position);
        PositionOutGoingDto positionOutGoingDto = service.save(positionIncomingDto);
        Assertions.assertNotNull(positionOutGoingDto);
        Assertions.assertEquals(id, positionOutGoingDto.getId());
    }

    @Test
    void update() throws NotFoundException {
        Long id = 1L;
        String positionName = "Admin";
        List<Long> users = new ArrayList<>();
        PositionUpdateDto dto = new PositionUpdateDto(id, positionName, users);
        Mockito.doReturn(true).when(mockRepository).existById(Mockito.any());
        service.update(dto);
        ArgumentCaptor<Position> argumentCaptor = ArgumentCaptor.forClass(Position.class);
        Mockito.verify(mockRepository).update(argumentCaptor.capture());
        Position positionCaptor = argumentCaptor.getValue();
        Assertions.assertEquals(id, positionCaptor.getId());
        Assertions.assertEquals(positionName, positionCaptor.getName());
    }

    @Test
    void findById() throws NotFoundException {
        Long id = 1L;
        String positionName = "Yandex";
        List<User> usersList = new ArrayList<>();
        Position position = new Position(id, positionName, usersList);
        when(mockRepository.findById(id)).thenReturn(Optional.ofNullable(position));
        PositionOutGoingDto savedPosition = service.findById(id);
        Assertions.assertNotNull(savedPosition);
        Assertions.assertEquals(positionName, savedPosition.getName());
    }

    @Test
    void findAll() {
        service.findAll();
        Mockito.verify(mockRepository).findAll();
    }

    @Test
    void findAllNotNull() {
        List<User> usersList = new ArrayList<>();
        Position position = new Position(1L, "Admin", usersList);
        Position position2 = new Position(2L, "Manager", usersList);
        List<Position> list = new ArrayList<>();
        list.add(position);
        list.add(position2);
        when(mockRepository.findAll()).thenReturn(list);
        List<Position> list3 = mockRepository.findAll();
        Assertions.assertNotNull(list3);
    }

}
