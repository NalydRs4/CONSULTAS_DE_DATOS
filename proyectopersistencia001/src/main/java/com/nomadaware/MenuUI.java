package com.nomadaware;

// Importaciones necesarias para JPA
import jakarta.persistence.EntityManager;
import java.util.Scanner;

import com.nomadaware.metodos.GestionProductos;

import java.util.List;

public class MenuUI {

    // Escáner para leer la entrada del usuario desde la consola
    private static final Scanner scanner = new Scanner(System.in);

    // Método principal para iniciar la interfaz de usuario del menú
    public static void iniciar(EntityManager em) {
        boolean salir = false;
        while (!salir) {
            System.out.println("=============================");
            System.out.println("= MENU PRINCIPAL NOMADAWARE =");
            System.out.println("1. Menú de Productos");
            System.out.println("2. Menú de Clientes");
            System.out.println("3. Menú de Facturas");
            System.out.println("4. Agregar método de pago");
            System.out.println("5. Consultas");
            System.out.println("6. Salir del sistema");
            System.out.println("=============================");
            System.out.print("Elegir un menú: ");
            // Leer la opción del usuario y manejar errores de entrada
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        // Llamar al método para manejar el menú de productos, pasando el EntityManager
                        menuProductos(em);
                        break;
                    case 2:
                        // Llamar al método para manejar el menú de usuarios y clientes, pasando el
                        menuClientes(em);
                        break;
                    case 3:
                        // Llamar al método para manejar el menú de facturación, pasando el
                        menuFacturas(em);
                        break;
                    case 4:
                        // Llamar al método para manejar el menú de métodos de pago, pasando el EntityManager
                        menuMetodosPago(em);
                        break;
                    case 5:
                        // Llamar al método para manejar el menú de consultas, pasando el EntityManager
                        menuConsultas(em);
                        break;
                    case 6:
                        salir = true;
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:
                        System.out.println("Opción no reconocida.");
                }
            } catch (NumberFormatException e) { // Captura errores de formato
                System.out.println("Por favor, ingresa un número válido.");
            }
        }
    }
    
    // Método para manejar el menú de métodos de pago
    private static void menuMetodosPago(EntityManager em) {
        boolean volver = false;
        while (!volver) {
            System.out.println("------------------------------");
            System.out.println("--- GESTION DE METODOS DE PAGO ---");
            System.out.println("1. Crear método de pago");
            System.out.println("2. Leer métodos de pago");
            System.out.println("3. Eliminar método de pago");
            System.out.println("4. Volver al menú principal");
            System.out.println("------------------------------");
            System.out.print("Opción: ");
            // Leer la opción del usuario y manejar errores de entrada
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        // Se actualiza para pedir el nombre y la descripción
                        System.out.print("Ingresa el nombre del método de pago (ej. Efectivo, Tarjeta): ");
                        String nombre = scanner.nextLine();
                        System.out.print("Ingresa la descripción del método de pago: ");
                        String descripcion = scanner.nextLine();
                        // Llamada al método para crear el método de pago 
                        com.nomadaware.entities.MetodoPago mp = com.nomadaware.metodos.GestionMetodosPago.crearMetodoPago(em, nombre, descripcion);
                        System.out.println("¡Método de pago '" + mp.getNombre() + "' creado con ID " + mp.getId() + "!");
                        break;
                    case 2:
                        System.out.println("\n--- LISTA DE METODOS DE PAGO ---");
                        // Llamada al método para leer todos los métodos de pago y mostrarlos en la consola
                        List<com.nomadaware.entities.MetodoPago> metodos = com.nomadaware.metodos.GestionMetodosPago.leerMetodosPago(em);
                        if (metodos.isEmpty()) {
                            System.out.println("No hay métodos de pago registrados.");
                        } else {
                            for (com.nomadaware.entities.MetodoPago metodo : metodos) {
                                System.out.printf("ID: %d | Nombre: %s | Descripción: %s%n", metodo.getId(), metodo.getNombre(), metodo.getDescripcion());
                            }
                        }
                        break;
                    case 3:
                        System.out.print("Ingresa el ID del método de pago a eliminar: ");
                        // Leer el ID del método de pago a eliminar
                        int idEliminar = Integer.parseInt(scanner.nextLine());
                        boolean eliminado = com.nomadaware.metodos.GestionMetodosPago.eliminarMetodoPago(em, idEliminar);
                        System.out.println(eliminado ? "¡Método de pago eliminado!" : "No se encontró el método de pago.");
                        break;
                    case 4:
                        volver = true;
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    } 

    // Método para manejar el menú de clientes
    private static void menuClientes(EntityManager em) {
        boolean volver = false;
        while (!volver) {
            System.out.println("---------------------------");
            System.out.println("--- GESTION DE CLIENTES ---");
            System.out.println("1. Crear cliente");
            System.out.println("2. Leer clientes");
            System.out.println("3. Eliminar cliente");
            System.out.println("4. Actualizar cliente");
            System.out.println("5. Volver al menú principal");
            System.out.println("---------------------------");
            System.out.print("Opción: ");
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        System.out.print("Ingresa el nombre del cliente: ");
                        String nombre = scanner.nextLine();
                        System.out.print("Ingresa el apellido del cliente: ");
                        String apellido = scanner.nextLine();
                        System.out.print("Ingresa el correo del cliente: ");
                        String correo = scanner.nextLine();
                        System.out.print("Ingresa el teléfono del cliente: ");
                        String telefono = scanner.nextLine();
                        // Llamada al método para crear el cliente 
                        com.nomadaware.entities.Cliente c = com.nomadaware.metodos.GestionClientes.crearCliente(em, nombre, apellido, correo, telefono);
                        System.out.println("¡Cliente '" + c.getNombre() + " " + c.getApellido() + "' creado con éxito!");
                        break;
                    case 2:
                        System.out.println("\n--- LISTA DE CLIENTES ---");
                        // Llamada al método para leer todos los clientes y mostrarlos en la consola
                        List<com.nomadaware.entities.Cliente> clientes = com.nomadaware.metodos.GestionClientes.leerClientes(em);
                        if (clientes.isEmpty()) {
                            System.out.println("No hay clientes registrados.");
                        } else {
                            for (com.nomadaware.entities.Cliente c1 : clientes) {
                                System.out.printf("ID: %d | Nombre: %s %s | Correo: %s | Teléfono: %s%n", 
                                        c1.getId(), c1.getNombre(), c1.getApellido(), c1.getCorreo(), c1.getTelefono());
                            }
                        }
                        break;
                    case 3:
                        System.out.print("Ingresa el ID del cliente a eliminar: ");
                        // Leer el ID del cliente a eliminar
                        int clienteId = Integer.parseInt(scanner.nextLine());
                        boolean eliminado = com.nomadaware.metodos.GestionClientes.eliminarCliente(em, clienteId);
                        System.out.println(eliminado ? "¡Cliente eliminado!" : "No se encontró el cliente.");
                        break;
                    case 4:
                        System.out.print("Ingresa el ID del cliente a actualizar: ");
                        int idActualizar = Integer.parseInt(scanner.nextLine());
                        // Llamada al método para buscar el cliente por ID
                        com.nomadaware.entities.Cliente cli = com.nomadaware.metodos.GestionClientes.buscarCliente(em, idActualizar);
                        // Si el cliente existe, se solicitan los nuevos datos y se actualiza
                        if (cli != null) {
                            System.out.print("Ingresa el nuevo nombre (actual: " + cli.getNombre() + "): ");
                            cli.setNombre(scanner.nextLine());
                            System.out.print("Ingresa el nuevo apellido (actual: " + cli.getApellido() + "): ");
                            cli.setApellido(scanner.nextLine());
                            System.out.print("Ingresa el nuevo correo (actual: " + cli.getCorreo() + "): ");
                            cli.setCorreo(scanner.nextLine());
                            System.out.print("Ingresa el nuevo teléfono (actual: " + cli.getTelefono() + "): ");
                            cli.setTelefono(scanner.nextLine());
                            // Llamada al método para actualizar el cliente
                            com.nomadaware.metodos.GestionClientes.actualizarCliente(em, cli);
                            System.out.println("¡Cliente actualizado con éxito!");
                        } else {
                            System.out.println("No se encontró el cliente con ese ID.");
                        }
                        break;
                    case 5:
                        volver = true;
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // Método para manejar el menú de facturación
    private static void menuFacturas(EntityManager em) {
        boolean volver = false;
        while (!volver) {
            System.out.println("------------------------------");
            System.out.println("--- GESTION DE FACTURACION ---");
            System.out.println("1. Crear factura");
            System.out.println("2. Leer facturas");
            System.out.println("3. Eliminar factura");
            System.out.println("4. Volver al menú principal");
            System.out.println("------------------------------");
            System.out.print("Opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        System.out.print("Ingresa el ID del cliente: ");
                        int clienteId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Ingresa el ID del método de pago (tipo de pago): ");
                        int metodoPagoId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Ingresa el ID del producto que compra: ");
                        int productoId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Ingresa la cantidad comprada: ");
                        int cantidad = Integer.parseInt(scanner.nextLine());
                        // Llamada al método para crear la factura, pasando los IDs y la cantidad
                        com.nomadaware.entities.Factura f = com.nomadaware.metodos.GestionFacturas.crearFactura(em, java.time.LocalDate.now(), clienteId, metodoPagoId, productoId, cantidad);
                        System.out.println("¡Factura creada con ID " + f.getId() + " y detalle registrado con éxito!");
                        break;
                    case 2:
                        System.out.println("\n--- LISTA DE FACTURAS ---");
                        // Llamada al método para leer todas las facturas y mostrarlas en la consola
                        var facturas = com.nomadaware.metodos.GestionFacturas.leerFacturas(em);
                        if (facturas.isEmpty()) {
                            System.out.println("No hay facturas registradas.");
                        } else {
                            for (com.nomadaware.entities.Factura fact : facturas) {
                                // Evitar NullPointerException si el método de pago no está mapeado correctamente al leer
                                String metodoStr = (fact.getMetodoPago() != null) ? String.valueOf(fact.getMetodoPago().getId()) : "N/A";
                                System.out.printf("ID: %d | Fecha: %s | Total: $%.2f | Cliente: %s | Pago ID: %s%n",
                                        fact.getId(), fact.getFecha(), fact.getTotal(), fact.getCliente().getNombre(), metodoStr);
                            }
                        }
                        break;
                    case 3:
                        System.out.print("Ingresa el ID de la factura a eliminar: ");
                        // Leer el ID de la factura a eliminar
                        int idEliminar = Integer.parseInt(scanner.nextLine());
                        boolean eliminada = com.nomadaware.metodos.GestionFacturas.eliminarFactura(em, idEliminar);
                        System.out.println(eliminada ? "¡Factura eliminada!" : "No se encontró la factura.");
                        break;
                    case 4:
                        volver = true;
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void menuConsultas(EntityManager em) {
        boolean volver = false;
        while (!volver) {
            System.out.println("------------------------------");
            System.out.println("--- MENU DE CONSULTAS ---");
            System.out.println("1. Consultar productos por precio orden ASCENDENTE y paginación");
            System.out.println("2. Consultar productos por precio orden DESCENDENTE y paginación");
            System.out.println("3. Consultar facturas por cliente");
            System.out.println("4. Consultar facturas por rango de fechas");
            System.out.println("5. Volver al menú principal");
            System.out.println("------------------------------");
            System.out.print("Opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        // Llamada al método para consultar productos por precio mayor a un valor

                        break;
                    case 2:
                        // Llamada al método para consultar productos por categoría

                        break;
                    case 3:
                        // Llamada al método para consultar facturas por cliente

                        break;
                    case 4:
                        // Llamada al método para consultar facturas por rango de fechas

                        break;
                    case 5:
                        volver = true;
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // Método para manejar el menú de productos
    private static void menuProductos(EntityManager em) {
        boolean volver = false;
        while (!volver) {
            System.out.println("----------------------------");
            System.out.println("--- GESTION DE PRODUCTOS ---");
            System.out.println("1. Crear producto");
            System.out.println("2. Leer productos");
            System.out.println("3. Actualizar precio");
            System.out.println("4. Eliminar producto");
            System.out.println("5. Volver al menú principal");
            System.out.println("----------------------------");
            System.out.print("Opción: ");
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        System.out.print("Ingresa el nombre del producto: ");
                        String nombre = scanner.nextLine();
                        System.out.print("Ingresa la cantidad: ");
                        int cantidad = Integer.parseInt(scanner.nextLine());
                        System.out.print("Ingresa el precio: ");
                        java.math.BigDecimal precio = new java.math.BigDecimal(scanner.nextLine());
                        System.out.print("Ingresa la categoría del producto: ");
                        String cat = scanner.nextLine();
                        // Llamamos al método para crear el producto
                        com.nomadaware.entities.Producto p = com.nomadaware.metodos.GestionProductos.crearProducto(em, nombre, cantidad, precio, cat);
                        System.out.println("¡Producto '" + p.getNombre() + "' creado con ID " + p.getId() + "!");
                        break;
                    case 2:
                        System.out.println("\n--- LISTA DE PRODUCTOS ---");
                        // Llamamos al método para leer todos los productos 
                        var productos = com.nomadaware.metodos.GestionProductos.leerProductos(em);
                        if (productos.isEmpty()) {
                            System.out.println("No hay productos.");
                        } else {
                            for (com.nomadaware.entities.Producto prod : productos) {
                                String catDesc = (prod.getCategoria() != null) ? prod.getCategoria().getDescripcion()
                                        : "Sin Categoría";
                                System.out.printf("ID: %d | Nombre: %s | Precio: $%.2f | Categoría: %s%n", prod.getId(),
                                        prod.getNombre(), prod.getPrecio(), catDesc);
                            }
                        }
                        break;
                    case 3:
                        System.out.print("Ingresa el ID del producto: ");
                        // Leer el ID del producto a actualizar
                        int idActualizar = Integer.parseInt(scanner.nextLine());
                        System.out.print("Ingresa el nuevo precio: ");
                        // Leer el nuevo precio como BigDecimal para manejar valores monetarios
                        java.math.BigDecimal nuevoPrecio = new java.math.BigDecimal(scanner.nextLine());
                        // Llamamos al método para actualizar el precio del producto
                        boolean actualizado = com.nomadaware.metodos.GestionProductos.actualizarPrecio(em, idActualizar, nuevoPrecio);
                        System.out.println(actualizado ? "¡Precio actualizado!" : "No se encontró el producto.");
                        break;
                    case 4:
                        System.out.print("Ingresa el ID del producto a eliminar: ");
                        // Leer el ID del producto a eliminar
                        int idEliminar = Integer.parseInt(scanner.nextLine());
                        // Llamamos al método para eliminar el producto
                        boolean eliminado = com.nomadaware.metodos.GestionProductos.eliminarProducto(em, idEliminar);
                        System.out.println(eliminado ? "¡Producto eliminado!" : "No se encontró el producto.");
                        break;
                    case 5:
                        volver = true;
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}