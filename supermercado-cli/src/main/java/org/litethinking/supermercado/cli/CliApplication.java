package org.litethinking.supermercado.cli;

import org.litethinking.supermercado.cli.service.CliService;
import org.litethinking.supermercado.shareddto.supermercado.CajeroDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;

/**
 * Main class for the CLI application.
 */
@SpringBootApplication
@Slf4j
public class CliApplication implements CommandLineRunner {

    private final CliService cliService;

    public CliApplication(CliService cliService) {
        this.cliService = cliService;
    }

    /**
     * Metodo principá pa arrancal la aplicasión
     * Ete e el punto de entrada del programa
     */
    public static void main(String[] args) {
        SpringApplication.run(CliApplication.class, args);
    }

    /**
     * Ete metodo se ejecuta cuando arranca la aplicasión
     * Maneja toa la logica del menú principá
     */
    @Override
    public void run(String... args) {
        log.info("Iniciando la aplicasión CLI del supelmercao");
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        // Variable pa guardal el cajero que ta logueao
        CajeroDto cajeroLogueado = null;

        while (!exit) {
            printMainMenu(cajeroLogueado);
            int option = getOption(scanner);

            switch (option) {
                case 1:
                    log.debug("Usualio seleccionó la opsión de getión de producto");
                    cliService.productMenu(scanner);
                    break;
                case 2:
                    log.debug("Usualio seleccionó la opsión de getión de inventalio");
                    cliService.inventoryMenu(scanner);
                    break;
                case 3:
                    log.debug("Usualio seleccionó la opsión de getión de venta");
                    if (cajeroLogueado == null) {
                        log.info("Cajero no ta logueao, pidiendo login primero");
                        cajeroLogueado = cliService.cajeroLogin(scanner);
                        if (cajeroLogueado != null) {
                            log.info("Login exitoso del cajero: {}", cajeroLogueado.nombre());
                            cliService.saleMenu(scanner, cajeroLogueado);
                        } else {
                            log.warn("El login del cajero falló");
                        }
                    } else {
                        log.debug("Cajero ya ta logueao, abriendo menú de venta");
                        cliService.saleMenu(scanner, cajeroLogueado);
                    }
                    break;
                case 4:
                    log.debug("Usualio seleccionó la opsión de generar reporte");
                    cliService.generateReport();
                    break;
                case 5:
                    log.debug("Usualio seleccionó la opsión de getión de cajero");
                    if (cajeroLogueado == null) {
                        log.info("Intentando login de cajero");
                        cajeroLogueado = cliService.cajeroLogin(scanner);
                        if (cajeroLogueado != null) {
                            log.info("Login exitoso del cajero: {}", cajeroLogueado.nombre());
                            System.out.println("Inicio de sesión exitoso como: " + cajeroLogueado.nombre());
                        }
                    } else {
                        log.debug("Cajero ya ta logueao: {}", cajeroLogueado.nombre());
                        System.out.println("Ya ha iniciado sesión como: " + cajeroLogueado.nombre());
                        System.out.print("¿Desea cerrar sesión? (s/n): ");
                        String respuesta = scanner.nextLine();
                        if (respuesta.equalsIgnoreCase("s")) {
                            log.info("Cerrando sesión del cajero: {}", cajeroLogueado.nombre());
                            cajeroLogueado = null;
                            System.out.println("Sesión cerrada exitosamente.");
                        }
                    }
                    break;
                case 0:
                    log.info("Usualio seleccionó salil de la aplicasión");
                    exit = true;
                    System.out.println("Saliendo de la aplicación. ¡Hasta pronto!");
                    break;
                default:
                    log.warn("Usualio ingresó una opsión inválida: {}", option);
                    System.out.println("Opción inválida. Por favor intente de nuevo.");
            }
        }
        scanner.close();
    }

    /**
     * Imprime el menú principá de la aplicasión
     * Muestra diferente opsione según si hay un cajero logueao o no
     */
    private void printMainMenu(CajeroDto cajero) {
        log.debug("Mostrando el menú principá");
        System.out.println("\n===== SISTEMA DE GESTIÓN DE SUPERMERCADO =====");
        if (cajero != null) {
            log.debug("Mostrando info del cajero logueao: {}", cajero.nombre());
            System.out.println("Cajero: " + cajero.nombre() + " (Código: " + cajero.codigo() + ")");
        }
        System.out.println("1. Gestión de Productos");
        System.out.println("2. Gestión de Inventario");
        System.out.println("3. Gestión de Ventas");
        System.out.println("4. Generar Reporte");
        System.out.println("5. " + (cajero == null ? "Iniciar Sesión como Cajero" : "Gestionar Sesión de Cajero, ejemplo use code CAJ003 para prueba"));
        System.out.println("0. Salir");
        System.out.print("Ingrese su opción: ");
    }

    /**
     * Obtiene la opsión que el usualio seleccionó
     * Si el usualio no ingresa un número, devuelve -1
     */
    private int getOption(Scanner scanner) {
        try {
            String input = scanner.nextLine();
            log.debug("Usualio ingresó: {}", input);
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            log.warn("Usualio ingresó algo que no e un número");
            return -1;
        }
    }
}
