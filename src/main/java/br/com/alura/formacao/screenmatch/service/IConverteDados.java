package br.com.alura.formacao.screenmatch.service;

public interface IConverteDados {
    <T> T ObterDados(String json, Class<T> classe);
}
