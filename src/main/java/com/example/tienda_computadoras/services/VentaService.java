package com.example.tienda_computadoras.services;

import com.example.tienda_computadoras.model.Producto;
import com.example.tienda_computadoras.model.Cliente;
import com.example.tienda_computadoras.model.DetalleVenta;
import com.example.tienda_computadoras.model.Venta;
import com.example.tienda_computadoras.dto.response.DetalleVentaDTO;
import com.example.tienda_computadoras.dto.response.VentaDTO;
import com.example.tienda_computadoras.repository.ClienteRepository;
import com.example.tienda_computadoras.repository.ProductoRepository;
import com.example.tienda_computadoras.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // Registra la venta de un producto (se busca el producto por su id)
    public String registrarVenta(String cliente, int cantidad, Long productoId) {
        Producto producto = productoRepository.findById(productoId).orElse(null);

        if (producto == null) {
            return "No existe un producto con el id " + productoId;
        }

        Cliente clienteRegistrado = clienteRepository.findFirstByNombreIgnoreCase(cliente)
                .orElseGet(() -> {
                    Cliente nuevoCliente = new Cliente();
                    nuevoCliente.setNombre(cliente);
                    return clienteRepository.save(nuevoCliente);
                });

        Venta venta = new Venta();
        venta.setCliente(clienteRegistrado);
        venta.setFecha(LocalDate.now());

        DetalleVenta detalle = new DetalleVenta();
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(producto.getPrecio());
        venta.agregarDetalle(detalle);

        ventaRepository.save(venta);
        return "Venta registrada: " + cantidad + " x " + producto.getNombre() + " para el cliente " + cliente;
    }

    // Devuelve todas las ventas guardadas
    @Transactional(readOnly = true)
    public List<VentaDTO> listarVentas() {
        return ventaRepository.findAll().stream()
                .map(this::convertirAVentaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public VentaDTO buscarVenta(Long ventaId) {
        return ventaRepository.findById(ventaId)
                .map(this::convertirAVentaDTO)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe una venta con el id " + ventaId));
    }

    @Transactional(readOnly = true)
    public List<VentaDTO> listarVentasPorProducto(Long productoId) {
        if (!productoRepository.existsById(productoId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No existe un producto con el id " + productoId);
        }
        return ventaRepository.findDistinctByDetalles_Producto_IdOrderByFechaDesc(productoId)
                .stream()
                .map(this::convertirAVentaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VentaDTO> listarVentasPorCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No existe un cliente con el id " + clienteId);
        }
        return ventaRepository.findByCliente_IdOrderByFechaDesc(clienteId)
                .stream()
                .map(this::convertirAVentaDTO)
                .toList();
    }

    private VentaDTO convertirAVentaDTO(Venta venta) {
        List<DetalleVentaDTO> detalles = venta.getDetalles().stream()
                .map(detalle -> new DetalleVentaDTO(
                        detalle.getId(),
                        detalle.getCantidad(),
                        detalle.getPrecioUnitario(),
                        detalle.getProducto().getId()
                ))
                .toList();

        return new VentaDTO(
                venta.getId(),
                venta.getFecha(),
                venta.getCliente() == null ? null : venta.getCliente().getId(),
                venta.getEmpleado() == null ? null : venta.getEmpleado().getId(),
                venta.getSucursal() == null ? null : venta.getSucursal().getId(),
                detalles
        );
    }
}
