package comeycalla.controlador;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import comeycalla.herramientas.Parametros;
import comeycalla.modelo.Carta;
import comeycalla.modelo.Conexion;
import comeycalla.modelo.Pedido;
import comeycalla.modelo.Pedido.Estado;
import comeycalla.modelo.PedidoProductos;
import comeycalla.modelo.Producto;
import comeycalla.modelo.Restaurante;
import comeycalla.modelo.Usuario;

/**
 * Servlet implementation class PedidosServlet
 */
@WebServlet("/restaurantes/*")
public class RestaurantesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RestaurantesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getPathInfo();
		
    	if (path == null || path.length() == 0) {
    		response.sendRedirect(request.getContextPath());
    	}
    	else if (path.startsWith("/ver")) {
    		mostrarCreacion(request, response);
    	}
    	else {
    		response.sendRedirect(request.getContextPath());
    	}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getPathInfo();

    	if (path.startsWith("/busqueda")) {
    		busqueda(request, response);
    	}
    	else if(path.startsWith("/ver/cambiar_precio")) {
    		cambiarPrecio(request, response);
    	}
    	else if(path.startsWith("/ver/anyadir_producto")) {
    		anyadirProducto(request, response);
    	}
    	else if(path.startsWith("/ver/eliminar_producto")) {
    		eliminarProducto(request, response);
    	}
    	else if(path.startsWith("/ver/hacer_pedido")) {
    		hacerPedido(request, response);
    	}
    	else {
    		response.sendRedirect(request.getContextPath() + "/index.html");
    	}
	}
	
	private void hacerPedido(HttpServletRequest request, HttpServletResponse response) throws IOException{
		int idCarta = Integer.parseInt(request.getParameter("id_carta"));
		int idRestaurante = Integer.parseInt(request.getParameter("id_restaurante"));
		int postal = Integer.parseInt(request.getParameter("cod_postal"));
		String direccionCliente = request.getParameter("direccion_cliente");
		HttpSession session = request.getSession();
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		
		Pedido pedido = new Pedido();
		
		try (Conexion conexion = Conexion.conexion()) {
			Carta carta = conexion.cargaCarta(idCarta);
			Usuario usuarioReal = conexion.cargaUsuario(usuario.getId());
			
			List<Producto> productos = carta.getProductos();
			
			if(carta.getRestaurante().getPostal()==postal) {
				pedido.setId(0);
				pedido.setUsuario(usuarioReal);
				pedido.setDireccion(direccionCliente);
				pedido.setEstado(Estado.Pendiente);
				Date fecha = new Date();
				java.sql.Date fechaSQL = new java.sql.Date(fecha.getTime());
				pedido.setFecha(fechaSQL);
				pedido.setRestaurante(carta.getRestaurante());
			
				conexion.insertaPedido(pedido);
			
				int cantidad=0;
				boolean todos_a_cero=true;
				for(Producto producto : productos) {
					cantidad = Integer.parseInt(request.getParameter(Integer.toString(producto.getId())));
					if(cantidad>0) {
						PedidoProductos pedidoproducto = new PedidoProductos();
						pedidoproducto.setId(0);
						pedidoproducto.setCantidad(cantidad);
						pedidoproducto.setPedido(pedido);
						pedidoproducto.setProducto(producto);
						conexion.insertaPedidoProductos(pedidoproducto);
						todos_a_cero=false;
					}
				}
				if(todos_a_cero) {
					response.sendRedirect("./"+idRestaurante);
				}else {
					conexion.commit();
					conexion.borradoCache();
					response.sendRedirect("../../mis_pedidos");
				}
			}else {
				response.sendRedirect("./"+idRestaurante);
			}
		}
		
	}
	
	private void eliminarProducto(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int idProducto = Integer.parseInt(request.getParameter("id_producto"));
		int idRestaurante = Integer.parseInt(request.getParameter("id_restaurante"));
		HttpSession sesion = request.getSession();
		Usuario usersesion = (Usuario)sesion.getAttribute("usuario");
		
		try (Conexion conexion = Conexion.conexion()) {
			Usuario usuario = conexion.cargaUsuario(usersesion.getId());
			if(usuario.getRestaurantes().get(0).getId()==idRestaurante) {
				Producto producto = conexion.cargaProducto(idProducto);
				conexion.borraPedidosProductoPorProducto(producto);
				conexion.borraProducto(idProducto);
				conexion.commit();
				conexion.borradoCache();
			}
		}
		response.sendRedirect("./"+idRestaurante);
	}
	
	private void anyadirProducto(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int idCarta = Integer.parseInt(request.getParameter("id_carta"));
		String descripcion = request.getParameter("descripcion");
		String nombre = request.getParameter("nombre");
		double precio = Double.parseDouble(request.getParameter("precio"));
		int idRestaurante = Integer.parseInt(request.getParameter("id_restaurante"));
		HttpSession sesion = request.getSession();
		Usuario usersesion = (Usuario)sesion.getAttribute("usuario");
		
		try (Conexion conexion = Conexion.conexion()) {
			Producto producto = new Producto();
			Usuario usuario = conexion.cargaUsuario(usersesion.getId());
			if(usuario.getRestaurantes().get(0).getId()==idRestaurante) {
				Carta carta = conexion.cargaCarta(idCarta);
				producto.setCarta(carta);
				producto.setDescripcion(descripcion);
				producto.setId(0);
				producto.setNombre(nombre);
				producto.setPrecio(precio);
				conexion.insertaProducto(producto);
				conexion.commit();
				conexion.borradoCache();
			}
		}
		response.sendRedirect("./"+idRestaurante);
	}
	
	private void cambiarPrecio(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int idProducto = Integer.parseInt(request.getParameter("id_producto"));
		double precio = Double.parseDouble(request.getParameter("precio"+idProducto));
		int idRestaurante = Integer.parseInt(request.getParameter("id_restaurante"));
		HttpSession sesion = request.getSession();
		Usuario usersesion = (Usuario)sesion.getAttribute("usuario");
		
		try (Conexion conexion = Conexion.conexion()) {
			Usuario usuario = conexion.cargaUsuario(usersesion.getId());
			if(usuario.getRestaurantes().get(0).getId()==idRestaurante) {
				Producto producto = conexion.cargaProducto(idProducto);
				producto.setPrecio(precio);
				conexion.modificaProducto(producto);
				conexion.commit();
				conexion.borradoCache();
			}
		}
		response.sendRedirect("./"+idRestaurante);
	}

	private void busqueda(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//recoger parametros
		String nombre=null;
		if(request.getParameter("nombre")!="")
			nombre = request.getParameter("nombre");
		String direccion=null;
		if(request.getParameter("direccion")!="")
			direccion = request.getParameter("direccion");
		String tipo = request.getParameter("tipo");
		int postal=0;
		int telefono=0;
		if(request.getParameter("postal")!="")
			postal = Integer.parseInt(request.getParameter("postal"));
		if(request.getParameter("telefono")!="")
			telefono = Integer.parseInt(request.getParameter("telefono"));
		
		try (Conexion conexion = Conexion.conexion()) {
			List<Restaurante> restaurantes = conexion.listaRestaurantes(nombre, direccion, tipo, postal, telefono);
			request.setAttribute("restaurantes", restaurantes);
			getServletContext().getRequestDispatcher("/busqueda_restaurante.jsp").forward(request, response);
		}
		
	}
	
	private void mostrarCreacion(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	int id = Parametros.obtieneId(req,1);
        try (Conexion conexion = Conexion.conexion()) {
        	
        	Restaurante restaurante = conexion.cargaRestaurante(id);
        	req.setAttribute("restaurante", restaurante);
        	List<Carta> cartas = conexion.listaCartasPorRestaurante(restaurante);
        	Carta carta = cartas.get(0);
        	req.setAttribute("carta", carta);
            getServletContext().getRequestDispatcher("/ver_restaurante.jsp").forward(req, resp);
        }
    }

}
