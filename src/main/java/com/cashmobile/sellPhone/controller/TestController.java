package com.cashmobile.sellPhone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/db")
    public String testDBConnection() throws SQLException {
        try (Connection conn = dataSource.getConnection()){
            return "connected to DB : "+ conn.getMetaData().getURL();
        } catch (Exception e){
            return "Failed to connect : "+ e.getMessage();
        }

    }


}
