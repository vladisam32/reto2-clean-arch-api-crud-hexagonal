package org.litethinking.supermercado.cli.service.supermercado;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.litethinking.supermercado.cli.service.CliService;
import org.litethinking.supermercado.cli.service.report.ReportGenerator;
import org.litethinking.supermercado.shareddto.supermercado.CajeroDto;
import org.litethinking.supermercado.shareddto.supermercado.ProductoDto;
import org.litethinking.supermercado.shareddto.supermercado.inventario.InventarioDto;
import org.litethinking.supermercado.shareddto.supermercado.venta.ItemVentaDto;
import org.litethinking.supermercado.shareddto.supermercado.venta.VentaDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Implementación de la interfaz CliService.
 * Esta clase usa la API REST pa' realizar operaciones en el sistema del supermercado.
 * Maneja toas' las funcionalidades del CLI como login de cajero, gestión de productos,
 * inventario, ventas y reportes.
 * 
 * @author LiteThinking
 * @version 1.0
 */
@Service
public class CliServiceImpl implements CliService {

    private static final Logger logger = LoggerFactory.getLogger(CliServiceImpl.class);
    private final RestTemplate restTemplate;
    private final String apiBaseUrl;
    private final ReportGenerator reportGenerator;
    private CajeroDto cajeroLogueado;

    /**
     * Constructor del servicio CLI.
     * Inicializa los componentes necesarios pa' comunicarse con la API REST.
     * 
     * @param restTemplate Cliente pa' hacer peticiones HTTP a la API
     * @param apiBaseUrl URL base de la API REST
     * @param reportGenerator Generador de reportes
     */
    public CliServiceImpl(RestTemplate restTemplate, 
                         @Value("${api.base-url}") String apiBaseUrl,
                         ReportGenerator reportGenerator) {
        this.restTemplate = restTemplate;
        this.apiBaseUrl = apiBaseUrl;
        this.reportGenerator = reportGenerator;
        this.cajeroLogueado = null;
        logger.info("¡Servicio CLI inicializado y listo pa' trabajar con la API en: {}!", apiBaseUrl);
        logger.debug("Componentes inicializados: RestTemplate, ReportGenerator, URL base: {}", apiBaseUrl);
    }

    /**
     * Método pa' que el cajero inicie sesión en el sistema.
     * Pide el código del cajero y verifica si existe en la base de datos.
     * 
     * @param scanner Scanner pa' leer la entrada del usuario
     * @return CajeroDto con la información del cajero si el login es exitoso, null si no
     */
    @Override
    public CajeroDto cajeroLogin(Scanner scanner) {
        logger.info("Iniciando proceso de login de cajero, ejemplo use CAJ003");
        System.out.println("\n===== CAJERO LOGIN =====");
        System.out.print("Ingrese su código de cajero, ejemplo use CAJ003: ");
        String codigo = scanner.nextLine();
        logger.debug("Código de cajero ingresado: {}", codigo);

        try {
            logger.debug("Enviando petición a la API: {}/cajeros/codigo/{}", apiBaseUrl, codigo);
            ResponseEntity<CajeroDto> response = restTemplate.getForEntity(
                    apiBaseUrl + "/cajeros/codigo/" + codigo,
                    CajeroDto.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                CajeroDto cajero = response.getBody();
                logger.info("¡Login exitoso! Cajero: {} (ID: {})", cajero.nombre(), cajero.id());
                System.out.println("¡Bienvenido, " + cajero.nombre() + "!");
                this.cajeroLogueado = cajero;
                return cajero;
            } else {
                logger.warn("Respuesta inválida del servidor al intentar login con código: {}", codigo);
                System.out.println("Código de cajero no válido.");
                return null;
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.warn("Código de cajero no encontrado: {}", codigo);
                System.out.println("Código de cajero no encontrado.");
            } else {
                logger.error("Error HTTP al iniciar sesión con código {}: {}", codigo, e.getMessage());
                System.out.println("Error al iniciar sesión: " + e.getMessage());
            }
            return null;
        } catch (Exception e) {
            logger.error("¡Diablo! Error inesperado al iniciar sesión: {}", e.getMessage(), e);
            System.out.println("Error al iniciar sesión: " + e.getMessage());
            return null;
        }
    }

    /**
     * Menú pa' la gestión de productos.
     * Permite listar, buscar, agregar, actualizar y eliminar productos.
     * 
     * @param scanner Scanner pa' leer la entrada del usuario
     */
    @Override
    public void productMenu(Scanner scanner) {
        logger.info("Entrando al menú de gestión de productos");
        boolean exit = false;
        while (!exit) {
            System.out.println("\n===== GESTIÓN DE PRODUCTOS =====");
            System.out.println("1. Listar todos los productos");
            System.out.println("2. Buscar producto por ID");
            System.out.println("3. Agregar nuevo producto");
            System.out.println("4. Actualizar producto");
            System.out.println("5. Eliminar producto");
            System.out.println("0. Volver al menú principal");
            System.out.print("Ingrese su opción: ");

            int option = getOption(scanner);
            logger.debug("Opción seleccionada en menú de productos: {}", option);

            switch (option) {
                case 1:
                    logger.info("Usuario seleccionó: Listar todos los productos");
                    listAllProducts();
                    break;
                case 2:
                    logger.info("Usuario seleccionó: Buscar producto por ID");
                    findProductById(scanner);
                    break;
                case 3:
                    logger.info("Usuario seleccionó: Agregar nuevo producto");
                    addNewProduct(scanner);
                    break;
                case 4:
                    logger.info("Usuario seleccionó: Actualizar producto");
                    updateProduct(scanner);
                    break;
                case 5:
                    logger.info("Usuario seleccionó: Eliminar producto");
                    deleteProduct(scanner);
                    break;
                case 0:
                    logger.info("Usuario seleccionó: Volver al menú principal");
                    exit = true;
                    break;
                default:
                    logger.warn("Opción inválida seleccionada: {}", option);
                    System.out.println("Opción inválida. Por favor intente de nuevo.");
            }
        }
        logger.debug("Saliendo del menú de gestión de productos");
    }

