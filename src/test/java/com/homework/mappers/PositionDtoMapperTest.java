package com.homework.mappers;

import com.homework.connection.ConnectionManager;
import com.homework.connection.ContainerConnectionManager;
import com.homework.dto.PositionIncomingDto;
import com.homework.dto.PositionOutGoingDto;
import com.homework.dto.PositionUpdateDto;
import com.homework.dto.mappers.PositionDtoMapper;
import com.homework.dto.mappers.impl.PositionDtoMapperImpl;
import com.homework.entity.Position;
import com.homework.entity.User;
import com.homework.fabric.Fabric;
import com.homework.repository.TestUtil;
import com.homework.repository.impl.UserRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
@Testcontainers
class PositionDtoMapperTest {
    @Container
    static PostgreSQLContainer<?> postgres = TestUtil.testUtil();

    ConnectionManager connectionManager = new ContainerConnectionManager(postgres.getJdbcUrl(),
            postgres.getUsername(),postgres.getPassword());

    private PositionDtoMapper mapper = new PositionDtoMapperImpl(new UserRepositoryImpl(connectionManager));

    @Test
    void mapIncomingToPosition() {

        PositionIncomingDto dto = new PositionIncomingDto("Admin", List.of(1L, 2L));
        Position result = mapper.map(dto);
        Assertions.assertNull(result.getId());

    }

    @Test
    void mapPositionToOutGoing() {
        Position position = new Position(1L, "Admin", List.of(new User()));
        PositionOutGoingDto result = mapper.map(position);
        Assertions.assertEquals(position.getId(), result.getId());
        Assertions.assertEquals(position.getName(), result.getName());
        Assertions.assertEquals(position.getUsers().size(), result.getUserList().size());
    }

    @Test
    void mapUpdateToPosition() {
        PositionUpdateDto dto = new PositionUpdateDto(1L, "Dev", List.of(1L));
        Position result = mapper.map(dto);
        Assertions.assertEquals(dto.getId(), result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());
    }

    @Test
    void listPositionsToListOutGoing() {
        List<Position> positions = List.of(new Position(1L, "admin", List.of(new User())),
                new Position(2L, "Dev", List.of(new User())));
        int result = mapper.map(positions).size();
        Assertions.assertEquals(positions.size(), result);

    }
}
