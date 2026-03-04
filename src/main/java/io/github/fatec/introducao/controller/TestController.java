package io.github.fatec.introducao.controller;

import io.github.fatec.introducao.dto.UserDataDTO;
import io.github.fatec.introducao.dto.UserUpdateResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/teste")
public class TestController {

    private final Map<String, UserDataDTO> userDatabase = new ConcurrentHashMap<>(); //ConcurrentHashMap para simular banco de dados em memória.

    @GetMapping
    public String getRoot() {
        return "Rodando";
    }

    @GetMapping(value = "/random-id")
    public String teste() {
        String id = UUID.randomUUID().toString();
        return id;
    }

    // GET http://localhost:8080/teste/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserUpdateResponseDTO> getUserById(@PathVariable String id) {
        UserDataDTO userData = userDatabase.get(id);
        if (userData != null) {
            UserUpdateResponseDTO response = new UserUpdateResponseDTO(id, userData.getNome(), userData.getTelefone(), userData.getEndereco());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST http://localhost:8080/teste
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserUpdateResponseDTO receivePost(@RequestBody UserDataDTO userData) {
        String newId = UUID.randomUUID().toString();
        userDatabase.put(newId, userData);
        return new UserUpdateResponseDTO(newId, userData.getNome(), userData.getTelefone(), userData.getEndereco());
    }

    // PUT http://localhost:8080/teste/id
    @PutMapping("/{id}")
    public ResponseEntity<UserUpdateResponseDTO> receivePut(@PathVariable String id, @RequestBody UserDataDTO userData) {
        if (userDatabase.containsKey(id)) {
            userDatabase.put(id, userData);
            UserUpdateResponseDTO response = new UserUpdateResponseDTO(id, userData.getNome(), userData.getTelefone(), userData.getEndereco());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE http://localhost:8080/teste/id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> receiveDelete(@PathVariable String id) {
        if (userDatabase.remove(id) != null) {
            return ResponseEntity.ok("Usuário com ID " + id + " foi deletado com sucesso.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
