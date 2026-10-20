package com.example.apiBase.apiBancoBase.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.apiBase.apiBancoBase.domain.Livro;
import com.example.apiBase.apiBancoBase.domain.Usuario;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/front")
public class LivroFrontController {

    @GetMapping("/livros")
    public String listarLivros(Model oModel, HttpSession session) {

        String token = (String) session.getAttribute("token");

        if (token == null) {
            return "redirect:/front/showLogin";
        }

        String url = "http://localhost:8080/api/livros/buscarTodos";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<List> resposta
                = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        request,
                        List.class
                );

        oModel.addAttribute(
                "livros",
                resposta.getBody()
        );

        return "livros";
    }

    @GetMapping("/livros/paginaCadastrar")
    public String showCadastro() {
        return "cadastrarLivro";
    }

    @PostMapping("/livros/salvar")
    public String salvarLivro(
            @RequestParam String titulo,
            @RequestParam String autor,
            @RequestParam String area,
            @RequestParam String editora,
            @RequestParam int anoPublicacao,
            @RequestParam String isbn, HttpSession session) {

        String token = (String) session.getAttribute("token");

        if (token == null) {
            return "redirect:/front/showLogin";
        }

        String url = "http://localhost:8080/api/livros/salvar";

        RestTemplate restTemplate = new RestTemplate();

        Livro livro = new Livro();

        livro.setTitulo(titulo);
        livro.setAutor(autor);
        livro.setArea(area);
        livro.setEditora(editora);
        livro.setAnoPublicacao(anoPublicacao);
        livro.setIsbn(isbn);

        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(token);

        HttpEntity<Livro> request = new HttpEntity<>(livro, headers);

        restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Livro.class
        );

        return "redirect:/front/livros";
    }

    //metodo para chamar a tela de edição de livro
    @GetMapping("/livros/editar/{id}")
    public String formAbrirEdicao(@PathVariable Long id, Model model, HttpSession session) {

        //chamar a API para buscar o livro pelo id junto com token de autenticação
        String token = (String) session.getAttribute("token");

        if (token == null) {
            return "redirect:/front/showLogin";
        }

        String url = "http://localhost:8080/api/livros/" + id;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Livro> resposta
                = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        request,
                        Livro.class
                );

        Livro livro = resposta.getBody();

        model.addAttribute("livro", livro);

        return "editarLivro";
    }

    @PostMapping("/livros/editar")
    public String editarLivro(
            @RequestParam Long id,
            @RequestParam String titulo,
            @RequestParam String autor,
            @RequestParam String area,
            @RequestParam String editora,
            @RequestParam int anoPublicacao,
            @RequestParam String isbn, HttpSession session) {

        String token = (String) session.getAttribute("token");

        if (token == null) {
            return "redirect:/front/showLogin";
        }

        String url = "http://localhost:8080/api/livros/editar/" + id;

        RestTemplate restTemplate = new RestTemplate();
        Livro livro = new Livro();

        livro.setTitulo(titulo);
        livro.setAutor(autor);
        livro.setArea(area);
        livro.setEditora(editora);
        livro.setAnoPublicacao(anoPublicacao);
        livro.setIsbn(isbn);

        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(token);

        HttpEntity<Livro> request = new HttpEntity<>(livro, headers);

        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                Livro.class
        );

        return "redirect:/front/livros";
    }

    @GetMapping("/livros/deletar/{id}")
    public String deletarLivro(@PathVariable Long id, HttpSession session) {

        String token = (String) session.getAttribute("token");

        if (token == null) {
            return "redirect:/front/showLogin";
        }

        String url = "http://localhost:8080/api/livros/deletar/" + id;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                request,
                Void.class
        );

        return "redirect:/front/livros";
    }

    @GetMapping("/showLogin")
    public String abrirLogin() {
        return "login";
    }

    //metodo para realizar o login
    @PostMapping("/login")
    public String realizarLogin(
            @RequestParam String login,
            @RequestParam String senha,
            HttpSession session) {

        String url = "http://localhost:8080/autenticar/login";

        RestTemplate restTemplate = new RestTemplate();

        Usuario usuario = new Usuario();

        usuario.setLogin(login);
        usuario.setSenha(senha);

        try {

            ResponseEntity<Map> resposta
                    = restTemplate.postForEntity(
                            url,
                            usuario,
                            Map.class
                    );

            String token = (String) resposta.getBody().get("token");

            session.setAttribute("token", token);

            return "redirect:/front/livros";

        } catch (HttpClientErrorException.Unauthorized e) {

            return "redirect:/front/showLogin?erro=Login%20ou%20senha%20inv%C3%A1lidos";
        }
    }

}
