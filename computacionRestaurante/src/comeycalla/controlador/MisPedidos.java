package comeycalla.controlador;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import comeycalla.modelo.*;

/**
 * Servlet implementation class MisPedidos
 */
@WebServlet("/mis_pedidos")
public class MisPedidos extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MisPedidos() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Usuario userSession = (Usuario) session.getAttribute("usuario"); 
		
		try (Conexion conexion = Conexion.conexion()) {
		Usuario usuario = conexion.cargaUsuario(userSession.getId());
		
		if(usuario!=null) {
			
			RequestDispatcher despach = null;
			if(usuario.isRestaurante()) {
				Restaurante restaurante = (Restaurante) conexion.restaurantePorUsuarioGestor(usuario);
				request.setAttribute("pedidos_recientes", restaurante.getPedidos());
				request.setAttribute("idRestaurante", restaurante.getId());
				despach= request.getRequestDispatcher("/mis_pedidos_gestores.jsp");
			}else {
				request.setAttribute("pedidos_recientes", usuario.getPedidos());
				despach= request.getRequestDispatcher("/mis_pedidos_cliente.jsp");
			}
			conexion.borradoCache();
			despach.forward(request, response);
		}else {
			response.sendRedirect("./index.html?entrada=denegada");
		}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
