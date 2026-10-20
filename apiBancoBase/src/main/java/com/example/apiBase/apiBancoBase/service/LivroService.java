package com.example.apiBase.apiBancoBase.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.apiBase.apiBancoBase.domain.Livro;
import com.example.apiBase.apiBancoBase.repository.LivroRepository;

@Service
public class LivroService {

    //criação da variavel de acesso para a repository Livro
    private final LivroRepository livroRepository;

    //construtor da Service, que será executado quando a classe for
    //executada
    public LivroService(LivroRepository oLivroRepository) {
        this.livroRepository = oLivroRepository;
    }

    public List<Livro> buscarPorNome(String titulo) {

        return livroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Livro> buscarTodos() {
        return livroRepository.findAll();
    }

    public Livro atualizarLivro(Long id, Livro oLivroAlterado){
        Livro livroExistente = livroRepository.findById(id).
        orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        
        if(livroExistente != null){
            livroExistente.setTitulo(oLivroAlterado.getTitulo());
            livroExistente.setAutor(oLivroAlterado.getAutor());
            livroExistente.setArea(oLivroAlterado.getArea());
            livroExistente.setEditora(oLivroAlterado.getEditora());
            livroExistente.setAnoPublicacao(oLivroAlterado.getAnoPublicacao());
            livroExistente.setIsbn(oLivroAlterado.getIsbn());
            return livroRepository.save(livroExistente);
        }
        
        return null;
    }

}