    /**
     * Menú pa' la gestión del inventario.
     * Permite listar todos los items del inventario, buscar por ID de producto
     * y actualizar la cantidad de un producto en el inventario.
     * 
     * @param scanner Scanner pa' leer la entrada del usuario
     */
    @Override
    public void inventoryMenu(Scanner scanner) {
        logger.info("Entrando al menú de gestión de inventario");
        boolean exit = false;
        while (!exit) {
            System.out.println("\n===== GESTIÓN DE INVENTARIO =====");
            System.out.println("1. Listar todo el inventario");
            System.out.println("2. Buscar inventario por ID de producto");
            System.out.println("3. Actualizar cantidad en inventario");
            System.out.println("0. Volver al menú principal");
            System.out.print("Ingrese su opción: ");

            int option = getOption(scanner);
            logger.debug("Opción seleccionada en menú de inventario: {}", option);

            switch (option) {
                case 1:
                    logger.info("Usuario seleccionó: Listar todo el inventario");
                    listAllInventory();
                    break;
                case 2:
                    logger.info("Usuario seleccionó: Buscar inventario por ID de producto");
                    findInventoryByProductId(scanner);
                    break;
                case 3:
                    logger.info("Usuario seleccionó: Actualizar cantidad en inventario");
                    updateInventoryQuantity(scanner);
                    break;
                case 0:
                    logger.info("Usuario seleccionó: Volver al menú principal");
                    exit = true;
                    break;
                default:
                    logger.warn("Opción inválida seleccionada: {}", option);
                    System.out.println("Opción inválida. Por favor intente de nuevo.");
            }
        }
        logger.debug("Saliendo del menú de gestión de inventario");
    }

    /**
     * Menú pa' la gestión de ventas.
     * Permite listar todas las ventas, buscar ventas por ID y crear nuevas ventas.
     * Solo accesible pa' cajeros autenticados.
     * 
     * @param scanner Scanner pa' leer la entrada del usuario
     * @param cajero El cajero que está logueado en el sistema
     */
    @Override
    public void saleMenu(Scanner scanner, CajeroDto cajero) {
        logger.info("Intentando acceder al menú de ventas");
        if (cajero == null) {
            logger.warn("Intento de acceso al menú de ventas sin cajero autenticado");
            System.out.println("Debe iniciar sesión como cajero para acceder a este menú.");
            return;
        }

        logger.info("Entrando al menú de gestión de ventas con cajero: {} (ID: {})", cajero.nombre(), cajero.id());
        boolean exit = false;
        while (!exit) {
            System.out.println("\n===== GESTIÓN DE VENTAS =====");
            System.out.println("Cajero: " + cajero.nombre() + " (Código: " + cajero.codigo() + ")");
            System.out.println("1. Listar todas las ventas");
            System.out.println("2. Buscar venta por ID");
            System.out.println("3. Crear nueva venta");
            System.out.println("0. Volver al menú principal");
            System.out.print("Ingrese su opción: ");

            int option = getOption(scanner);
            logger.debug("Opción seleccionada en menú de ventas: {}", option);

            switch (option) {
                case 1:
                    logger.info("Usuario seleccionó: Listar todas las ventas");
                    listAllSales();
                    break;
                case 2:
                    logger.info("Usuario seleccionó: Buscar venta por ID");
                    findSaleById(scanner);
                    break;
                case 3:
                    logger.info("Usuario seleccionó: Crear nueva venta");
                    createNewSale(scanner, cajero);
                    break;
                case 0:
                    logger.info("Usuario seleccionó: Volver al menú principal");
                    exit = true;
                    break;
                default:
                    logger.warn("Opción inválida seleccionada: {}", option);
                    System.out.println("Opción inválida. Por favor intente de nuevo.");
            }
        }
        logger.debug("Saliendo del menú de gestión de ventas");
    }

    /**
     * Genera un reporte del sistema usando el ReportGenerator.
     * Este método delega la generación del reporte al componente especializado.
     */
    @Override
    public void generateReport() {
        logger.info("¡Vamo' a generar un reporte del sistema!");
        try {
            reportGenerator.generateReport();
            logger.info("¡Reporte generado con éxito!");
        } catch (Exception e) {
            logger.error("¡Diablo! Hubo un problema generando el reporte: {}", e.getMessage(), e);
            System.out.println("Error al generar el reporte: " + e.getMessage());
        }
    }

    // Métodos auxiliares pa' la gestión de productos
    /**
     * Lista todos los productos disponibles en el sistema.
     * Hace una petición GET a la API y muestra los productos en formato de tabla.
     */
    private void listAllProducts() {
        logger.info("Obteniendo lista de todos los productos");
        try {
            logger.debug("Enviando petición a la API: {}/productos", apiBaseUrl);
            ResponseEntity<List<ProductoDto>> response = restTemplate.exchange(
                    apiBaseUrl + "/productos",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ProductoDto>>() {}
            );
            List<ProductoDto> productos = response.getBody();

            if (productos != null && !productos.isEmpty()) {
                logger.info("¡Encontramo' {} productos en total!", productos.size());

                System.out.println("\nLista de Productos:");
                System.out.printf("%-5s | %-20s | %-15s | %-10s | %-15s%n", 
                        "ID", "Nombre", "Precio", "Categoría", "Código Barras");
                System.out.println("---------------------------------------------------------------------");

                for (ProductoDto producto : productos) {
                    logger.debug("Producto: ID={}, Nombre={}, Precio=${}", 
                        producto.id(), producto.nombre(), producto.precio());

                    System.out.printf("%-5d | %-20s | $%-14.2f | %-10s | %-15s%n", 
                            producto.id(), 
                            producto.nombre(), 
                            producto.precio(), 
                            producto.categoria(), 
                            producto.codigoBarras());
                }
            } else {
                logger.warn("No se encontraron productos en el sistema");
                System.out.println("No se encontraron productos.");
            }
        } catch (Exception e) {
            logger.error("¡Diablo! Error al obtener la lista de productos: {}", e.getMessage(), e);
            System.out.println("Error al obtener los productos: " + e.getMessage());
        }
    }

