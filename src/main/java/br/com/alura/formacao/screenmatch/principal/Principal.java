package br.com.alura.formacao.screenmatch.principal;

import br.com.alura.formacao.screenmatch.model.DadosEpisodio;
import br.com.alura.formacao.screenmatch.model.DadosSerie;
import br.com.alura.formacao.screenmatch.model.DadosTemporada;
import br.com.alura.formacao.screenmatch.model.Episodio;
import br.com.alura.formacao.screenmatch.service.ConsumoAPI;
import br.com.alura.formacao.screenmatch.service.ConverterDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\nTOP 5 Epidosios");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase(("N/A")))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())//ordenando pela avaliacao decrescente
                .limit(5)
                .forEach(System.out::println);


        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(e -> new Episodio(t.numero(), e))
                        ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("A partir de que ano você deseja ver os episodios?");
        var anoSerie = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(anoSerie, 1, 1);

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getDataLancameto() != null && e.getDataLancameto().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                " Episodio: " + e.getNumeroEpisodio() +
                                " Data Lançamento: " + e.getDataLancameto().format(formatador)
                ));


    }
}
