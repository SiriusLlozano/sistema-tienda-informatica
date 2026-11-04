package com.example.tienda_computadoras.services;

import com.example.tienda_computadoras.dto.request.CrearClienteDTO;
import com.example.tienda_computadoras.dto.response.ClienteDTO;
import com.example.tienda_computadoras.model.Cliente;
import com.example.tienda_computadoras.repository.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteDTO registrarCliente(CrearClienteDTO dto) {
        String correo = normalizar(dto.correo());
        String telefono = normalizar(dto.telefono());

        if (correo != null && clienteRepository.existsByCorreoIgnoreCase(correo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }
        if (telefono != null && clienteRepository.existsByTelefono(telefono)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El teléfono ya está registrado");
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(normalizar(dto.nombre()));
        cliente.setCorreo(correo);
        cliente.setTelefono(telefono);

        Cliente clienteGuardado = clienteRepository.save(cliente);
        return new ClienteDTO(
                clienteGuardado.getId(),
                clienteGuardado.getNombre(),
                clienteGuardado.getCorreo(),
                clienteGuardado.getTelefono()
        );
    }

    public List<ClienteDTO> listarClientes() {
        return clienteRepository.findAll().stream()
                .map(cliente -> new ClienteDTO(
                        cliente.getId(),
                        cliente.getNombre(),
                        cliente.getCorreo(),
                        cliente.getTelefono()
                ))
                .toList();
    }

    private String normalizar(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor.trim();
    }
}
