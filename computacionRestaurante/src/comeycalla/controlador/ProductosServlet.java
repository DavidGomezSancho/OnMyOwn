package comeycalla.controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import comeycalla.modelo.Conexion;
import comeycalla.modelo.Producto;

/**
 * Servlet implementation class ProductosServlet
 */
@WebServlet("/ProductosServlet")
public class ProductosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductosServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    		response.sendRedirect(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getPathInfo();

    	if (path.startsWith("/anyadir")) {
    		anyadir(request,response);
    	}
    	else if (path.startsWith("/eliminar")) {
    		eliminar(request, response);
    	}
    	else if (path.startsWith("/cambiar")) {
    		cambiar(request, response);
    	}
    	else {
    		response.sendRedirect(request.getContextPath() + "/login");
    	}
	}

	private void cambiar(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int id_producto=0;
		if(request.getParameter("id_producto")!="")
			id_producto = Integer.parseInt(request.getParameter("id_producto"));
		double precio=0;
		if(request.getParameter("precio")!="")
			precio = Double.parseDouble(request.getParameter("precio"));
		try (Conexion conexion = Conexion.conexion()) {
			Producto producto = conexion.cargaProducto(id_producto);
			producto.setPrecio(precio);
			
			conexion.modificaProducto(producto);
			conexion.commit();
			response.sendRedirect(request.getContextPath() + "/restaurantes/ver");
		}
	}

	private void eliminar(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int id_producto=0;
		if(request.getParameter("id_producto")!="")
			id_producto = Integer.parseInt(request.getParameter("id_producto"));
		try (Conexion conexion = Conexion.conexion()) {
			conexion.borraProducto(id_producto);
			conexion.commit();
			response.sendRedirect(request.getContextPath() + "/restaurantes/ver");
		}
	}

	private void anyadir(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//recoger parametros
				String nombre=null;
				if(request.getParameter("nombre")!="")
					nombre = request.getParameter("nombre");
				String descripcion=null;
				if(request.getParameter("descripcion")!="")
					descripcion = request.getParameter("descripcion");
				double precio=0;
				int id_carta=0;
				if(request.getParameter("precio")!="")
					precio = Double.parseDouble(request.getParameter("precio"));
				if(request.getParameter("id_carta")!="")
					id_carta = Integer.parseInt(request.getParameter("id_carta"));
				
				try (Conexion conexion = Conexion.conexion()) {
		        	Producto producto = new Producto();
		        	producto.setDescripcion(descripcion);
		        	producto.setPrecio(precio);
		        	producto.setNombre(nombre);
		        	producto.setCarta(conexion.cargaCarta(id_carta));
		            
		        	conexion.insertaProducto(producto);
		        	conexion.commit();
		            response.sendRedirect(request.getContextPath() + "/restaurantes/ver");
		        }
	}

}
