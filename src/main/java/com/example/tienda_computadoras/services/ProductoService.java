package com.example.tienda_computadoras.services;

import com.example.tienda_computadoras.model.Producto;
import com.example.tienda_computadoras.model.Categoria;
import com.example.tienda_computadoras.repository.ProductoRepository;
import com.example.tienda_computadoras.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Producto registrarProducto(
            String nombre,
            String nombreCategoria,
            String modelo,
            BigDecimal precio) {

        Categoria categoria = categoriaRepository
                .findFirstByNombreCategoriaIgnoreCase(nombreCategoria)
                .orElseGet(() -> categoriaRepository.save(
                        new Categoria(nombreCategoria, null)
                ));
        Producto producto = new Producto(nombre, categoria, modelo);
        producto.setPrecio(precio == null ? BigDecimal.ZERO : precio);

        return productoRepository.save(producto);
    }

    // Devuelve todos los productos guardados
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
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
