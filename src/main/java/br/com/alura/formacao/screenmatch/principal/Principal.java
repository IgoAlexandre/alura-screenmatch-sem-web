package br.com.alura.formacao.screenmatch.principal;

import br.com.alura.formacao.screenmatch.model.DadosEpisodio;
import br.com.alura.formacao.screenmatch.model.DadosSerie;
import br.com.alura.formacao.screenmatch.model.DadosTemporada;
import br.com.alura.formacao.screenmatch.service.ConsumoAPI;
import br.com.alura.formacao.screenmatch.service.ConverterDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private final String ENDERECO_PRINCIPAL = "https://www.omdbapi.com/?t=";
    private final String ENDERECO_SEASON = "&Season=";
    private final String API_KEY = "&apikey=6585022c";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverterDados converterDados = new ConverterDados();

    public void exibeMenu(){
        System.out.println("Digite o nome de série para busca");
        var nomeSerie = leitura.nextLine();
        var nomeSerieTratada = nomeSerie.replace(" ", "+");
        var json = consumoAPI.obterDados(ENDERECO_PRINCIPAL + nomeSerieTratada + API_KEY);
        DadosSerie dadosSerie = converterDados.ObterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);

        buscarDetalhesSerie(nomeSerieTratada, dadosSerie);

    }

    private void buscarDetalhesSerie(String nomeSerieTratada, DadosSerie dadosSerie) {
        String json;
        List<DadosTemporada> temporadas = new ArrayList<>();

        for (Integer i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            json = consumoAPI.obterDados(ENDERECO_PRINCIPAL+ nomeSerieTratada + ENDERECO_SEASON + i + API_KEY);
            temporadas.add(converterDados.ObterDados(json, DadosTemporada.class));
        }
        temporadas.forEach(System.out::println);

//        for(int i = 0; i < dadosSerie.totalTemporadas(); i++){
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for(int j = 0; j < episodiosTemporada.size(); j++){
//                System.out.println(episodiosTemporada.get(j).Titulo());
//            }
//        }

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
    }
}
