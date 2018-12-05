package comeycalla.controlador;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import comeycalla.modelo.*;

/**
 * Servlet implementation class Registro
 */
@WebServlet("/registro")
public class Registro extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Registro() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login = request.getParameter("user");
		String pass = request.getParameter("password");
		String correo = request.getParameter("correo");
		int telefono = Integer.parseInt(request.getParameter("telefono"));
		Date fecha = new Date();
		java.sql.Date fechaSQL = new java.sql.Date(fecha.getTime());
		
		Usuario usuario = new Usuario();
		usuario.setLogin(login);
		usuario.setPass(pass);
		usuario.setEmail(correo);
		usuario.setTelefono(telefono);
		usuario.setCreacion(fechaSQL);
		usuario.setRestaurante(false);
		usuario.setId(0);
		
		try (Conexion conexion = Conexion.conexion()) {
			conexion.insertaUsuario(usuario);
			conexion.commit();
			conexion.borradoCache();
		}
		
		response.sendRedirect("./index.html");
	}

}