    /**
     * Busca un producto por su ID.
     * Hace una petición GET a la API y muestra los detalles del producto.
     * 
     * @param scanner Scanner pa' leer la entrada del usuario
     */
    private void findProductById(Scanner scanner) {
        System.out.print("Ingrese el ID del producto: ");
        try {
            String input = scanner.nextLine();
            logger.debug("ID de producto ingresado: {}", input);
            Long id = Long.parseLong(input);

            logger.info("Buscando producto con ID: {}", id);
            logger.debug("Enviando petición a la API: {}/productos/{}", apiBaseUrl, id);

            ResponseEntity<ProductoDto> response = restTemplate.getForEntity(
                    apiBaseUrl + "/productos/" + id,
                    ProductoDto.class
            );

            ProductoDto producto = response.getBody();
            if (producto != null) {
                logger.info("¡Encontramo' el producto! ID: {}, Nombre: {}", id, producto.nombre());

                System.out.println("\nDetalles del Producto:");
                System.out.println("ID: " + producto.id());
                System.out.println("Nombre: " + producto.nombre());
                System.out.println("Descripción: " + producto.descripcion());
                System.out.println("Precio: $" + producto.precio());
                System.out.println("Categoría: " + producto.categoria());
                System.out.println("Código de Barras: " + producto.codigoBarras());

                logger.debug("Detalles completos del producto: {}", producto);
            } else {
                logger.warn("No se encontró ningún producto con ID: {}", id);
                System.out.println("No se encontró ningún producto con ese ID.");
            }
        } catch (NumberFormatException e) {
            logger.error("Formato de ID inválido: {}", e.getMessage());
            System.out.println("Formato de ID inválido. Por favor ingrese un número.");
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.warn("Producto no encontrado en la API");
                System.out.println("Producto no encontrado.");
            } else {
                logger.error("Error HTTP al buscar el producto: {}", e.getMessage());
                System.out.println("Error al buscar el producto: " + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("¡Diablo! Error inesperado al buscar el producto: {}", e.getMessage(), e);
            System.out.println("Error al buscar el producto: " + e.getMessage());
        }
    }

    /**
     * Agrega un nuevo producto al sistema.
     * Solicita los datos del producto al usuario y hace una petición POST a la API.
     * 
     * @param scanner Scanner pa' leer la entrada del usuario
     */
    private void addNewProduct(Scanner scanner) {
        logger.info("Iniciando proceso de agregar un nuevo producto");
        try {
            System.out.println("\nIngrese los Detalles del Producto:");

            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            logger.debug("Nombre ingresado: {}", nombre);

            System.out.print("Descripción: ");
            String descripcion = scanner.nextLine();
            logger.debug("Descripción ingresada: {}", descripcion);

            System.out.print("Precio: ");
            String precioStr = scanner.nextLine();
            logger.debug("Precio ingresado: {}", precioStr);
            BigDecimal precio = new BigDecimal(precioStr);

            System.out.print("Categoría: ");
            String categoria = scanner.nextLine();
            logger.debug("Categoría ingresada: {}", categoria);

            System.out.print("Código de Barras: ");
            String codigoBarras = scanner.nextLine();
            logger.debug("Código de Barras ingresado: {}", codigoBarras);

            ProductoDto nuevoProducto = new ProductoDto(
                    null, // El ID será asignado por el servidor
                    nombre,
                    descripcion,
                    precio,
                    categoria,
                    codigoBarras
            );

            logger.info("Enviando nuevo producto a la API: {}", nombre);
            logger.debug("Detalles del producto a crear: {}", nuevoProducto);

            ResponseEntity<ProductoDto> response = restTemplate.postForEntity(
                    apiBaseUrl + "/productos",
                    nuevoProducto,
                    ProductoDto.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                Long nuevoId = response.getBody().id();
                logger.info("¡Producto agregado con éxito! ID: {}", nuevoId);
                System.out.println("¡Producto agregado con éxito! ID: " + nuevoId);
            } else {
                logger.warn("Respuesta inesperada del servidor: {}", response.getStatusCode());
                System.out.println("Error al agregar el producto. Respuesta del servidor: " + response.getStatusCode());
            }
        } catch (NumberFormatException e) {
            logger.error("Formato de precio inválido: {}", e.getMessage());
            System.out.println("Formato de precio inválido. Por favor ingrese un número válido.");
        } catch (Exception e) {
            logger.error("¡Diablo! Error al agregar el producto: {}", e.getMessage(), e);
            System.out.println("Error al agregar el producto: " + e.getMessage());
        }
    }

    /**
     * Actualiza un producto existente en el sistema.
     * Primero obtiene el producto actual, luego solicita los nuevos datos al usuario
     * y finalmente hace una petición PUT a la API.
     * 
     * @param scanner Scanner pa' leer la entrada del usuario
     */
    private void updateProduct(Scanner scanner) {
        logger.info("Iniciando proceso de actualización de producto");
        try {
            System.out.print("Ingrese el ID del producto a actualizar: ");
            String input = scanner.nextLine();
            logger.debug("ID de producto ingresado: {}", input);
            Long id = Long.parseLong(input);

            logger.info("Buscando producto con ID: {} pa' actualizar", id);
            // Primero, obtenemos el producto actual
            ResponseEntity<ProductoDto> getResponse = restTemplate.getForEntity(
                    apiBaseUrl + "/productos/" + id,
                    ProductoDto.class
            );

            if (!getResponse.getStatusCode().is2xxSuccessful() || getResponse.getBody() == null) {
                logger.warn("No se encontró ningún producto con ID: {} pa' actualizar", id);
                System.out.println("Producto no encontrado con ID: " + id);
                return;
            }

            ProductoDto existingProduct = getResponse.getBody();
            logger.info("Producto encontrado: {}", existingProduct.nombre());
            logger.debug("Detalles actuales del producto: {}", existingProduct);

            System.out.println("\nActualizar Detalles del Producto (presione Enter para mantener el valor actual):");

            System.out.print("Nombre [" + existingProduct.nombre() + "]: ");
            String nombre = scanner.nextLine();
            if (nombre.isEmpty()) nombre = existingProduct.nombre();
            logger.debug("Nuevo nombre (o actual si no cambió): {}", nombre);

            System.out.print("Descripción [" + existingProduct.descripcion() + "]: ");
            String descripcion = scanner.nextLine();
            if (descripcion.isEmpty()) descripcion = existingProduct.descripcion();
            logger.debug("Nueva descripción (o actual si no cambió): {}", descripcion);

            System.out.print("Precio [" + existingProduct.precio() + "]: ");
            String precioStr = scanner.nextLine();
            BigDecimal precio = precioStr.isEmpty() ? existingProduct.precio() : new BigDecimal(precioStr);
            logger.debug("Nuevo precio (o actual si no cambió): {}", precio);

            System.out.print("Categoría [" + existingProduct.categoria() + "]: ");
            String categoria = scanner.nextLine();
            if (categoria.isEmpty()) categoria = existingProduct.categoria();
            logger.debug("Nueva categoría (o actual si no cambió): {}", categoria);

            System.out.print("Código de Barras [" + existingProduct.codigoBarras() + "]: ");
            String codigoBarras = scanner.nextLine();
            if (codigoBarras.isEmpty()) codigoBarras = existingProduct.codigoBarras();
            logger.debug("Nuevo código de barras (o actual si no cambió): {}", codigoBarras);

            ProductoDto updatedProduct = new ProductoDto(
                    id,
                    nombre,
                    descripcion,
                    precio,
                    categoria,
                    codigoBarras
            );

            logger.info("Enviando producto actualizado a la API: {}", nombre);
            logger.debug("Detalles del producto actualizado: {}", updatedProduct);

            restTemplate.put(
                    apiBaseUrl + "/productos/" + id,
                    updatedProduct
            );

            logger.info("¡Producto actualizado con éxito! ID: {}", id);
            System.out.println("¡Producto actualizado con éxito!");
        } catch (NumberFormatException e) {
            logger.error("Formato de ID o precio inválido: {}", e.getMessage());
            System.out.println("Formato de ID o precio inválido. Por favor ingrese números válidos.");
        } catch (HttpClientErrorException e) {
            logger.error("Error HTTP al actualizar el producto: {}", e.getMessage());
            System.out.println("Error al actualizar el producto: " + e.getMessage());
        } catch (Exception e) {
            logger.error("¡Diablo! Error inesperado al actualizar el producto: {}", e.getMessage(), e);
            System.out.println("Error al actualizar el producto: " + e.getMessage());
        }
    }

    /**
     * Elimina un producto del sistema.
     * Solicita confirmación al usuario antes de eliminar y hace una petición DELETE a la API.
     * 
     * @param scanner Scanner pa' leer la entrada del usuario
     */
    private void deleteProduct(Scanner scanner) {
        logger.info("Iniciando proceso de eliminación de producto");
        try {
            System.out.print("Ingrese el ID del producto a eliminar: ");
            String input = scanner.nextLine();
            logger.debug("ID de producto ingresado: {}", input);
            Long id = Long.parseLong(input);

            // Primero verificamos si el producto existe
            try {
                logger.debug("Verificando si el producto existe antes de eliminar");
                ResponseEntity<ProductoDto> checkResponse = restTemplate.getForEntity(
                        apiBaseUrl + "/productos/" + id,
                        ProductoDto.class
                );

                if (checkResponse.getBody() != null) {
                    ProductoDto producto = checkResponse.getBody();
                    logger.info("Producto encontrado para eliminar: {} (ID: {})", producto.nombre(), id);

                    System.out.println("\nDetalles del producto a eliminar:");
                    System.out.println("ID: " + producto.id());
                    System.out.println("Name: " + producto.nombre());
                    System.out.println("Preccio: $" + producto.precio());
                    System.out.println("Categ: " + producto.categoria());
                }
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    logger.warn("Producto no encontrado con ID: {}", id);
                    System.out.println("Producto no encontrado con ID: " + id);
                    return;
                }
            }

            System.out.print("¿Está segur que desea eliminar este prodo? (s/n): ");
            String confirmation = scanner.nextLine();
            logger.debug("Confirmación de eliminacción: {}", confirmation);

            if (confirmation.equalsIgnoreCase("s")) {
                logger.info("Eliminando producto con ID: {}", id);
                restTemplate.delete(apiBaseUrl + "/productos/" + id);
                logger.info("¡Producto eliminado con éxito!");
                System.out.println("¡Producto eliminado con éxito!");
            } else {
                logger.info("Eliminación cancelada por el usuario");
                System.out.println("Eliminación cancelada.");
            }
        } catch (NumberFormatException e) {
            logger.error("Format de ID inválido: {}", e.getMessage());
            System.out.println("Formato de ID inválido. Por favor ingrese un número.");
        } catch (Exception e) {
            logger.error("¡Diablo! Error inesperado al eliminar el producto: {}", e.getMessage(), e);
            System.out.println("Error al eliminar el producto: " + e.getMessage());
        }
    }

