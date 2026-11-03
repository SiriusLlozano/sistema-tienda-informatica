package com.example.tienda_computadoras.services;

import com.example.tienda_computadoras.model.Producto;
import com.example.tienda_computadoras.model.Cliente;
import com.example.tienda_computadoras.model.DetalleVenta;
import com.example.tienda_computadoras.model.Venta;
import com.example.tienda_computadoras.repository.ClienteRepository;
import com.example.tienda_computadoras.repository.ProductoRepository;
import com.example.tienda_computadoras.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }
}
