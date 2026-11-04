package com.example.tienda_computadoras.controller;

import com.example.tienda_computadoras.dto.request.CrearProductoDTO;
import com.example.tienda_computadoras.dto.request.CrearPedidoDTO;
import com.example.tienda_computadoras.dto.request.RegistrarVentaDTO;
import com.example.tienda_computadoras.dto.request.CrearClienteDTO;
import com.example.tienda_computadoras.dto.response.ClienteDTO;
import com.example.tienda_computadoras.dto.response.ProductoDTO;
import com.example.tienda_computadoras.dto.response.VentaDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.tienda_computadoras.model.Producto;
import com.example.tienda_computadoras.services.ProductoService;
import com.example.tienda_computadoras.services.ClienteService;
import com.example.tienda_computadoras.services.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

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

    @Autowired
    private ClienteService clienteService;

    // 1. GET para ver el estado general de la tienda
    // URL de prueba: http://localhost:8080/estado
    @GetMapping("/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public String buscarProducto(@RequestParam String nombre) {
        return productoService.verificarProducto(nombre);
    }

    // 4. GET para consultar el historial de un producto (lógica 2)
    // URL de prueba: http://localhost:8080/consultar?nombre=Monitor
    @GetMapping("/consultar")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public String consultarProducto(@RequestParam String nombre) {
        return productoService.revisarHistorial(nombre);
    }

    // Prueba en Swagger UI o Postman: POST http://localhost:8080/pedido
    // En Postman selecciona Body > raw > JSON y envía: {"tipo":"Delivery"}
    // El navegador no puede enviar este JSON pegándolo en la barra de direcciones.
    // Requiere autenticación con rol ADMIN o VENDEDOR.
    @PostMapping("/pedido")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public String hacerPedido(@RequestBody CrearPedidoDTO dto) {
        return productoService.realizarPedido(dto.tipo());
    }


    // 7. GET para ver todos los productos guardados en la base de datos
    // URL de prueba: http://localhost:8080/listar
    @GetMapping("/listar")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public List<Producto> listarGuardados() {
        return productoService.listarProductos();
    }

    // POST http://localhost:8080/clientes
    // JSON: {"nombre":"Pedro","correo":"pedro@ejemplo.com","telefono":"999123456"}
    @PostMapping("/clientes")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteDTO registrarCliente(@RequestBody CrearClienteDTO dto) {
        return clienteService.registrarCliente(dto);
    }

    // GET http://localhost:8080/clientes
    // Devuelve los clientes en formato ClienteDTO. Requiere ADMIN o VENDEDOR.
    @GetMapping("/listar-clientes")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public List<ClienteDTO> listarClientes() {
        return clienteService.listarClientes();
    }

    // Primero consulta GET http://localhost:8080/listar para obtener un producto y su id.
    // Prueba en Swagger UI o Postman: POST http://localhost:8080/vender
    // En Postman selecciona Body > raw > JSON y envía: {"cliente":"Pedro","cantidad":1,"productoId":1}
        // Requiere autenticación con rol ADMIN o VENDEDOR.
        @PostMapping("/vender")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public String venderProducto(@RequestBody RegistrarVentaDTO dto) {
        return ventaService.registrarVenta(dto.cliente(), dto.cantidad(), dto.productoId());
    }

    // 9. GET para ver todas las ventas registradas
    // URL de prueba: http://localhost:8080/ventas
    @GetMapping("/ventas")
    @PreAuthorize("hasRole('ADMIN')")
    public List<VentaDTO> listarVentas() {
        return ventaService.listarVentas();
    }

    // GET http://localhost:8080/ventas/{ventaId}
    // Solo ADMIN puede consultar el detalle de una venta.
    @GetMapping("/ventas/{ventaId}")
    @PreAuthorize("hasRole('ADMIN')")
    public VentaDTO buscarVenta(@PathVariable Long ventaId) {
        return ventaService.buscarVenta(ventaId);
    }

    // GET http://localhost:8080/productos/{productoId}/ventas
    // ADMIN y VENDEDOR pueden consultar el historial de ventas de un producto.
    @GetMapping("/productos/{productoId}/ventas")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public List<VentaDTO> listarVentasPorProducto(@PathVariable Long productoId) {
        return ventaService.listarVentasPorProducto(productoId);
    }

    // GET http://localhost:8080/clientes/{clienteId}/ventas
    // ADMIN y VENDEDOR pueden consultar el historial de compras de un cliente.
    @GetMapping("/clientes/{clienteId}/ventas")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public List<VentaDTO> listarVentasPorCliente(@PathVariable Long clienteId) {
        return ventaService.listarVentasPorCliente(clienteId);
    }


    @GetMapping("/listar-producto")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
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
