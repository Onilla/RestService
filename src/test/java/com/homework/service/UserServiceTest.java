package com.homework.service;

import com.homework.connection.ConnectionManager;
import com.homework.connection.ContainerConnectionManager;
import com.homework.dto.*;
import com.homework.dto.mappers.impl.UserDtoMapperImpl;
import com.homework.entity.Company;
import com.homework.entity.Position;
import com.homework.entity.User;
import com.homework.exception.NotFoundException;
import com.homework.repository.TestUtil;
import com.homework.repository.impl.CompanyRepositoryImpl;
import com.homework.repository.impl.PositionRepositoryImpl;
import com.homework.repository.impl.UserRepositoryImpl;
import com.homework.service.impl.UserServiceImpl;
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
class UserServiceTest {

    @Container
    static PostgreSQLContainer<?> postgres = TestUtil.testUtil();

    ConnectionManager connectionManager = new ContainerConnectionManager(postgres.getJdbcUrl(),
            postgres.getUsername(), postgres.getPassword());

    @Mock
    private UserRepositoryImpl mockRepository;

    @InjectMocks
    private UserServiceImpl service = new UserServiceImpl(new UserRepositoryImpl(connectionManager),
            new UserDtoMapperImpl(new CompanyRepositoryImpl(connectionManager),
                    new PositionRepositoryImpl(connectionManager)));

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
    void save() {
        Long id = 1L;
        String firstname = "Ivan";
        String lastname = "Ivanov";
        Company company = new Company(1L, "Yandex");
        List<Long> positionsId = new ArrayList<>();
        List<Position> positions = new ArrayList<>();
        User user = new User(id, firstname, lastname, company, positions);
        UserIncomingDto userIncomingDto = new UserIncomingDto(firstname, lastname, id, positionsId);
        when(mockRepository.save(Mockito.any(User.class))).thenReturn(user);
        UserOutGoingDto userOutGoingDto = service.save(userIncomingDto);
        Assertions.assertNotNull(userOutGoingDto);
        Assertions.assertEquals(id, userOutGoingDto.getId());
    }

    @Test
    void update() throws NotFoundException {
        Long id = 1L;
        String firstname = "Ivan";
        String lastname = "Ivanov";
        List<Long> positionsId = new ArrayList<>();
        UserUpdateDto dto = new UserUpdateDto(id, firstname, lastname, id, positionsId);
        Mockito.doReturn(true).when(mockRepository).existById(Mockito.any());
        service.update(dto);
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(mockRepository).update(argumentCaptor.capture());
        User userCaptor = argumentCaptor.getValue();
        Assertions.assertEquals(id, userCaptor.getId());
        Assertions.assertEquals(lastname, userCaptor.getLastname());
    }

    @Test
    void findById() throws NotFoundException {
        Long id = 1L;
        String firstname = "Ivan";
        String lastname = "Ivanov";
        Company company = new Company(1L, "Yandex");
        List<Position> positions = new ArrayList<>();
        User user = new User(id, firstname, lastname, company, positions);
        when(mockRepository.findById(id)).thenReturn(Optional.ofNullable(user));
        UserOutGoingDto savedUser = service.findById(id);
        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(lastname, savedUser.getLastname());
    }

    @Test
    void findAll() {
        service.findAll();
        Mockito.verify(mockRepository).findAll();
    }

    @Test
    void findAllNotNull() {
        Company company = new Company(1L, "Yandex");
        List<Position> positions = new ArrayList<>();
        User user = new User(1L, "Ivan", "Ivanov", company, positions);
        User user2 = new User(1L, "Petr", "Petrov", company, positions);
        List<User> list = new ArrayList<>();
        list.add(user);
        list.add(user2);
        when(mockRepository.findAll()).thenReturn(list);
        List<User> list3 = mockRepository.findAll();
        Assertions.assertNotNull(list3);
    }
}
