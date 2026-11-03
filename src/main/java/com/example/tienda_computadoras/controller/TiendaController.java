package com.example.tienda_computadoras.controller;

import com.example.tienda_computadoras.dto.request.CrearProductoDTO;
import com.example.tienda_computadoras.dto.response.ProductoDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.tienda_computadoras.model.Producto;
import com.example.tienda_computadoras.model.Venta;
import com.example.tienda_computadoras.services.ProductoService;
import com.example.tienda_computadoras.services.VentaService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

@RestController
public class TiendaController {


    @Autowired
    private ProductoService productoService;

    @Autowired
    private VentaService ventaService;

    // 1. GET para ver el estado general de la tienda
    // URL de prueba: http://localhost:8080/estado
    @GetMapping("/estado")
    public Map<String, String> verificarEstado() {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("sistema", "Tienda de Computadoras");
        respuesta.put("estado", "Abierta y Operativa");
        respuesta.put("mensaje", "Listo para registrar ventas grupal");
        return respuesta;
    }

    // 2. GET para ver los productos disponibles en la tienda (datos fijos)
    // URL de prueba: http://localhost:8080/productos
    @GetMapping("/productos")
    public Map<String, String> listarProductos() {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("producto_1", "Laptop Gamer");
        respuesta.put("producto_2", "Monitor LED 24 pulgadas");
        respuesta.put("producto_3", "Teclado y Mouse Inalámbricos");
        return respuesta;
    }

    // 3. GET para buscar un producto por su nombre (lógica 1)
    // URL de prueba: http://localhost:8080/buscar?nombre=Laptop
    @GetMapping("/buscar")
    public String buscarProducto(@RequestParam String nombre) {
        return productoService.verificarProducto(nombre);
    }

    // 4. GET para consultar el historial de un producto (lógica 2)
    // URL de prueba: http://localhost:8080/consultar?nombre=Monitor
    @GetMapping("/consultar")
    public String consultarProducto(@RequestParam String nombre) {
        return productoService.revisarHistorial(nombre);
    }

    // 5. GET para registrar un pedido según el tipo de entrega (lógica 3)
    // URL de prueba: http://localhost:8080/pedido?tipo=Delivery
    @GetMapping("/pedido")
    public String hacerPedido(@RequestParam String tipo) {
        return productoService.realizarPedido(tipo);
    }


    // 7. GET para ver todos los productos guardados en la base de datos
    // URL de prueba: http://localhost:8080/listar
    @GetMapping("/listar")
    public List<Producto> listarGuardados() {
        return productoService.listarProductos();
    }

    // 8. GET para registrar la venta de un producto (el id sale de /listar)
    // URL de prueba: http://localhost:8080/vender?cliente=Pedro&cantidad=1&productoId=1
    @GetMapping("/vender")
    public String venderProducto(@RequestParam String cliente, @RequestParam int cantidad, @RequestParam Long productoId) {
        return ventaService.registrarVenta(cliente, cantidad, productoId);
    }

    // 9. GET para ver todas las ventas registradas
    // URL de prueba: http://localhost:8080/ventas
    @GetMapping("/ventas")
    public List<Venta> listarVentas() {
        return ventaService.listarVentas();
    }


    @GetMapping("/listar-producto")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProductoDTO> listafiltrada () {
        List<Producto> listareal = productoService.listarProductos();
        List<ProductoDTO> listafiltrada = new ArrayList<>();
        for (Producto m : listareal) {
            ProductoDTO dto = new ProductoDTO(m.getNombre(),m.getCategoria());
            listafiltrada.add(dto);
        }
        return listafiltrada;
    }

    @PostMapping("/guardar-producto")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductoDTO guardarproducto(@RequestBody CrearProductoDTO dto) {
        Producto productoguardado = productoService.registrarProducto(
                dto.nombre(),
                dto.categoria(),
                dto.modelo(),
                dto.precio()
        );
        return new ProductoDTO(
                productoguardado.getNombre(),
                productoguardado.getCategoria()
        );
    }
}

