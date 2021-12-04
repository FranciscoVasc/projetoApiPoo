package com.api.ficr.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.ficr.model.Empregado;

@Repository
public interface EmpregadoRepositorio extends JpaRepository<Empregado, Long>{

}
