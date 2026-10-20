package com.example.apiBase.apiBancoBase.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apiBase.apiBancoBase.domain.Livro;
import com.example.apiBase.apiBancoBase.repository.LivroRepository;
import com.example.apiBase.apiBancoBase.service.LivroService;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService livroService;
    private final LivroRepository livroRepository;

    //criando chaves de acesso para as classes livroService e repository
    public LivroController(LivroService oLivroService,
            LivroRepository oLivroRepository) {
        this.livroService = oLivroService;
        this.livroRepository = oLivroRepository;
    }

    @PostMapping("/salvar")
    public Livro salvarLivro(@RequestBody Livro oLivro) {
        return livroRepository.save(oLivro);
    }

    @GetMapping("/buscarTodos")
    public List<Livro> listarTodosLivros() {
        return livroService.buscarTodos();
    }

    @PostMapping("/buscarPorTitulo")
    public ResponseEntity<List<Livro>> buscarTitulo(@RequestBody Map<String, String> dadosRequest) {
        String tituloLivro = dadosRequest.get("titulo");
        List<Livro> retornoLivros = livroService.buscarPorNome(tituloLivro);

        return ResponseEntity.ok(retornoLivros);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<Livro> editarLivro(@PathVariable Long id,
            @RequestBody Livro oLivro) {
        Livro livroEditado = livroService.atualizarLivro(id, oLivro);
        return ResponseEntity.ok(livroEditado);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarLivro(@PathVariable Long id) {
        livroRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //metodo para buscar o livro por ID para realizar o editar ou deletar Livro
    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarPorId(@PathVariable Long id) {

        Optional<Livro> livro = livroRepository.findById(id);

        if (livro.isPresent()) {
            return ResponseEntity.ok(livro.get());
        }

        return ResponseEntity.notFound().build();
    }

}
