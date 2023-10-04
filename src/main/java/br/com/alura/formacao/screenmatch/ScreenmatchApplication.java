package br.com.alura.formacao.screenmatch;

import br.com.alura.formacao.screenmatch.model.DadosSerie;
import br.com.alura.formacao.screenmatch.service.ConsumoAPI;
import br.com.alura.formacao.screenmatch.service.ConverterDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c");
		//System.out.println(json);
		ConverterDados converterDados = new ConverterDados();
		DadosSerie dadosSerie = converterDados.ObterDados(json, DadosSerie.class);
		System.out.println(dadosSerie);

	}
}