    // Métodos auxiliares pa' la gestión de inventario
    /**
     * Lista todos los items del inventario disponibles en el sistema.
     * Hace una petición GET a la API y muestra los items en formato de tabla.
     */
    private void listAllInventory() {
        logger.info("Obteniendo lista de todo el inventario");
        try {
            logger.debug("Enviando Request a la API: {}/inventario", apiBaseUrl);
            ResponseEntity<List<InventarioDto>> response = restTemplate.exchange(
                    apiBaseUrl + "/inventario",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<InventarioDto>>() {}
            );
            List<InventarioDto> inventarios = response.getBody();

            if (inventarios != null && !inventarios.isEmpty()) {
                logger.info("¡Encontramo' {} items de inventario en total!", inventarios.size());

                // Contar productos con stock bajo
                long stockBajo = inventarios.stream()
                    .filter(inv -> inv.cantidad() < inv.stockMinimo())
                    .count();

                if (stockBajo > 0) {
                    logger.warn("¡Ojo! Hay {} productos con stock por debajo del mínimo", stockBajo);
                }

                System.out.println("\nLista de Inventario:");
                System.out.printf("%-5s | %-20s | %-10s | %-15s%n", 
                        "ID", "Producto", "Cantidad", "Stock Mínimo");
                System.out.println("--------------------------------------------------");

                for (InventarioDto inventario : inventarios) {
                    logger.debug("Inventario: Producto={}, Cantidad={}, Stock Mínimo={}", 
                        inventario.producto().nombre(), inventario.cantidad(), inventario.stockMinimo());

                    // Destacar visualmente los productos con stock bajo
                    boolean stockBajoFlag = inventario.cantidad() < inventario.stockMinimo();

                    if (stockBajoFlag) {
                        logger.debug("¡Alerta! Stock bajo para producto: {}", inventario.producto().nombre());
                    }

                    System.out.printf("%-5d | %-20s | %-10d | %-15d %s%n", 
                            inventario.id(), 
                            inventario.producto().nombre(), 
                            inventario.cantidad(), 
                            inventario.stockMinimo(),
                            stockBajoFlag ? "⚠️ STOCK BAJO" : "");
                }
            } else {
                logger.warn("No se encontraron items de inventario en el sistema");
                System.out.println("No se encontraron items de inventario.");
            }
        } catch (Exception e) {
            logger.error("¡Diablo! Error al obtener la lista de inventario: {}", e.getMessage(), e);
            System.out.println("Error al obtener el inventario: " + e.getMessage());
        }
    }

