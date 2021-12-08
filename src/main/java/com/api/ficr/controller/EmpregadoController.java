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

import com.api.ficr.model.Empregado;
import com.api.ficr.repositorio.EmpregadoRepositorio;

@RestController
@RequestMapping("/api")
public class EmpregadoController {
	
	//Injeção de Depencia para usar o repositório. 
	@Autowired
	EmpregadoRepositorio empregadoRepositorio;
	
	/**
	 * Metodo GET que lista todos os empregados
	 * @return List<Empregado>
	 */
	@GetMapping
	public ResponseEntity<List<Empregado>> listaEmpregados(){
		return ResponseEntity.ok(empregadoRepositorio.findAll());
	}
	
	/**
	 * Metodo post para salvar empregados na base
	 * @param empregado
	 * @return ResponseEntity
	 */
	@PostMapping
	public ResponseEntity<Empregado> post(@RequestBody Empregado empregado){
		Empregado novoEmpregado = empregadoRepositorio.save(empregado);
		//Recuperando Link de consulta do objto salvo. O link aparecerá no HEADER do insomia
		URI location = getUri(novoEmpregado.getMatricula());
		return ResponseEntity.created(location).build();
	}
	
	/**
	 * Metodo que atualiza um empregado na base
	 * @param matricula
	 * @param empregado
	 * @return
	 */
	@PutMapping("/{matricula}")
	public ResponseEntity<Empregado> put(@PathVariable("matricula") Long matricula, @RequestBody Empregado empregado){
		Assert.notNull(matricula,"Matrícula é obrigatória");
		Optional<Empregado> oldEmpregado = empregadoRepositorio.findById(matricula);
		
		Empregado novoEmpregado = new Empregado();
		if(oldEmpregado.isPresent()){
			novoEmpregado.setMatricula(matricula);
			novoEmpregado.setNome(empregado.getNome());
			novoEmpregado.setSexo(empregado.getSexo());
			empregadoRepositorio.save(novoEmpregado);	
		}
		return novoEmpregado.getMatricula() != null ? ResponseEntity.ok(novoEmpregado) : ResponseEntity.notFound().build();
		
	}
	
	/**
	 * Metodo que deleta um usuário do banco
	 * @param matricula
	 * @return
	 */
	@DeleteMapping("/{matricula}")
	public ResponseEntity<Empregado> deleteEmpregado(@PathVariable("matricula") Long matricula){
		Assert.notNull(matricula,"A matrícula deve ser informada!");
		Optional<Empregado> empregado = empregadoRepositorio.findById(matricula);
		if(empregado.isPresent()) 
			empregadoRepositorio.delete(empregado.get());
		
		return empregado != null ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
	}
	
	private URI getUri(Long id) {
			return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri(); 
	}
}
