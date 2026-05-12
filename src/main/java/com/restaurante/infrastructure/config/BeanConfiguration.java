package com.restaurante.infrastructure.config;

import com.restaurante.domain.port.input.*;
import com.restaurante.domain.port.output.*;
import com.restaurante.domain.service.*;
import com.restaurante.infrastructure.adapter.outbound.persistence.adapter.*;
import com.restaurante.infrastructure.adapter.outbound.persistence.jpa.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfiguration {

    @Bean
    public CategoriaRepository categoriaRepository(SpringDataCategoriaRepository repo) {
        return new JpaCategoriaRepositoryAdapter(repo);
    }

    @Bean
    public PlatoRepository platoRepository(SpringDataPlatoRepository repo) {
        return new JpaPlatoRepositoryAdapter(repo);
    }

    @Bean
    public MesaRepository mesaRepository(SpringDataMesaRepository repo) {
        return new JpaMesaRepositoryAdapter(repo);
    }

    @Bean
    public PedidoRepository pedidoRepository(SpringDataPedidoRepository repo) {
        return new JpaPedidoRepositoryAdapter(repo);
    }

    @Bean
    public FacturaRepository facturaRepository(SpringDataFacturaRepository repo) {
        return new JpaFacturaRepositoryAdapter(repo);
    }

    @Bean
    public ClienteRepository clienteRepository(SpringDataClienteRepository repo) {
        return new JpaClienteRepositoryAdapter(repo);
    }

    @Bean
    public UsuarioRepository usuarioRepository(SpringDataUsuarioRepository repo) {
        return new JpaUsuarioRepositoryAdapter(repo);
    }

    @Bean
    public GestionMenuUseCase gestionMenuUseCase(
            CategoriaRepository categoriaRepo, PlatoRepository platoRepo) {
        return new GestionMenuService(categoriaRepo, platoRepo);
    }

    @Bean
    public TomarPedidoUseCase tomarPedidoUseCase(
            PedidoRepository pedidoRepo, PlatoRepository platoRepo, MesaRepository mesaRepo) {
        return new TomarPedidoService(pedidoRepo, platoRepo, mesaRepo);
    }

    @Bean
    public GestionMesasUseCase gestionMesasUseCase(MesaRepository mesaRepo) {
        return new GestionMesasService(mesaRepo);
    }

    @Bean
    public FacturacionUseCase facturacionUseCase(
            FacturaRepository facturaRepo, PedidoRepository pedidoRepo, MesaRepository mesaRepo) {
        return new FacturacionService(facturaRepo, pedidoRepo, mesaRepo);
    }

    @Bean
    public AutenticacionUseCase autenticacionUseCase(UsuarioRepository usuarioRepo, PasswordEncoder passwordEncoder) {
        return new AutenticacionService(usuarioRepo, passwordEncoder);
    }

    @Bean
    public GestionClientesUseCase gestionClientesUseCase(ClienteRepository clienteRepo) {
        return new GestionClientesService(clienteRepo);
    }
}
