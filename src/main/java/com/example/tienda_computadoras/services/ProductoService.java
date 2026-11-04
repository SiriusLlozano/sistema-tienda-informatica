package com.example.tienda_computadoras.services;

import com.example.tienda_computadoras.dto.request.ActualizarProductoDTO;
import com.example.tienda_computadoras.dto.response.ProductoDetalleDTO;
import com.example.tienda_computadoras.model.Producto;
import com.example.tienda_computadoras.model.Categoria;
import com.example.tienda_computadoras.repository.DetalleCompraRepository;
import com.example.tienda_computadoras.repository.DetalleVentaRepository;
import com.example.tienda_computadoras.repository.InventarioRepository;
import com.example.tienda_computadoras.repository.ProductoRepository;
import com.example.tienda_computadoras.repository.CategoriaRepository;
import com.example.tienda_computadoras.repository.ProductoProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private ProductoProveedorRepository productoProveedorRepository;

    public Producto registrarProducto(
            String nombre,
            String nombreCategoria,
            String modelo,
            BigDecimal precio) {

        Producto producto = new Producto(nombre, obtenerOCrearCategoria(nombreCategoria), modelo);
        producto.setPrecio(precio == null ? BigDecimal.ZERO : precio);

        return productoRepository.save(producto);
    }

    // Devuelve todos los productos guardados
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public List<ProductoDetalleDTO> listarProductosDetalle() {
        return productoRepository.findAll().stream()
                .map(this::convertirADetalleDto)
                .toList();
    }

    public ProductoDetalleDTO buscarProducto(Long productoId) {
        return convertirADetalleDto(obtenerProducto(productoId));
    }

    public ProductoDetalleDTO actualizarProducto(Long productoId, ActualizarProductoDTO dto) {
        Producto producto = obtenerProducto(productoId);
        producto.setNombre(dto.nombre());
        producto.setCategoria(obtenerOCrearCategoria(dto.categoria()));
        producto.setModelo(dto.modelo());
        producto.setPrecio(dto.precio() == null ? BigDecimal.ZERO : dto.precio());
        return convertirADetalleDto(productoRepository.save(producto));
    }

    public void eliminarProducto(Long productoId) {
        Producto producto = obtenerProducto(productoId);
        if (detalleVentaRepository.existsByProducto_Id(productoId)
                || detalleCompraRepository.existsByProducto_Id(productoId)
                || inventarioRepository.existsByProducto_Id(productoId)
                || productoProveedorRepository.existsByProducto_Id(productoId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar el producto porque tiene ventas, compras, inventario o proveedores asociados");
        }
        productoRepository.delete(producto);
    }

    private Producto obtenerProducto(Long productoId) {
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe un producto con el id " + productoId));
    }

    private Categoria obtenerOCrearCategoria(String nombreCategoria) {
        return categoriaRepository.findFirstByNombreCategoriaIgnoreCase(nombreCategoria)
                .orElseGet(() -> categoriaRepository.save(new Categoria(nombreCategoria, null)));
    }

    private ProductoDetalleDTO convertirADetalleDto(Producto producto) {
        return new ProductoDetalleDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getCategoria() == null ? null : producto.getCategoria().getNombreCategoria(),
                producto.getModelo(),
                producto.getPrecio()
        );
    }

    // Lógica 1 (buscar): verifica si un producto está en la tienda
    public String verificarProducto(String nombre) {

        if (nombre.equalsIgnoreCase("Laptop")) {
            return "El producto Laptop está disponible en la tienda";
        } else if (nombre.equalsIgnoreCase("Monitor")) {
            return "El producto Monitor está disponible en la tienda";
        } else {
            return "El producto " + nombre + " no está en la base de datos de la tienda";
        }
    }

    // Lógica 2 (consultar): revisa el historial de un producto
    public String revisarHistorial(String nombre) {

        if (nombre.equalsIgnoreCase("Laptop")) {
            return "El producto Laptop tiene garantía vigente";
        } else if (nombre.equalsIgnoreCase("Monitor")) {
            return "El producto Monitor está agotado";
        } else {
            return "El producto " + nombre + " no está en la base de datos de la tienda";
        }
    }

    // Lógica 3 (pedido): registra un pedido según el tipo de entrega
    public String realizarPedido(String tipo) {

        if (tipo.equalsIgnoreCase("Delivery")) {
            return "Pedido registrado: será enviado a domicilio";
        } else if (tipo.equalsIgnoreCase("Recojo")) {
            return "Pedido registrado: estará listo para recoger en la tienda";
        } else {
            return "El tipo de pedido " + tipo + " no es válido";
        }
    }
}
