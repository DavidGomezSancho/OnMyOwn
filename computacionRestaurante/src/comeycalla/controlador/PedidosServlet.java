package comeycalla.controlador;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import comeycalla.herramientas.Parametros;
import comeycalla.modelo.Conexion;
import comeycalla.modelo.Pedido;
import comeycalla.modelo.PedidoProductos;
import comeycalla.modelo.Usuario;

/**
 * Servlet implementation class PedidosServlet
 */
@WebServlet("/pedidos/*")
public class PedidosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PedidosServlet() {
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
    	else if (path.startsWith("/usuario")) {
    		listaPedidos(request, response);
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
    	
    	if (path.startsWith("/hacer")) {
    		//hacer(request,response);
    	}
    	else {
    		response.sendRedirect(request.getContextPath() + "/login");
    	}
	}

	private void listaPedidos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		String user = usuario.getLogin();
		String password = usuario.getPass();
		try (Conexion conexion = Conexion.conexion()) {
			Usuario usuarioBBDD = conexion.cargaUsuario(user, password);
			if(usuarioBBDD!=null){
	        	request.setAttribute("pedidos_recientes", usuarioBBDD.getPedidos());
	            getServletContext().getRequestDispatcher("/mis_pedidos_cliente.jsp").forward(request, response);
			}else {
				response.sendRedirect("./index.html?entrada=denegada");
			}
        }
	}
	
	private void mostrarCreacion(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	int id = Parametros.obtieneId(req,1);
        try (Conexion conexion = Conexion.conexion()) {
        	Pedido pedido = conexion.cargaPedido(id);
        	req.setAttribute("pedido", pedido);
        	List<PedidoProductos> pedproductos = conexion.listaPedidoProducto(pedido);
        	req.setAttribute("pedproductos", pedproductos);
            getServletContext().getRequestDispatcher("/ver_pedido.jsp").forward(req, resp);
        }
    }

}
