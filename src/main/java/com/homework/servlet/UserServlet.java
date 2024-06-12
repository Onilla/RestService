package com.homework.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.dto.*;
import com.homework.exception.NotFoundException;
import com.homework.service.UserService;
import com.homework.service.impl.UserServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "UserServlet", value = "/user/*")
public class UserServlet extends HttpServlet {

    private final UserService userService = new UserServiceImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String response = "";
        if (req.getParameter("id") != null) {
            try {
                if (req.getParameter("id").equals("all")) {
                    List<UserFullOutGoingDto> userDtoList = userService.findAll();
                    resp.setStatus(HttpServletResponse.SC_OK);
                    response = objectMapper.writeValueAsString(userDtoList);
                } else {
                    Long userId = Long.parseLong(req.getParameter("id"));
                    UserFullOutGoingDto userFullOutGoingDto = userService.findById(userId);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    response = objectMapper.writeValueAsString(userFullOutGoingDto);
                }
            } catch (NotFoundException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response = e.getMessage();
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response = "Неверный аргумент";
            }
        } else {
            try {
                Long companyId = Long.parseLong(req.getParameter("company_id"));
                List<UserFullOutGoingDto> userDtoList = userService.findByCompanyId(companyId);
                resp.setStatus(HttpServletResponse.SC_OK);
                response = objectMapper.writeValueAsString(userDtoList);
            } catch (NotFoundException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response = e.getMessage();
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response = "Неверный аргумент";
            }
        }

        PrintWriter printWriter = resp.getWriter();
        printWriter.write(response);
        printWriter.close();
    }

    private static String mapToJson(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String response = "";
        String requestBody = mapToJson(req);
        Optional<UserIncomingDto> userIncomingDto;
        try {
            userIncomingDto = Optional.ofNullable(objectMapper.readValue(requestBody, UserIncomingDto.class));
            UserIncomingDto userIncoming = userIncomingDto.orElseThrow(IllegalArgumentException::new);
            response = objectMapper.writeValueAsString(userService.save(userIncoming));
        } catch (JsonProcessingException e) {
            response = "Ошибка при обработке JSON";
        } catch (NullPointerException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response = "Не удалось сохранить пользователя";
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response = "Неверный аргумент";
        }

        PrintWriter printWriter = resp.getWriter();
        printWriter.write(response);
        printWriter.close();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String response = "";
        try {
            Long userId = Long.parseLong(req.getParameter("id"));
            if (userService.delete(userId)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                response = "Пользователь удален";
            }
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response = e.getMessage();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response = "Неверный аргумент";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(response);
        printWriter.close();
    }

//    @Override
//    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        resp.setContentType("application/json");
//        String response = "";
//        String requestBody = mapToJson(req);
//
//        try {
//
//            UserUpdateDto userUpdateDto = objectMapper.readValue(requestBody, UserUpdateDto.class);
//            userService.update(userUpdateDto);
//        } catch (JsonProcessingException e) {
//            response = "Ошибка при обработке JSON";
//        } catch (NotFoundException e) {
//            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            response = e.getMessage();
//        } catch (Exception e) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            response = "Неверный аргумент";
//        }
//
//        PrintWriter printWriter = resp.getWriter();
//        printWriter.write(response);
//        printWriter.close();
//    }
}
