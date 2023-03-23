package com.xuno.message.controller;


import com.xuno.message.dto.MessageResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping()
public class EchoController {


    @GetMapping(value = "")
    public ResponseEntity<?> echo(@RequestParam(required = false) String echo) {
        MessageResponseDTO<String> messageResponseDTO = new MessageResponseDTO<String>();
        messageResponseDTO.setSuccess(true);
        if (!Objects.isNull(echo)) {
            messageResponseDTO.setData(echo);
        }
        messageResponseDTO.setMessage("Welcome to XUNO Simple Message assignment");
        messageResponseDTO.setStatus(100);
        return ResponseEntity.ok(messageResponseDTO);
    }
}
