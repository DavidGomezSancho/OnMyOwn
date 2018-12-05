package comeycalla.controlador;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import comeycalla.modelo.Conexion;
import comeycalla.modelo.Restaurante;
import comeycalla.modelo.Usuario;

/**
 * Servlet implementation class validar
 */
@WebServlet("/validar")
public class validar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public validar() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String user = request.getParameter("user");
		String password = request.getParameter("password");
		
		try (Conexion conexion = Conexion.conexion()) {
			Usuario usuario = conexion.cargaUsuario(user, password);
			if(usuario!=null) {
				Usuario usuario2 = new Usuario();
				usuario2.setLogin(user);
				usuario2.setPass(password);
				usuario2.setRestaurante(usuario.isRestaurante());
				usuario2.setId(usuario.getId());
				session.setAttribute("usuario", usuario2);
				
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
				despach.forward(request, response);
			}else {
				response.sendRedirect("./index.html?entrada=denegada");
			}
		}
	}

}
