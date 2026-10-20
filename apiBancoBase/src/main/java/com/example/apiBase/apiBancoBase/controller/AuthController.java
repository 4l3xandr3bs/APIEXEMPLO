package com.example.apiBase.apiBancoBase.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apiBase.apiBancoBase.domain.Usuario;
import com.example.apiBase.apiBancoBase.repository.UsuarioRepository;
import com.example.apiBase.apiBancoBase.security.JwtUtil;

@RestController
@RequestMapping("/autenticar")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    //retorna uma resposta para a aplicação, respondendo com um true ou false/ autenticação do usuário para atribuir um token para ele do JWT
    //utilizando responseEntity
    public ResponseEntity<?> login(@RequestBody Usuario oUsuario) {
        
        Optional<Usuario> oUserEncontrado = usuarioRepository.findByLogin(oUsuario.getLogin());
        
        if(oUserEncontrado.isPresent() &&
        oUserEncontrado.get().getSenha().equals(oUsuario.getSenha())) {
            String token = jwtUtil.gerarToken(oUserEncontrado.get().getLogin());

            return ResponseEntity.ok(
                Map.of("token", token)
            );
        }
        
        return ResponseEntity.status(401).body("Autenticação falhou");
    }

}