    /**
     * Busca el inventario de un producto por su ID.
     * Primero verifica si el producto existe y luego busca su inventario.
     * 
     * @param scanner Scanner pa' leer la entrada del usuario
     */
    private void findInventoryByProductId(Scanner scanner) {
        logger.info("Iniciando búsqueda de inventario por ID de producto");
        System.out.print("Ingrese el ID del producto: ");
        try {
            String input = scanner.nextLine();
            logger.debug("ID de producto ingresado: {}", input);
            Long productoId = Long.parseLong(input);

            logger.info("Buscando inventario para producto con ID: {}", productoId);

            // Primero verificamos si el producto existe
            try {
                logger.debug("Verificando si el producto existe: {}/productos/{}", apiBaseUrl, productoId);
                ResponseEntity<ProductoDto> productoResponse = restTemplate.getForEntity(
                        apiBaseUrl + "/productos/" + productoId,
                        ProductoDto.class
                );

                if (!productoResponse.getStatusCode().is2xxSuccessful() || productoResponse.getBody() == null) {
                    logger.warn("Producto no encontrado con ID: {}", productoId);
                    System.out.println("Producto no encontrado con ID: " + productoId);
                    return;
                }

                ProductoDto producto = productoResponse.getBody();
                logger.info("Producto encontrado: {} (ID: {})", producto.nombre(), productoId);

                // Ahora buscamos el inventario para este producto
                logger.debug("Buscando inventario para el producto: {}/inventario/producto/{}", apiBaseUrl, productoId);
                ResponseEntity<InventarioDto> response = restTemplate.getForEntity(
                        apiBaseUrl + "/inventario/producto/" + productoId,
                        InventarioDto.class
                );

                InventarioDto inventario = response.getBody();
                if (inventario != null) {
                    logger.info("¡Encontramo' el inventario pa'l producto {}!", producto.nombre());
                    logger.debug("Detalles del inventario: ID={}, Cantidad={}, Stock Mínimo={}", 
                        inventario.id(), inventario.cantidad(), inventario.stockMinimo());

                    System.out.println("\nDetalles del Inventario:");
                    System.out.println("ID: " + inventario.id());
                    System.out.println("Producto: " + inventario.producto().nombre());
                    System.out.println("Cantidad: " + inventario.cantidad());
                    System.out.println("Stock Mínimo: " + inventario.stockMinimo());
                    System.out.println("Stock Máximo: " + inventario.stockMaximo());
                    System.out.println("Última Reposición: " + inventario.fechaUltimaReposicion());
                    System.out.println("Ubicación: " + inventario.ubicacion());

                    // Mostrar alerta si el stock está por debajo del mínimo
                    if (inventario.cantidad() < inventario.stockMinimo()) {
                        logger.warn("¡ALERTA! Stock bajo para producto {}: {} unidades (mínimo: {})", 
                            producto.nombre(), inventario.cantidad(), inventario.stockMinimo());
                        System.out.println("\n¡ALERTA! El stock está por debajo del mínimo requerido.");
                    }
                } else {
                    logger.warn("No se encontró inventario para el producto: {} (ID: {})", producto.nombre(), productoId);
                    System.out.println("No se encontró inventario para el producto con ID: " + productoId);
                }
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    logger.warn("No se encontró inventario para el producto con ID: {}", productoId);
                    System.out.println("No se encontró inventario para el producto con ID: " + productoId);
                } else {
                    logger.error("Error HTTP al buscar inventario: {}", e.getMessage());
                    throw e;
                }
            }
        } catch (NumberFormatException e) {
            logger.error("Formato de ID inválido: {}", e.getMessage());
            System.out.println("Formato de ID inválido. Por favor ingrese un número.");
        } catch (Exception e) {
            logger.error("¡Diablo! Error inesperado al buscar el inventario: {}", e.getMessage(), e);
            System.out.println("Error al buscar el inventario: " + e.getMessage());
        }
    }

    /**
     * Actualiza la cantidad de un producto en el inventario.
     * Si el producto no tiene inventario, permite crear uno nuevo.
     * 
     * @param scanner Scanner pa' leer la entrada del usuario
     */
    private void updateInventoryQuantity(Scanner scanner) {
        logger.info("Iniciando proceso de actualización de inventario");
        System.out.print("Ingrese el ID del producto: ");
        try {
            String input = scanner.nextLine();
            logger.debug("ID de producto ingresado: {}", input);
            Long productoId = Long.parseLong(input);

            logger.info("Buscando producto con ID: {} pa' actualizar su inventario", productoId);
            // Primero verificamos si el producto existe
            ResponseEntity<ProductoDto> productoResponse = restTemplate.getForEntity(
                    apiBaseUrl + "/productos/" + productoId,
                    ProductoDto.class
            );

            if (!productoResponse.getStatusCode().is2xxSuccessful() || productoResponse.getBody() == null) {
                logger.warn("Producto no encontrado con ID: {}", productoId);
                System.out.println("Producto no encontrado con ID: " + productoId);
                return;
            }

            ProductoDto producto = productoResponse.getBody();
            logger.info("Producto encontrado: {} (ID: {})", producto.nombre(), productoId);

            // Ahora buscamos el inventario para este producto
            ResponseEntity<InventarioDto> inventarioResponse;
            try {
                logger.debug("Buscando inventario para el producto: {}/inventarios/producto/{}", apiBaseUrl, productoId);
                inventarioResponse = restTemplate.getForEntity(
                        apiBaseUrl + "/inventario/producto/" + productoId,
                        InventarioDto.class
                );
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    logger.warn("No existe inventario para el producto: {}", producto.nombre());
                    System.out.println("No existe inventario para el producto: " + producto.nombre());
                    System.out.print("¿Desea crear un nuevo registro de inventario? (s/n): ");
                    String respuesta = scanner.nextLine();
                    logger.debug("Respuesta del usuario para crear nuevo inventario: {}", respuesta);

                    if (!respuesta.equalsIgnoreCase("s")) {
                        logger.info("Usuario decidió no crear nuevo inventario");
                        return;
                    }

                    logger.info("Creando nuevo inventario para producto: {}", producto.nombre());
                    // Crear nuevo inventario
                    System.out.print("Cantidad: ");
                    String cantidadStr = scanner.nextLine();
                    logger.debug("Cantidad ingresada: {}", cantidadStr);
                    Integer cantidad = Integer.parseInt(cantidadStr);

                    System.out.print("Stock Mínimo: ");
                    String stockMinimoStr = scanner.nextLine();
                    logger.debug("Stock mínimo ingresado: {}", stockMinimoStr);
                    Integer stockMinimo = Integer.parseInt(stockMinimoStr);

                    System.out.print("Stock Máximo: ");
                    String stockMaximoStr = scanner.nextLine();
                    logger.debug("Stock máximo ingresado: {}", stockMaximoStr);
                    Integer stockMaximo = Integer.parseInt(stockMaximoStr);

                    System.out.print("Ubicación: ");
                    String ubicacion = scanner.nextLine();
                    logger.debug("Ubicación ingresada: {}", ubicacion);

                    InventarioDto nuevoInventario = new InventarioDto(
                            null, // El ID será asignado por el servidor
                            producto,
                            cantidad,
                            stockMinimo,
                            stockMaximo,
                            LocalDate.now(),
                            ubicacion
                    );

                    logger.info("Enviando nuevo inventario a la API");
                    logger.debug("Detalles del nuevo inventario: {}", nuevoInventario);

                    ResponseEntity<InventarioDto> createResponse = restTemplate.postForEntity(
                            apiBaseUrl + "/inventario",
                            nuevoInventario,
                            InventarioDto.class
                    );

                    if (createResponse.getStatusCode().is2xxSuccessful()) {
                        Long nuevoId = createResponse.getBody().id();
                        logger.info("¡Inventario creado con éxito! ID: {}", nuevoId);
                        System.out.println("¡Inventario creado exitosamente con ID: " + nuevoId + "!");

                        // Verificar si el stock está por debajo del mínimo
                        if (cantidad < stockMinimo) {
                            logger.warn("¡ALERTA! Stock inicial está por debajo del mínimo: {} < {}", 
                                cantidad, stockMinimo);
                            System.out.println("\n¡ALERTA! El stock está por debajo del mínimo requerido.");
                        }
                    } else {
                        logger.warn("Respuesta inesperada del servidor: {}", createResponse.getStatusCode());
                        System.out.println("Error al crear el inventario. Respuesta del servidor: " + createResponse.getStatusCode());
                    }
                    return;
                } else {
                    logger.error("Error HTTP al buscar inventario: {}", e.getMessage());
                    throw e;
                }
            }

            InventarioDto inventario = inventarioResponse.getBody();
            if (inventario == null) {
                logger.error("Error al obtener el inventario, respuesta vacía del servidor");
                System.out.println("Error al obtener el inventario.");
                return;
            }

            logger.info("Inventario encontrado para producto: {}", producto.nombre());
            logger.debug("Detalles actuales del inventario: ID={}, Cantidad={}, Stock Mínimo={}", 
                inventario.id(), inventario.cantidad(), inventario.stockMinimo());

            System.out.println("\nDetalles actuales del Inventario:");
            System.out.println("Producto: " + inventario.producto().nombre());
            System.out.println("Cantidad actual: " + inventario.cantidad());
            System.out.println("Stock Mínimo: " + inventario.stockMinimo());
            System.out.println("Stock Máximo: " + inventario.stockMaximo());

            System.out.print("\nNueva cantidad: ");
            String nuevaCantidadStr = scanner.nextLine();
            logger.debug("Nueva cantidad ingresada: {}", nuevaCantidadStr);
            Integer nuevaCantidad = Integer.parseInt(nuevaCantidadStr);

            // Actualizar solo la cantidad, manteniendo los demás valores
            InventarioDto inventarioActualizado = new InventarioDto(
                    inventario.id(),
                    inventario.producto(),
                    nuevaCantidad,
                    inventario.stockMinimo(),
                    inventario.stockMaximo(),
                    LocalDate.now(), // Actualizar la fecha de reposición
                    inventario.ubicacion()
            );

            logger.info("Actualizando inventario con nueva cantidad: {} (anterior: {})", 
                nuevaCantidad, inventario.cantidad());
            logger.debug("Enviando inventario actualizado a la API: {}/inventarios/{}", 
                apiBaseUrl, inventario.id());

            restTemplate.put(
                    apiBaseUrl + "/inventario/" + inventario.id(),
                    inventarioActualizado
            );

            logger.info("¡Inventario actualizado con éxito! ID: {}", inventario.id());
            System.out.println("¡Inventario actualizado exitosamente!");

            // Mostrar alerta si el stock está por debajo del mínimo
            if (nuevaCantidad < inventario.stockMinimo()) {
                logger.warn("¡ALERTA! Nueva cantidad está por debajo del mínimo: {} < {}", 
                    nuevaCantidad, inventario.stockMinimo());
                System.out.println("\n¡ALERTA! El stock está por debajo del mínimo requerido.");
            }
        } catch (NumberFormatException e) {
            logger.error("Formato de número inválido: {}", e.getMessage());
            System.out.println("Formato de número inválido. Por favor ingrese un número válido.");
        } catch (HttpClientErrorException e) {
            logger.error("Error HTTP al actualizar el inventario: {}", e.getMessage());
            System.out.println("Error al actualizar el inventario: " + e.getMessage());
        } catch (Exception e) {
            logger.error("¡Diablo! Error inesperado al actualizar el inventario: {}", e.getMessage(), e);
            System.out.println("Error al actualizar el inventario: " + e.getMessage());
        }
    }

    // Helper methods for sales management
    private void listAllSales() {
        logger.info("¡Vamo' a buscar toas' las ventas que tenemo'!");
        try {
            ResponseEntity<List<VentaDto>> response = restTemplate.exchange(
                    apiBaseUrl + "/ventas",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<VentaDto>>() {}
            );
            List<VentaDto> ventas = response.getBody();

            if (ventas != null && !ventas.isEmpty()) {
                logger.info("¡Encontramo' {} ventas en total!", ventas.size());

                // Calcular el total vendido para mostrar en el log
                double totalVendido = ventas.stream()
                    .mapToDouble(v -> v.montoTotal().doubleValue())
                    .sum();
                logger.info("Total vendido: ${}", totalVendido);

                System.out.println("\nLista de Ventas:");
                System.out.printf("%-5s | %-20s | %-15s | %-10s%n", 
                        "ID", "Fecha", "Cliente", "Total");
                System.out.println("--------------------------------------------------");

                for (VentaDto venta : ventas) {
                    System.out.printf("%-5d | %-20s | %-15s | $%-9.2f%n", 
                            venta.id(), 
                            venta.fechaVenta(), 
                            venta.nombreCliente(), 
                            venta.montoTotal());

                    // Loguear detalles de cada venta a nivel de debug
                    logger.debug("Venta ID: {}, Cliente: {}, Total: ${}, Fecha: {}", 
                        venta.id(), venta.nombreCliente(), venta.montoTotal(), venta.fechaVenta());
                }
            } else {
                logger.warn("¡No encontramo' ninguna venta, ta' to' vacío!");
                System.out.println("No se encontraron ventas.");
            }
        } catch (Exception e) {
            logger.error("¡Diablo! Hubo un problema buscando las ventas: {}", e.getMessage(), e);
            System.out.println("Error al recuperar las ventas: " + e.getMessage());
        }
    }

    private void findSaleById(Scanner scanner) {
        System.out.print("Ingrese el ID de la venta: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            logger.info("¡Vamo' a buscar la venta con ID: {}!", id);

            ResponseEntity<VentaDto> response = restTemplate.getForEntity(
                    apiBaseUrl + "/ventas/" + id,
                    VentaDto.class
            );

            VentaDto venta = response.getBody();
            if (venta != null) {
                logger.info("¡Encontramo' la venta! ID: {}, Cliente: {}, Total: ${}", 
                    id, venta.nombreCliente(), venta.montoTotal());

                System.out.println("\nDetalles de la Venta:");
                System.out.println("ID: " + venta.id());
                System.out.println("Fecha: " + venta.fechaVenta());
                System.out.println("Cliente: " + venta.nombreCliente());
                System.out.println("Total: $" + venta.montoTotal());
                System.out.println("Método de Pago: " + venta.metodoPago());

                if (venta.items() != null && !venta.items().isEmpty()) {
                    logger.debug("La venta tiene {} productos", venta.items().size());

                    System.out.println("\nItems:");
                    System.out.printf("%-5s | %-20s | %-10s | %-10s | %-10s%n", 
                            "ID", "Producto", "Cantidad", "Precio", "Subtotal");
                    System.out.println("------------------------------------------------------------------");

                    for (ItemVentaDto item : venta.items()) {
                        // Loguear detalles de cada item a nivel de debug
                        logger.debug("Item: Producto={}, Cantidad={}, Subtotal=${}", 
                            item.producto().nombre(), item.cantidad(), item.subtotal());

                        System.out.printf("%-5d | %-20s | %-10d | $%-9.2f | $%-9.2f%n", 
                                item.id(), 
                                item.producto().nombre(), 
                                item.cantidad(), 
                                item.precioUnitario(), 
                                item.subtotal());
                    }
                }
            } else {
                logger.warn("No encontramo' ninguna venta con ID: {}", id);
                System.out.println("Venta no encontrada con ID: " + id);
            }
        } catch (NumberFormatException e) {
            logger.error("¡Diablo! El ID que pusieron no es un número: {}", e.getMessage());
            System.out.println("Formato de ID inválido. Por favor ingrese un número.");
        } catch (Exception e) {
            logger.error("¡Rayos! Hubo un problema buscando la venta con ID: {}", scanner.nextLine(), e);
            System.out.println("Error al buscar la venta: " + e.getMessage());
        }
    }

    private void createNewSale(Scanner scanner, CajeroDto cajero) {
        logger.info("¡Vamo' a crear una venta nueva con el cajero: {}!", cajero.nombre());
        try {
            System.out.println("\nCrear Nueva Venta:");

            System.out.print("Nombre del Cliente: ");
            String nombreCliente = scanner.nextLine();
            logger.info("Cliente pa' la venta: {}", nombreCliente);

            List<ItemVentaDto> items = new ArrayList<>();
            boolean agregarMasItems = true;
            BigDecimal montoTotal = BigDecimal.ZERO;

            while (agregarMasItems) {
                System.out.print("\nIngrese el ID del producto: ");
                Long productoId = Long.parseLong(scanner.nextLine());
                logger.debug("Buscando producto con ID: {} pa' la venta", productoId);

                ResponseEntity<ProductoDto> productoResponse = restTemplate.getForEntity(
                        apiBaseUrl + "/productos/" + productoId,
                        ProductoDto.class
                );

                if (!productoResponse.getStatusCode().is2xxSuccessful() || productoResponse.getBody() == null) {
                    logger.warn("¡No encontramo' el producto con ID: {}!", productoId);
                    System.out.println("Producto no encontrado con ID: " + productoId);
                    continue;
                }

                ProductoDto producto = productoResponse.getBody();
                logger.debug("Producto encontrado: {}, Precio: ${}", producto.nombre(), producto.precio());

                System.out.print("Cantidad: ");
                Integer cantidad = Integer.parseInt(scanner.nextLine());

                BigDecimal precioUnitario = producto.precio();
                BigDecimal subtotal = precioUnitario.multiply(new BigDecimal(cantidad));
                montoTotal = montoTotal.add(subtotal);

                logger.debug("Agregando a la venta: {} x {} = ${}", 
                    producto.nombre(), cantidad, subtotal);

                ItemVentaDto item = new ItemVentaDto(
                        null, // ID will be assigned by the server
                        producto,
                        cantidad,
                        precioUnitario,
                        subtotal
                );

                items.add(item);

                System.out.print("¿Desea agregar otro producto? (s/n): ");
                String respuesta = scanner.nextLine();
                agregarMasItems = respuesta.equalsIgnoreCase("s");
            }

            System.out.print("Método de Pago (efectivo/tarjeta): ");
            String metodoPago = scanner.nextLine();
            logger.debug("Método de pago: {}", metodoPago);

            logger.info("¡Preparando venta con {} productos, total: ${}!", 
                items.size(), montoTotal);

            VentaDto nuevaVenta = new VentaDto(
                    null, // ID will be assigned by the server
                    LocalDateTime.now(),
                    nombreCliente,
                    items,
                    montoTotal,
                    metodoPago
            );

            logger.info("¡Enviando la venta al servidor pa' guardarla!");
            ResponseEntity<VentaDto> response = restTemplate.postForEntity(
                    apiBaseUrl + "/ventas",
                    nuevaVenta,
                    VentaDto.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                Long ventaId = response.getBody().id();
                logger.info("¡Venta creá con éxito! ID: {}, Cliente: {}, Total: ${}", 
                    ventaId, nombreCliente, montoTotal);
                System.out.println("Venta creada exitosamente con ID: " + ventaId);
            }
        } catch (NumberFormatException e) {
            logger.error("¡Diablo! Número inválido: {}", e.getMessage());
            System.out.println("Formato de número inválido. Por favor ingrese un número válido.");
        } catch (Exception e) {
            logger.error("¡Qué lío! Error creando la venta: {}", e.getMessage(), e);
            System.out.println("Error al crear la venta: " + e.getMessage());
        }
    }

    /**
     * Método auxiliar pa' obtener una opción numérica del usuario.
     * Convierte la entrada del usuario a un número entero.
     * Si la entrada no es un número válido, retorna -1.
     * 
     * @param scanner Scanner pa' leer la entrada del usuario
     * @return La opción seleccionada como número entero, o -1 si es inválida
     */
    private int getOption(Scanner scanner) {
        try {
            String input = scanner.nextLine();
            logger.debug("Entrada del usuario para opción: '{}'", input);
            int option = Integer.parseInt(input);
            return option;
        } catch (NumberFormatException e) {
            logger.debug("Entrada inválida, no es un número: retornando -1");
            return -1;
        }
    }
}
