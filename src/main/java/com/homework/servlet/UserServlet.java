package com.homework.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.dto.*;
import com.homework.exception.NotFoundException;
import com.homework.fabric.Fabric;
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

@WebServlet(name = "UserServlet", value = "/user/*")
public class UserServlet extends HttpServlet {

    private transient UserService userService;
    private ObjectMapper objectMapper;

    public UserServlet(){
        this.userService = Fabric.getUserService();
        this.objectMapper = Fabric.getObjectMapper();
    }

    public static void setJson(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJson(resp);
        String response;
        if (req.getParameter("id") != null) {
            try {
                if (req.getParameter("id").equals("all")) {
                    List<UserOutGoingDto> userDtoList = userService.findAll();
                    resp.setStatus(HttpServletResponse.SC_OK);
                    response = objectMapper.writeValueAsString(userDtoList);
                } else {
                    Long userId = Long.parseLong(req.getParameter("id"));
                    UserOutGoingDto userOutGoingDto = userService.findById(userId);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    response = objectMapper.writeValueAsString(userOutGoingDto);
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
        setJson(resp);
        String response;
        String requestBody = mapToJson(req);
        try {
            UserIncomingDto userIncomingDto = objectMapper.readValue(requestBody, UserIncomingDto.class);
            response = objectMapper.writeValueAsString(userService.save(userIncomingDto));
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
            userService.delete(userId);
            resp.setStatus(HttpServletResponse.SC_OK);
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

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJson(resp);
        String response = "";
        String requestBody = mapToJson(req);
        try {
            UserUpdateDto userUpdateDto = objectMapper.readValue(requestBody, UserUpdateDto.class);
            userService.update(userUpdateDto);
        } catch (JsonProcessingException e) {
            response = "Ошибка при обработке JSON";
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
}
