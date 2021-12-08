package com.api.ficr.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.ficr.model.Treinamento;
import com.api.ficr.repositorio.TreinamentoRepository;

@RestController
@RequestMapping("/api/treinamento")
public class TreinamentoController {

	// Injeção de Depencia para usar o repositório.
	@Autowired
	TreinamentoRepository treinamentoRepositorio;

	/**
	 * Metodo GET que lista todos os empregados
	 * 
	 * @return List<Treinamento>
	 */
	@GetMapping
	public ResponseEntity<List<Treinamento>> listaTreinamento() {
		return ResponseEntity.ok(treinamentoRepositorio.findAll());
	}

	/**
	 * Metodo que atualizar o objeto Treinamento
	 * 
	 * @param codigo
	 * @param treinamento
	 * @return
	 */
	@PutMapping("/{codigo}")
	public ResponseEntity<Treinamento> atualizarTreinamento(@PathVariable("codigo") Long codigo,@RequestBody Treinamento treinamento) {

		Optional<Treinamento> oldTreinamento = treinamentoRepositorio.findById(codigo);

		Treinamento novoTreinamento = new Treinamento();

		if (oldTreinamento.isPresent()) {
			novoTreinamento.setCodigo(codigo);
			novoTreinamento.setDescricao(treinamento.getDescricao());
			novoTreinamento.setHoraFim(treinamento.getHoraFim());
			novoTreinamento.setHoraInicio(treinamento.getHoraInicio());
			novoTreinamento.setQuantidadeDeVagas(treinamento.getQuantidadeDeVagas());
			novoTreinamento.setCargaHoraria(treinamento.getCargaHoraria());
			treinamentoRepositorio.save(novoTreinamento);
		}
		return oldTreinamento.get().getCodigo() != null ? ResponseEntity.ok(novoTreinamento) : ResponseEntity.notFound().build();

	}

	/**
	 * Metodo post para salvar empregados na base
	 * 
	 * @param treinamento
	 * @return ResponseEntity
	 */
	@PostMapping
	public ResponseEntity<Treinamento> post(@RequestBody Treinamento treinamento) {
		Treinamento novoTreinamento = treinamentoRepositorio.save(treinamento);
		// Recuperando Link de consulta do objto salvo. O link aparecerá no HEADER do
		// insomia
		URI location = getUri(novoTreinamento.getCodigo());
		return ResponseEntity.created(location).build();
	}

	/**
	 * Metodo responsável por deletar o objeto na base
	 * @param codigo
	 * @return
	 */
	@DeleteMapping("/{codigo}")
	public ResponseEntity<Treinamento> deletarTreinamento(@PathVariable("codigo") Long codigo) {
		Assert.notNull(codigo, "O codigo deve ser informado!");

		Optional<Treinamento> treinamento = treinamentoRepositorio.findById(codigo);
		if (treinamento.isPresent())
			treinamentoRepositorio.delete(treinamento.get());

		return treinamento != null ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
	}

	private URI getUri(Long id) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();

	}
}
