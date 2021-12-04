package com.api.ficr.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.ficr.model.Treinamento;

@Repository
public interface TreinamentoRepository extends JpaRepository<Treinamento, Long>{

}
