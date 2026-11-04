package com.example.tienda_computadoras;

import com.example.tienda_computadoras.model.Categoria;
import com.example.tienda_computadoras.model.Cliente;
import com.example.tienda_computadoras.model.DetalleVenta;
import com.example.tienda_computadoras.model.Producto;
import com.example.tienda_computadoras.model.Venta;
import com.example.tienda_computadoras.dto.request.CrearClienteDTO;
import com.example.tienda_computadoras.dto.response.ClienteDTO;
import com.example.tienda_computadoras.repository.ClienteRepository;
import com.example.tienda_computadoras.repository.ProductoRepository;
import com.example.tienda_computadoras.repository.VentaRepository;
import com.example.tienda_computadoras.services.ProductoService;
import com.example.tienda_computadoras.services.ClienteService;
import com.example.tienda_computadoras.services.VentaService;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

	@Autowired
	private ClienteService clienteService;

	@Test
	void contextLoads() {
	}

	@Test
	void mapsTwentyBusinessEntities() {
		Assertions.assertEquals(20, entityManager.getMetamodel().getEntities().size());
	}

	@Test
	void allowsDifferentClientNameWhenEmailAndPhoneAreUnique() {
		ClienteDTO cliente = clienteService.registrarCliente(
				new CrearClienteDTO("Pedro", "pedro@example.com", "999123456"));

		Assertions.assertNotNull(cliente.id());
		Assertions.assertEquals("Pedro", cliente.nombre());
	}

	@Test
	void rejectsDuplicateEmailIgnoringCase() {
		clienteService.registrarCliente(new CrearClienteDTO("Pedro", "pedro@example.com", "999123456"));

		ResponseStatusException exception = Assertions.assertThrows(
				ResponseStatusException.class,
				() -> clienteService.registrarCliente(
						new CrearClienteDTO("Pedro Nuevo", "PEDRO@example.com", "999654321")));

		Assertions.assertEquals(409, exception.getStatusCode().value());
	}

	@Test
	void rejectsDuplicatePhoneEvenWhenClientNameChanges() {
		clienteService.registrarCliente(new CrearClienteDTO("Pedro", "pedro@example.com", "999123456"));

		ResponseStatusException exception = Assertions.assertThrows(
				ResponseStatusException.class,
				() -> clienteService.registrarCliente(
						new CrearClienteDTO("Pedro Nuevo", "nuevo@example.com", "999123456")));

		Assertions.assertEquals(409, exception.getStatusCode().value());
	}

	@Test
	void listsRegisteredClientsAsDtos() {
		clienteService.registrarCliente(new CrearClienteDTO("Pedro", "pedro@example.com", "999123456"));
		clienteService.registrarCliente(new CrearClienteDTO("Ana", "ana@example.com", "999654321"));

		var clientes = clienteService.listarClientes();

		Assertions.assertEquals(2, clientes.size());
		Assertions.assertTrue(clientes.stream().anyMatch(cliente -> cliente.nombre().equals("Pedro")));
		Assertions.assertTrue(clientes.stream().anyMatch(cliente -> cliente.nombre().equals("Ana")));
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

	@Test
	void listsSaleHistoryByProductAndClient() {
		Categoria categoria = new Categoria("Computadoras", null);
		entityManager.persist(categoria);

		Producto producto = new Producto("Laptop", categoria, "Modelo X");
		producto.setPrecio(new BigDecimal("1200.00"));
		productoRepository.saveAndFlush(producto);

		Cliente cliente = new Cliente();
		cliente.setNombre("Pedro");
		clienteRepository.saveAndFlush(cliente);

		Venta venta = new Venta();
		venta.setCliente(cliente);
		venta.setFecha(LocalDate.now());

		DetalleVenta detalle = new DetalleVenta();
		detalle.setProducto(producto);
		detalle.setCantidad(1);
		detalle.setPrecioUnitario(producto.getPrecio());
		venta.agregarDetalle(detalle);
		ventaRepository.saveAndFlush(venta);

		Assertions.assertEquals(1, ventaService.listarVentasPorProducto(producto.getId()).size());
		Assertions.assertEquals(1, ventaService.listarVentasPorCliente(cliente.getId()).size());
	}

	@Test
	void returnsNotFoundForMissingSaleHistoryOwner() {
		ResponseStatusException exception = Assertions.assertThrows(
				ResponseStatusException.class,
				() -> ventaService.listarVentasPorProducto(999L));

		Assertions.assertEquals(404, exception.getStatusCode().value());
	}
}
