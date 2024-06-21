package br.com.sge.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.sge.entities.NivelDeAcesso;
@Repository
public interface NivelDeAcessoRepository extends JpaRepository<NivelDeAcesso, Long> {
}
