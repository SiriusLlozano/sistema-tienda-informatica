package com.example.tienda_computadoras;

import com.example.tienda_computadoras.model.Categoria;
import com.example.tienda_computadoras.model.Cliente;
import com.example.tienda_computadoras.model.DetalleVenta;
import com.example.tienda_computadoras.model.Producto;
import com.example.tienda_computadoras.model.Venta;
import com.example.tienda_computadoras.repository.ClienteRepository;
import com.example.tienda_computadoras.repository.ProductoRepository;
import com.example.tienda_computadoras.repository.VentaRepository;
import com.example.tienda_computadoras.services.ProductoService;
import com.example.tienda_computadoras.services.VentaService;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TiendaComputadorasApplicationTests {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private VentaRepository ventaRepository;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private VentaService ventaService;

	@Test
	void contextLoads() {
	}

	@Test
	void mapsTwentyBusinessEntities() {
		Assertions.assertEquals(20, entityManager.getMetamodel().getEntities().size());
	}

	@Test
	void savesSaleWithMultipleProductDetails() {
		Categoria categoria = new Categoria("Componentes", null);
		entityManager.persist(categoria);

		Producto producto = new Producto("Memoria RAM", categoria, "DDR5");
		producto.setPrecio(new BigDecimal("85.50"));
		productoRepository.saveAndFlush(producto);

		Cliente cliente = new Cliente();
		cliente.setNombre("Camila");
		clienteRepository.saveAndFlush(cliente);

		Venta venta = new Venta();
		venta.setCliente(cliente);
		venta.setFecha(LocalDate.now());

		DetalleVenta detalle = new DetalleVenta();
		detalle.setProducto(producto);
		detalle.setCantidad(2);
		detalle.setPrecioUnitario(producto.getPrecio());
		venta.agregarDetalle(detalle);

		Venta ventaGuardada = ventaRepository.saveAndFlush(venta);
		entityManager.clear();

		Venta ventaRecuperada = ventaRepository.findById(ventaGuardada.getId()).orElseThrow();
		Assertions.assertEquals("Camila", ventaRecuperada.getCliente().getNombre());
		Assertions.assertEquals(1, ventaRecuperada.getDetalles().size());
		Assertions.assertEquals(2, ventaRecuperada.getDetalles().get(0).getCantidad());
		Assertions.assertEquals(new BigDecimal("85.50"),
				ventaRecuperada.getDetalles().get(0).getPrecioUnitario());
	}

	@Test
	void keepsExistingProductAndSaleServiceFlow() {
		productoService.registrarProducto("Laptop", "Computadoras", "Modelo X", new BigDecimal("1200.00"));
		Producto producto = productoRepository.findAll().stream()
				.filter(item -> item.getNombre().equals("Laptop"))
				.findFirst()
				.orElseThrow();

		String respuesta = ventaService.registrarVenta("Pedro", 1, producto.getId());

		Assertions.assertTrue(respuesta.contains("Venta registrada"));
		Venta venta = ventaRepository.findAll().stream()
				.filter(item -> item.getCliente().getNombre().equals("Pedro"))
				.findFirst()
				.orElseThrow();
		Assertions.assertEquals(1, venta.getDetalles().size());
		Assertions.assertEquals(new BigDecimal("1200.00"),
				venta.getDetalles().get(0).getPrecioUnitario());
	}
}
