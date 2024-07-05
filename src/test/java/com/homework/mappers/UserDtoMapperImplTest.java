package com.homework.mappers;

import com.homework.dto.UserIncomingDto;
import com.homework.dto.UserOutGoingDto;
import com.homework.dto.UserUpdateDto;
import com.homework.dto.mappers.UserDtoMapper;
import com.homework.entity.Company;
import com.homework.entity.Position;
import com.homework.entity.User;
import com.homework.fabric.Fabric;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class UserDtoMapperImplTest {
    private UserDtoMapper mapper = Fabric.getUserDtoMapper();

    @Test
    void mapIncomingToUser() {
        UserIncomingDto dto = new UserIncomingDto(
                "Федор",
                "Васильев",
                1L, List.of(1L)
        );
        User result = mapper.map(dto);

        Assertions.assertNull(result.getId());
        Assertions.assertEquals(dto.getFirstname(), result.getFirstname());
        Assertions.assertEquals(dto.getLastname(), result.getLastname());
        Assertions.assertEquals(dto.getCompanyId(), result.getCompany().getId());
    }

    @Test
    void mapUserToOutGoing(){
        User user = new User(
                12L,
                "Иван",
                "Иванов",
                new Company(1L, "Яндекс"),
                List.of(new Position(1L, "Admin", null))
        );
        UserOutGoingDto result = mapper.map(user);

        Assertions.assertEquals(user.getId(), result.getId());
        Assertions.assertEquals(user.getFirstname(), result.getFirstname());
        Assertions.assertEquals(user.getLastname(), result.getLastname());
        Assertions.assertEquals(user.getCompany().getName(), result.getCompanyName());
        Assertions.assertEquals(user.getPositions().size(), result.getPositions().size());
    }
    @Test
    void userUpdateToUser(){

        UserUpdateDto dto = new UserUpdateDto(
                12L,
                "Иван",
                "Иванов",
                2L,
                List.of(1L)
        );
        User result = mapper.map(dto);

        Assertions.assertEquals(dto.getId(), result.getId());
        Assertions.assertEquals(dto.getFirstname(), result.getFirstname());
        Assertions.assertEquals(dto.getLastname(), result.getLastname());
        Assertions.assertEquals(dto.getCompanyId(), result.getCompany().getId());
        Assertions.assertEquals(dto.getPositions().size(), result.getPositions().size());
    }

    @Test
    void listUsersToOutGoing(){
        List<User> userList = List.of(
                new User(
                        10L,
                        "Иван",
                        "Иванов",
                        new Company(1L, "Яндекс"),
                        List.of(new Position(1L, "Admin"))
                ),
                new User(
                        11L,
                        "Алексей",
                        "Федоров",
                        new Company(1L, "Epam"),
                        List.of(new Position(2L, "Dev"))

                )
        );
        int result = mapper.map(userList).size();
        Assertions.assertEquals(userList.size(), result);
    }

}
