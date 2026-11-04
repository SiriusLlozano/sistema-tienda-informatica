package com.example.tienda_computadoras.services;

import com.example.tienda_computadoras.dto.request.ActualizarClienteDTO;
import com.example.tienda_computadoras.dto.request.CrearClienteDTO;
import com.example.tienda_computadoras.dto.response.ClienteDTO;
import com.example.tienda_computadoras.model.Cliente;
import com.example.tienda_computadoras.repository.ClienteRepository;
import com.example.tienda_computadoras.repository.DireccionRepository;
import com.example.tienda_computadoras.repository.VentaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final VentaRepository ventaRepository;
    private final DireccionRepository direccionRepository;

    public ClienteService(
            ClienteRepository clienteRepository,
            VentaRepository ventaRepository,
            DireccionRepository direccionRepository) {
        this.clienteRepository = clienteRepository;
        this.ventaRepository = ventaRepository;
        this.direccionRepository = direccionRepository;
    }

    public ClienteDTO registrarCliente(CrearClienteDTO dto) {
        String correo = normalizar(dto.correo());
        String telefono = normalizar(dto.telefono());

        validarUnicidad(correo, telefono, null);

        Cliente cliente = new Cliente();
        cliente.setNombre(normalizar(dto.nombre()));
        cliente.setCorreo(correo);
        cliente.setTelefono(telefono);

        return convertirADto(clienteRepository.save(cliente));
    }

    public List<ClienteDTO> listarClientes() {
        return clienteRepository.findAll().stream()
                .map(this::convertirADto)
                .toList();
    }

    public ClienteDTO buscarCliente(Long clienteId) {
        return convertirADto(obtenerCliente(clienteId));
    }

    public ClienteDTO actualizarCliente(Long clienteId, ActualizarClienteDTO dto) {
        Cliente cliente = obtenerCliente(clienteId);
        String correo = normalizar(dto.correo());
        String telefono = normalizar(dto.telefono());

        validarUnicidad(correo, telefono, clienteId);

        cliente.setNombre(normalizar(dto.nombre()));
        cliente.setCorreo(correo);
        cliente.setTelefono(telefono);
        return convertirADto(clienteRepository.save(cliente));
    }

    public void eliminarCliente(Long clienteId) {
        Cliente cliente = obtenerCliente(clienteId);
        if (ventaRepository.existsByCliente_Id(clienteId)
                || direccionRepository.existsByCliente_Id(clienteId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar el cliente porque tiene ventas o direcciones asociadas");
        }
        clienteRepository.delete(cliente);
    }

    private Cliente obtenerCliente(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe un cliente con el id " + clienteId));
    }

    private void validarUnicidad(String correo, String telefono, Long clienteId) {
        boolean correoDuplicado = correo != null && (clienteId == null
                ? clienteRepository.existsByCorreoIgnoreCase(correo)
                : clienteRepository.existsByCorreoIgnoreCaseAndIdNot(correo, clienteId));
        if (correoDuplicado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }

        boolean telefonoDuplicado = telefono != null && (clienteId == null
                ? clienteRepository.existsByTelefono(telefono)
                : clienteRepository.existsByTelefonoAndIdNot(telefono, clienteId));
        if (telefonoDuplicado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El teléfono ya está registrado");
        }
    }

    private ClienteDTO convertirADto(Cliente cliente) {
        return new ClienteDTO(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getCorreo(),
                cliente.getTelefono()
        );
    }

    private String normalizar(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor.trim();
    }
}
