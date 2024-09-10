package br.com.sge.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.sge.dto.PedidoEntregaDTO;
import br.com.sge.entities.PedidoEntrega;
import br.com.sge.repositories.ClienteRepository;
import br.com.sge.repositories.PedidoEntregaRepository;
import br.com.sge.repositories.VeiculoRepository;
import br.com.sge.services.exceptions.ResourceNotFoundException;
import br.com.sge.tests.Factory;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class) // Integra o suporte do Spring Test com o JUnit 5
public class PedidoEntregaServiceTests {

    @InjectMocks
    private PedidoEntregaService service;

    @Mock
    private PedidoEntregaRepository repository;

    private long idExistente;
    private long idInexistente;
    private PedidoEntrega pedidoEntrega;

    @BeforeEach
    void setUp() throws Exception {
        idExistente = 2L;
        idInexistente = 30L;
        pedidoEntrega = Factory.createPedidoEntrega(idExistente);

        // Mock para delete, update, e find
        Mockito.doNothing().when(repository).deleteById(idExistente);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(idInexistente);

        // Mock para salvar
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(pedidoEntrega);
        
        // Mock para update
        Mockito.when(repository.getReferenceById(idExistente)).thenReturn(pedidoEntrega);
        Mockito.when(repository.getReferenceById(idInexistente)).thenThrow(EntityNotFoundException.class);

        // Mock para find
        Mockito.when(repository.findById(idExistente)).thenReturn(Optional.of(pedidoEntrega));
        Mockito.when(repository.findById(idInexistente)).thenReturn(Optional.empty());

        // Mock para findAll
        Mockito.when(repository.findAll()).thenReturn(List.of(pedidoEntrega));
    }

    @Test
    public void updateDeveriaAtualizarQuandoIdExistente() {
        PedidoEntregaDTO dto = Factory.createPedidoEntregaDTO();

        dto = service.update(idExistente, dto);

        Assertions.assertNotNull(dto);
        Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(PedidoEntrega.class));
    }

    @Test
    public void insertDeveriaSalvarQuandoIdNulo() {
        PedidoEntregaDTO dto = Factory.createPedidoEntregaDTO();
        dto.setId(null);

        dto = service.insert(dto);

        Assertions.assertNotNull(dto);
        Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(PedidoEntrega.class));
    }

    @Test
    public void findByIdDeveriaLancarResourceNotFoundExceptionQuandoIdInexistente() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(idInexistente);
        });
    }

    @Test
    public void findAllDeveriaRetornarListaDePedidoEntregaDTO() {
        List<PedidoEntregaDTO> resultado = service.findAll();

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.size());

        PedidoEntregaDTO dto = resultado.get(0);
        Assertions.assertEquals(idExistente, dto.getId());
        Mockito.verify(repository, Mockito.times(1)).findAll();
    }
}
