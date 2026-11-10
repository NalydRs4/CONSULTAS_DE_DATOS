package com.nomadaware;

import com.nomadaware.entities.Categoria;
import com.nomadaware.entities.Cliente;
import com.nomadaware.entities.DetalleFactura;
import com.nomadaware.entities.Factura;
import com.nomadaware.entities.MetodoPago;
import com.nomadaware.entities.Producto;
import com.nomadaware.metodos.GestionCategorias;
import com.nomadaware.metodos.GestionClientes;
import com.nomadaware.metodos.GestionFacturas;
import com.nomadaware.metodos.GestionMetodosPago;
import com.nomadaware.metodos.GestionProductos;
import jakarta.persistence.EntityManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class InterfazGUI extends JFrame {

    private final EntityManager em;
    private final DefaultTableModel modeloProductos;
    private final DefaultTableModel modeloCategorias;
    private final DefaultTableModel modeloClientes;
    private final DefaultTableModel modeloFacturas;
    private final DefaultTableModel modeloDetalles;
    private final DefaultTableModel modeloMetodosPago;

    public InterfazGUI(EntityManager em, CountDownLatch latch) {
        this.em = em;
        setTitle("Sistema de Gestión - Nomadaware");
        setSize(860, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                latch.countDown();
            }
        });

        JTabbedPane pestanas = new JTabbedPane();

        modeloProductos = crearModeloTabla(new String[] { "ID", "Nombre", "Cantidad", "Precio", "Categoría" });
        modeloCategorias = crearModeloTabla(new String[] { "ID", "Descripción" });
        modeloClientes = crearModeloTabla(new String[] { "ID", "Nombre", "Apellido", "Correo", "Teléfono" });
        modeloFacturas = crearModeloTabla(new String[] { "ID", "Fecha", "Total", "Cliente", "ID Pago" });
        modeloDetalles = crearModeloTabla(
                new String[] { "ID Detalle", "ID Factura", "Producto", "Cantidad", "Precio Unit.", "Subtotal" });
        modeloMetodosPago = crearModeloTabla(new String[] { "ID", "Nombre", "Descripción" });

        pestanas.addTab("Productos", crearPanelProductos());
        pestanas.addTab("Categorías", crearPanelCategorias());
        pestanas.addTab("Clientes", crearPanelClientes());
        pestanas.addTab("Facturas", crearPanelFacturas());
        pestanas.addTab("Detalles de Factura", crearPanelDetalles());
        pestanas.addTab("Métodos de Pago", crearPanelMetodosPago());

        add(pestanas);

        actualizarTablaProductos();
        actualizarTablaCategorias();
        actualizarTablaClientes();
        actualizarTablaFacturas();
        actualizarTablaDetalles();
        actualizarTablaMetodosPago();
    }

    public static void iniciar(EntityManager em) {
        CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> new InterfazGUI(em, latch).setVisible(true));
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private DefaultTableModel crearModeloTabla(String[] columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    // ==================== PESTAÑA PRODUCTOS ====================
    private JPanel crearPanelProductos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(new JTable(modeloProductos)), BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton btnCrear = new JButton("Crear Producto");
        JButton btnPrecio = new JButton("Actualizar Precio");
        JButton btnEliminar = new JButton("Eliminar Producto");
        JButton btnFiltrar = new JButton("Consultas (JPQL)");
        JButton btnVerTodos = new JButton("Ver Todos");

        btnCrear.addActionListener(e -> {
            JTextField txtNombre = new JTextField();
            JTextField txtCantidad = new JTextField();
            JTextField txtPrecio = new JTextField();
            JTextField txtCategoria = new JTextField();
            Object[] campos = {
                    "Nombre:", txtNombre,
                    "Cantidad:", txtCantidad,
                    "Precio:", txtPrecio,
                    "Categoría:", txtCategoria
            };
            if (JOptionPane.showConfirmDialog(this, campos, "Nuevo Producto",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    GestionProductos.crearProducto(
                            em,
                            txtNombre.getText().trim(),
                            Integer.parseInt(txtCantidad.getText().trim()),
                            new BigDecimal(txtPrecio.getText().trim()),
                            txtCategoria.getText().trim());
                    actualizarTablaProductos();
                    actualizarTablaCategorias();
                } catch (Exception ex) {
                    mostrarError(ex.getMessage());
                }
            }
        });

        btnPrecio.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "ID del producto a actualizar:");
            if (idStr != null && !idStr.isBlank()) {
                String precioStr = JOptionPane.showInputDialog(this, "Nuevo precio:");
                if (precioStr != null && !precioStr.isBlank()) {
                    try {
                        boolean ok = GestionProductos.actualizarPrecio(em, Integer.parseInt(idStr.trim()),
                                new BigDecimal(precioStr.trim()));
                        if (!ok)
                            mostrarError("No se encontró el producto.");
                        actualizarTablaProductos();
                    } catch (Exception ex) {
                        mostrarError(ex.getMessage());
                    }
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "ID del producto a eliminar:");
            if (idStr != null && !idStr.isBlank()) {
                try {
                    boolean ok = GestionProductos.eliminarProducto(em, Integer.parseInt(idStr.trim()));
                    if (!ok)
                        mostrarError("No se encontró el producto.");
                    actualizarTablaProductos();
                } catch (Exception ex) {
                    mostrarError(ex.getMessage());
                }
            }
        });

        btnFiltrar.addActionListener(e -> {
            String[] opciones = {
                    "Consulta 1: Precio MAYOR a MENOR",
                    "Consulta 2: Precio MENOR a MAYOR",
                    "Consulta 3: Filtrar por Categoría"
            };

            JComboBox<String> comboFiltro = new JComboBox<>(opciones);
            JTextField txtPagina = new JTextField("1");
            JTextField txtTamano = new JTextField("5");
            comboFiltro.addActionListener(o -> {
                if (comboFiltro.getSelectedIndex() == 2) {
                    JOptionPane pane = (JOptionPane) SwingUtilities.getAncestorOfClass(JOptionPane.class, comboFiltro);
                    if (pane != null) {
                        pane.setValue(JOptionPane.OK_OPTION);
                    }
                }
            });

            Object[] campos = {
                    "Tipo de consulta JPQL:", comboFiltro,
                    "Número de página (1, 2...):", txtPagina,
                    "Número de elementos (1, 2...):", txtTamano,
            };

            if (JOptionPane.showConfirmDialog(this, campos, "Filtrar Productos (JPQL)",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    int pagina = Math.max(1, Integer.parseInt(txtPagina.getText().trim()));
                    int tamano = Math.max(1, Integer.parseInt(txtTamano.getText().trim()));

                    List<Producto> resultado = null;
                    if (comboFiltro.getSelectedIndex() == 0) {
                        resultado = GestionProductos.buscarPorPrecioDescendente(em, pagina, tamano);
                    } else if (comboFiltro.getSelectedIndex() == 1) {
                        resultado = GestionProductos.buscarPorPrecioAscendente(em, pagina, tamano);
                    } else if (comboFiltro.getSelectedIndex() == 2) {
                        String nombreCategoria = JOptionPane.showInputDialog(this, "Nombre de la categoría:");
                        if (nombreCategoria != null && !nombreCategoria.isBlank()) {
                            resultado = GestionProductos.buscarPorCategoria(em, nombreCategoria.trim(), pagina, tamano);
                        } else {
                            mostrarError("Debe ingresar un nombre de categoría.");
                            return;
                        }
                    }
                    cargarProductosEnTabla(resultado);
                } catch (Exception ex) {
                    mostrarError(ex.getMessage());
                }
            }
        });

        btnVerTodos.addActionListener(e -> actualizarTablaProductos());

        botones.add(btnCrear);
        botones.add(btnPrecio);
        botones.add(btnEliminar);
        botones.add(btnFiltrar);
        botones.add(btnVerTodos);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private void actualizarTablaProductos() {
        cargarProductosEnTabla(GestionProductos.leerProductos(em));
    }

    private void cargarProductosEnTabla(List<Producto> productos) {
        modeloProductos.setRowCount(0);
        for (Producto p : productos) {
            String cat = (p.getCategoria() != null) ? p.getCategoria().getDescripcion() : "Sin Categoría";
            modeloProductos.addRow(new Object[] { p.getId(), p.getNombre(), p.getCantidad(), p.getPrecio(), cat });
        }
    }

    // ==================== PESTAÑA CATEGORÍAS ====================
    private JPanel crearPanelCategorias() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(new JTable(modeloCategorias)), BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton btnCrear = new JButton("Crear Categoría");
        JButton btnActualizar = new JButton("Actualizar Categoría");
        JButton btnEliminar = new JButton("Eliminar Categoría");

        btnCrear.addActionListener(e -> {
            String desc = JOptionPane.showInputDialog(this, "Descripción de la categoría:");
            if (desc != null && !desc.isBlank()) {
                try {
                    GestionCategorias.crearCategoria(em, desc.trim());
                    actualizarTablaCategorias();
                } catch (Exception ex) {
                    mostrarError(ex.getMessage());
                }
            }
        });

        btnActualizar.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "ID de la categoría a actualizar:");
            if (idStr != null && !idStr.isBlank()) {
                try {
                    Categoria cat = GestionCategorias.buscarCategoria(em, Integer.parseInt(idStr.trim()));
                    if (cat == null) {
                        mostrarError("No se encontró la categoría.");
                        return;
                    }
                    String nuevaDesc = JOptionPane.showInputDialog(this, "Nueva descripción:", cat.getDescripcion());
                    if (nuevaDesc != null && !nuevaDesc.isBlank()) {
                        cat.setDescripcion(nuevaDesc.trim());
                        GestionCategorias.actualizarCategoria(em, cat);
                        actualizarTablaCategorias();
                        actualizarTablaProductos();
                    }
                } catch (Exception ex) {
                    mostrarError(ex.getMessage());
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "ID de la categoría a eliminar:");
            if (idStr != null && !idStr.isBlank()) {
                try {
                    boolean ok = GestionCategorias.eliminarCategoria(em, Integer.parseInt(idStr.trim()));
                    if (!ok)
                        mostrarError("No se encontró la categoría.");
                    actualizarTablaCategorias();
                } catch (Exception ex) {
                    mostrarError("No se puede eliminar si tiene productos asociados. Detalle: " + ex.getMessage());
                }
            }
        });

        botones.add(btnCrear);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private void actualizarTablaCategorias() {
        modeloCategorias.setRowCount(0);
        List<Categoria> categorias = GestionCategorias.leerCategorias(em);
        for (Categoria c : categorias) {
            modeloCategorias.addRow(new Object[] { c.getId(), c.getDescripcion() });
        }
    }

    // ==================== PESTAÑA CLIENTES ====================
    private JPanel crearPanelClientes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(new JTable(modeloClientes)), BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton btnCrear = new JButton("Crear Cliente");
        JButton btnActualizar = new JButton("Actualizar Cliente");
        JButton btnEliminar = new JButton("Eliminar Cliente");
        JButton btnFiltrar = new JButton("Consultas (JPQL)");
        JButton btnVerTodos = new JButton("Ver Todos");

        btnCrear.addActionListener(e -> {
            JTextField txtNombre = new JTextField();
            JTextField txtApellido = new JTextField();
            JTextField txtCorreo = new JTextField();
            JTextField txtTelefono = new JTextField();
            Object[] campos = {
                    "Nombre:", txtNombre,
                    "Apellido:", txtApellido,
                    "Correo:", txtCorreo,
                    "Teléfono:", txtTelefono
            };
            if (JOptionPane.showConfirmDialog(this, campos, "Nuevo Cliente",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    GestionClientes.crearCliente(
                            em,
                            txtNombre.getText().trim(),
                            txtApellido.getText().trim(),
                            txtCorreo.getText().trim(),
                            txtTelefono.getText().trim());
                    actualizarTablaClientes();
                } catch (Exception ex) {
                    mostrarError(ex.getMessage());
                }
            }
        });

        btnActualizar.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "ID del cliente a actualizar:");
            if (idStr != null && !idStr.isBlank()) {
                try {
                    Cliente cli = GestionClientes.buscarCliente(em, Integer.parseInt(idStr.trim()));
                    if (cli == null) {
                        mostrarError("No se encontró el cliente.");
                        return;
                    }
                    JTextField txtNombre = new JTextField(cli.getNombre());
                    JTextField txtApellido = new JTextField(cli.getApellido());
                    JTextField txtCorreo = new JTextField(cli.getCorreo());
                    JTextField txtTelefono = new JTextField(cli.getTelefono());
                    Object[] campos = {
                            "Nombre:", txtNombre,
                            "Apellido:", txtApellido,
                            "Correo:", txtCorreo,
                            "Teléfono:", txtTelefono
                    };
                    if (JOptionPane.showConfirmDialog(this, campos, "Editar Cliente",
                            JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                        cli.setNombre(txtNombre.getText().trim());
                        cli.setApellido(txtApellido.getText().trim());
                        cli.setCorreo(txtCorreo.getText().trim());
                        cli.setTelefono(txtTelefono.getText().trim());
                        GestionClientes.actualizarCliente(em, cli);
                        actualizarTablaClientes();
                    }
                } catch (Exception ex) {
                    mostrarError(ex.getMessage());
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "ID del cliente a eliminar:");
            if (idStr != null && !idStr.isBlank()) {
                try {
                    boolean ok = GestionClientes.eliminarCliente(em, Integer.parseInt(idStr.trim()));
                    if (!ok)
                        mostrarError("No se encontró el cliente.");
                    actualizarTablaClientes();
                } catch (Exception ex) {
                    mostrarError(ex.getMessage());
                }
            }
        });

        btnFiltrar.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Apellido del cliente a buscar:");
            if (nombre != null && !nombre.isBlank()) {
                try {
                    List<Cliente> resultado = GestionClientes.buscarClientesPorApellido(em, nombre.trim());
                    modeloClientes.setRowCount(0);
                    for (Cliente c : resultado) {
                        modeloClientes.addRow(new Object[] { c.getId(), c.getNombre(), c.getApellido(), c.getCorreo(),
                                c.getTelefono() });
                    }
                } catch (Exception ex) {
                    mostrarError(ex.getMessage());
                }
            }
        });

        btnVerTodos.addActionListener(e -> actualizarTablaClientes());

        botones.add(btnCrear);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnFiltrar);
        botones.add(btnVerTodos);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private void actualizarTablaClientes() {
        cargarClientesEnTabla(GestionClientes.leerClientes(em));
    }

    private void cargarClientesEnTabla(List<Cliente> clientes) {
        modeloClientes.setRowCount(0);
        for (Cliente c : clientes) {
            modeloClientes.addRow(new Object[] { c.getId(), c.getNombre(), c.getApellido(), c.getCorreo(),
                    c.getTelefono() });
        }
    }


    // ==================== PESTAÑA FACTURAS ====================
    private JPanel crearPanelFacturas() {
        JPanel panel = new JPanel(new BorderLayout());
        JTable tablaFacturas = new JTable(modeloFacturas);
        panel.add(new JScrollPane(tablaFacturas), BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton btnCrear = new JButton("Crear Factura");
        JButton btnVerDetalle = new JButton("Ver Detalle");
        JButton btnEliminar = new JButton("Eliminar Factura");
        JButton btnFiltrar = new JButton("Filtrar (JPQL)");
        JButton btnVerTodas = new JButton("Ver Todas");

        btnCrear.addActionListener(e -> {
            JTextField txtClienteId = new JTextField();
            JTextField txtMetodoId = new JTextField();
            JTextField txtProductoId = new JTextField();
            JTextField txtCantidad = new JTextField();
            Object[] campos = {
                    "ID Cliente:", txtClienteId,
                    "ID Método de Pago:", txtMetodoId,
                    "ID Producto:", txtProductoId,
                    "Cantidad:", txtCantidad
            };
            if (JOptionPane.showConfirmDialog(this, campos, "Nueva Factura",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    GestionFacturas.crearFactura(
                            em,
                            LocalDate.now(),
                            Integer.parseInt(txtClienteId.getText().trim()),
                            Integer.parseInt(txtMetodoId.getText().trim()),
                            Integer.parseInt(txtProductoId.getText().trim()),
                            Integer.parseInt(txtCantidad.getText().trim()));
                    actualizarTablaFacturas();
                    actualizarTablaDetalles();
                } catch (Exception ex) {
                    mostrarError(ex.getMessage());
                }
            }
        });

        btnVerDetalle.addActionListener(e -> {
            String idDefault = "";
            int filaSeleccionada = tablaFacturas.getSelectedRow();
            if (filaSeleccionada != -1) {
                idDefault = String.valueOf(modeloFacturas.getValueAt(filaSeleccionada, 0));
            }
            String idStr = JOptionPane.showInputDialog(this, "ID de la factura para ver su detalle:", idDefault);
            if (idStr != null && !idStr.isBlank()) {
                try {
                    int facturaId = Integer.parseInt(idStr.trim());
                    List<DetalleFactura> detalles = GestionFacturas.leerDetallesPorFactura(em, facturaId);
                    if (detalles.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                "No se encontraron detalles para la factura ID " + facturaId);
                        return;
                    }
                    DefaultTableModel modPopup = crearModeloTabla(
                            new String[] { "ID Detalle", "Producto", "Cantidad", "Precio Unit.", "Subtotal" });
                    for (DetalleFactura d : detalles) {
                        String prodNombre = (d.getProducto() != null) ? d.getProducto().getNombre() : "N/A";
                        modPopup.addRow(new Object[] { d.getId(), prodNombre, d.getCantidad(), d.getPrecioUnitario(),
                                d.getSubtotal() });
                    }
                    JTable tablaPopup = new JTable(modPopup);
                    JScrollPane scroll = new JScrollPane(tablaPopup);
                    scroll.setPreferredSize(new Dimension(500, 200));
                    JOptionPane.showMessageDialog(this, scroll, "Detalle de Factura #" + facturaId,
                            JOptionPane.PLAIN_MESSAGE);
                } catch (Exception ex) {
                    mostrarError(ex.getMessage());
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "ID de la factura a eliminar:");
            if (idStr != null && !idStr.isBlank()) {
                try {
                    boolean ok = GestionFacturas.eliminarFactura(em, Integer.parseInt(idStr.trim()));
                    if (!ok)
                        mostrarError("No se encontró la factura.");
                    actualizarTablaFacturas();
                    actualizarTablaDetalles();
                } catch (Exception ex) {
                    mostrarError(ex.getMessage());
                }
            }
        });

        btnFiltrar.addActionListener(e -> {
            JTextField txtTotalMin = new JTextField("20.00");
            JTextField txtPagina = new JTextField("1");
            JTextField txtTamano = new JTextField("5");

            Object[] campos = {
                    "Consulta 3: Total MAYOR o IGUAL a (Orden DESC):", txtTotalMin,
                    "Número de página (1, 2...):", txtPagina,
                    "Tamaño de página (registros):", txtTamano
            };

            if (JOptionPane.showConfirmDialog(this, campos, "Filtrar Facturas (JPQL)",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    BigDecimal totalMin = new BigDecimal(txtTotalMin.getText().trim());
                    int pagina = Math.max(1, Integer.parseInt(txtPagina.getText().trim()));
                    int tamano = Math.max(1, Integer.parseInt(txtTamano.getText().trim()));

                    List<Factura> resultado = GestionFacturas.buscarFacturasPorTotalMayor(em, totalMin, pagina, tamano);
                    cargarFacturasEnTabla(resultado);
                } catch (Exception ex) {
                    mostrarError(ex.getMessage());
                }
            }
        });

        btnVerTodas.addActionListener(e -> actualizarTablaFacturas());

        botones.add(btnCrear);
        botones.add(btnVerDetalle);
        botones.add(btnEliminar);
        botones.add(btnFiltrar);
        botones.add(btnVerTodas);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private void actualizarTablaFacturas() {
        cargarFacturasEnTabla(GestionFacturas.leerFacturas(em));
    }

    private void cargarFacturasEnTabla(List<Factura> facturas) {
        modeloFacturas.setRowCount(0);
        for (Factura f : facturas) {
            String clienteNombre = (f.getCliente() != null) ? f.getCliente().getNombre() : "N/A";
            String pagoId = (f.getMetodoPago() != null) ? String.valueOf(f.getMetodoPago().getId()) : "N/A";
            modeloFacturas.addRow(new Object[] { f.getId(), f.getFecha(), f.getTotal(), clienteNombre, pagoId });
        }
    }

    // ==================== PESTAÑA DETALLES DE FACTURA ====================
    private JPanel crearPanelDetalles() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(new JTable(modeloDetalles)), BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton btnFiltrarFactura = new JButton("Buscar por ID de Factura");
        JButton btnVerTodos = new JButton("Ver Todos los Detalles");

        btnFiltrarFactura.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "Ingresa el ID de la Factura:");
            if (idStr != null && !idStr.isBlank()) {
                try {
                    int facturaId = Integer.parseInt(idStr.trim());
                    cargarDetallesEnTabla(GestionFacturas.leerDetallesPorFactura(em, facturaId));
                } catch (Exception ex) {
                    mostrarError(ex.getMessage());
                }
            }
        });

        btnVerTodos.addActionListener(e -> actualizarTablaDetalles());

        botones.add(btnFiltrarFactura);
        botones.add(btnVerTodos);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private void actualizarTablaDetalles() {
        cargarDetallesEnTabla(GestionFacturas.leerTodosDetalles(em));
    }

    private void cargarDetallesEnTabla(List<DetalleFactura> detalles) {
        modeloDetalles.setRowCount(0);
        for (DetalleFactura d : detalles) {
            Integer factId = (d.getFactura() != null) ? d.getFactura().getId() : null;
            String prodNombre = (d.getProducto() != null) ? d.getProducto().getNombre() : "N/A";
            modeloDetalles.addRow(new Object[] {
                    d.getId(),
                    factId,
                    prodNombre,
                    d.getCantidad(),
                    d.getPrecioUnitario(),
                    d.getSubtotal()
            });
        }
    }

    // ==================== PESTAÑA MÉTODOS DE PAGO ====================
    private JPanel crearPanelMetodosPago() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(new JTable(modeloMetodosPago)), BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton btnCrear = new JButton("Crear Método de Pago");
        JButton btnEliminar = new JButton("Eliminar Método de Pago");

        btnCrear.addActionListener(e -> {
            JTextField txtNombre = new JTextField();
            JTextField txtDesc = new JTextField();
            Object[] campos = {
                    "Nombre (ej. Efectivo, Tarjeta):", txtNombre,
                    "Descripción:", txtDesc
            };
            if (JOptionPane.showConfirmDialog(this, campos, "Nuevo Método de Pago",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    GestionMetodosPago.crearMetodoPago(em, txtNombre.getText().trim(), txtDesc.getText().trim());
                    actualizarTablaMetodosPago();
                } catch (Exception ex) {
                    mostrarError(ex.getMessage());
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "ID del método de pago a eliminar:");
            if (idStr != null && !idStr.isBlank()) {
                try {
                    boolean ok = GestionMetodosPago.eliminarMetodoPago(em, Integer.parseInt(idStr.trim()));
                    if (!ok)
                        mostrarError("No se encontró el método de pago.");
                    actualizarTablaMetodosPago();
                } catch (Exception ex) {
                    mostrarError(ex.getMessage());
                }
            }
        });

        botones.add(btnCrear);
        botones.add(btnEliminar);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private void actualizarTablaMetodosPago() {
        modeloMetodosPago.setRowCount(0);
        List<MetodoPago> metodos = GestionMetodosPago.leerMetodosPago(em);
        for (MetodoPago mp : metodos) {
            modeloMetodosPago.addRow(new Object[] { mp.getId(), mp.getNombre(), mp.getDescripcion() });
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, "Error: " + mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}