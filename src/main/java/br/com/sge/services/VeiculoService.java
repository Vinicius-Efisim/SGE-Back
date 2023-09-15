package br.com.sge.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sge.dto.VeiculoDTO;
import br.com.sge.entities.Veiculo;
import br.com.sge.repositories.VeiculoRepository;
import br.com.sge.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class VeiculoService {
	@Autowired
	private VeiculoRepository repository;

	@Transactional(readOnly = true)
	public List<VeiculoDTO> findAll() {
		List<Veiculo> lista = repository.findAll();

		return lista.stream().map(x -> new VeiculoDTO(x)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public VeiculoDTO findById(Long id) {
		Optional<Veiculo> obj = repository.findById(id);
		Veiculo entity = obj
				.orElseThrow(() -> new ResourceNotFoundException("O registro solicitado não foi encontrado"));
		return new VeiculoDTO(entity);
	}

	@Transactional
	public VeiculoDTO insert(VeiculoDTO dto) {
		Veiculo entity = new Veiculo();
		entity.setPlaca(dto.getPlaca());
		entity.setDocumento(dto.getDocumento());
		entity.setModelo(dto.getModelo());
		entity.setCapacidade(dto.getCapacidade());
		

		entity = repository.save(entity);

		return new VeiculoDTO(entity);
	}

	@Transactional
	public VeiculoDTO update(Long id, VeiculoDTO dto) {
		try {
			Veiculo entity = repository.getReferenceById(id);
			entity.setPlaca(dto.getPlaca());
			entity.setDocumento(dto.getDocumento());
			entity.setModelo(dto.getModelo());
			entity.setCapacidade(dto.getCapacidade());
			

			entity = repository.save(entity);

			return new VeiculoDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("O recurso com o ID " + id + " não foi localizado");
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("O recurso com o ID " + id + " não foi localizado");
		}
	}
}
