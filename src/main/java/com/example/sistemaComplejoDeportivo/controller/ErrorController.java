/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.sistemaComplejoDeportivo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author eduar
 */
@Controller
public class ErrorController {

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied";
    }
}
