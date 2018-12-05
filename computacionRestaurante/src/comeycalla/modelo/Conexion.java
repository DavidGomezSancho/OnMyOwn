package comeycalla.modelo;

import java.sql.Connection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class Conexion implements AutoCloseable {
    
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory("comeycalla");
	private EntityManager em = null;

	public static void main(String[] args) {
		Conexion conexion = new Conexion();
		if(conexion.getConnection()!=null){
			System.out.println("conectado");
		}
		Usuario usuario = conexion.cargaUsuario(1);
		System.out.println(usuario.toString());
	}
	
	public static Conexion conexion() {
        return new Conexion();
    }
	
	private Conexion() {
    	em = factory.createEntityManager();
    	em.getTransaction().begin();
    }
	
	public Connection getConnection() {
    	return em.unwrap(java.sql.Connection.class);
    }
	
	public void commit() {
		em.getTransaction().commit();
    }
	
	@Override
    public void close() {
    	if (null != em) {
    		if (em.getTransaction().isActive())
    			em.getTransaction().rollback();
    		em.close();
    	}
    }

	public List<Restaurante> listaRestaurantes() {
		//PreparedStatement st = conexion.prepareStatement("SELECT r FROM Restaurante r order by r.id");
    	TypedQuery<Restaurante> q = em.createQuery("SELECT r FROM restaurantes r ORDER BY r.id", Restaurante.class);
		List<Restaurante> resultado = q.getResultList();
        return resultado;
    }
	
	public List<Restaurante> listaRestaurantes(String nombre, String direccion, String tipo, int postal, int telefono) {
		StringBuilder sql = new StringBuilder("SELECT r FROM restaurantes r ");
		String operator = "WHERE";
		
		if (null != nombre) {
			sql.append(operator);
			sql.append(" r.nombre = :nombre ");
			operator = "AND";
		}

		if (null != direccion) {
			sql.append(operator);
			sql.append(" r.direccion LIKE :direccion ");
			operator = "AND";
		}
		
		if (null != tipo) {
			switch(tipo){
			case "chino":
				sql.append(operator);
				sql.append(" r.chino = 1 ");
				operator = "AND";
				break;
			case "kebab":
				sql.append(operator);
				sql.append(" r.kebab = 1 ");
				operator = "AND";
				break;
			case "burguer":
				sql.append(operator);
				sql.append(" r.burguer = 1 ");
				operator = "AND";
				break;
			case "indio":
				sql.append(operator);
				sql.append(" r.indio = 1 ");
				operator = "AND";
				break;
			case "japones":
				sql.append(operator);
				sql.append(" r.japones = 1 ");
				operator = "AND";
				break;
			default:
				//nada
				break;
			}
		}
		
		if (postal > 0) {
			sql.append(operator);
			sql.append(" r.postal = :postal ");
			operator = "AND";
		}
		
		if (telefono > 0 && Integer.toString(telefono).length()==9) {//numero de 9 cifras
			sql.append(operator);
			sql.append(" r.telefono = :telefono ");
			operator = "AND";
		}
		
		sql.append(" ORDER BY r.nombre");
		
		TypedQuery<Restaurante> q = em.createQuery(sql.toString(), Restaurante.class);
		
		if (null != nombre) {
			q.setParameter("nombre", nombre);
		}

		if (null != direccion) {
			q.setParameter("direccion", "%"+direccion+"%");
		}
		
		if (postal > 0) {
			q.setParameter("postal", postal);
		}
		
		if (telefono > 0 && Integer.toString(telefono).length()==9) {
			q.setParameter("telefono", telefono);
		}

		return q.getResultList();
	}
	
	
	//--------------------------------------------------------------------------------------------------------
	public List<PedidoProductos> listaPedidoProducto(Pedido pedido) {
        //PreparedStatement st = conexion.prepareStatement("SELECT r FROM Restaurante r order by r.id");
        TypedQuery<PedidoProductos> q = em.createQuery("SELECT r FROM pedidosproductos r WHERE r.pedido = :pedido ORDER BY r.id", PedidoProductos.class);
        q.setParameter("pedido", pedido);
        return  q.getResultList();//List<PedidoProductos> resultado1 =
//        List<Producto> resultado = new ArrayList<>();
//        for(PedidoProductos pedidoproducto : resultado1) {
//        	resultado.add(pedidoproducto.getProducto());
//        }      
//        return resultado;
    }
	
	public List<Carta> listaCartasPorRestaurante(Restaurante restaurante) {
		TypedQuery<Carta> q = em.createQuery("SELECT c FROM cartas c WHERE c.restaurante = :restaurante ", Carta.class);
		q.setParameter("restaurante", restaurante);
		List<Carta> resultado = q.getResultList();
		return resultado;
	}
	
	public Restaurante restaurantePorUsuarioGestor(Usuario usuario) {
		TypedQuery<Restaurante> q = em.createQuery("SELECT r FROM restaurantes r WHERE r.usuario = :usuario ", Restaurante.class);
		q.setParameter("usuario", usuario);
		List<Restaurante> resultado = q.getResultList();
		return resultado.get(0);
	}
	
	public List<Pedido> listaPedidos() {
		//PreparedStatement st = conexion.prepareStatement("SELECT r FROM Restaurante r order by r.id");
    	TypedQuery<Pedido> q = em.createQuery("SELECT p FROM pedidos p ORDER BY p.id", Pedido.class);
		List<Pedido> resultado = q.getResultList();
        return resultado;
    }
	
	public Usuario cargaUsuario(String login, String password) {
    	TypedQuery<Usuario> q = em.createQuery("SELECT u FROM usuarios u WHERE u.login = :login ", Usuario.class);
    	q.setParameter("login", login);
    	
    	if (null != q.getResultList() && q.getResultList().size() > 0){
    		Usuario user = q.getSingleResult();
    		if(user.getPass().equals(password)){
    			return user;
    		}
    	}
    	
    	return null;
    }
	
	public Restaurante cargaRestaurante(int id) {
    	return em.find(Restaurante.class, id);
    }
	public Usuario cargaUsuario (int id) {
    	return em.find(Usuario.class, id);
    }
	public Pedido cargaPedido(int id) {
		return em.find(Pedido.class, id);
	}
	public Producto cargaProducto(int id) {
		return em.find(Producto.class, id);
	}
	public Carta cargaCarta(int id) {
		return em.find(Carta.class, id);
	}
	
	public void insertaRestaurante (Restaurante restaurante) {
        em.persist(restaurante);
        factory.getCache().evict(Restaurante.class);
    }
	
	public void insertaUsuario (Usuario usuario) {
        em.persist(usuario);
        factory.getCache().evict(Usuario.class);
    }
	
	public void insertaProducto (Producto producto) {
        em.persist(producto);
        factory.getCache().evict(Producto.class);
    }
	
	public void insertaPedido(Pedido pedido) {
        em.persist(pedido);
        factory.getCache().evict(Pedido.class);
    }
	
	public void insertaPedidoProductos(PedidoProductos pedidoProductos) {
        em.persist(pedidoProductos);
        factory.getCache().evict(PedidoProductos.class);
    }
	
	public void insertaCarta(Carta carta) {
        em.persist(carta);
        factory.getCache().evict(Carta.class);
    }
	
	public void modificaRestaurante(Restaurante restaurante) {
    	em.merge(restaurante);
    	factory.getCache().evict(Restaurante.class);
    }
	public void modificaUsuario(Usuario usuario) {
    	em.merge(usuario);
    	factory.getCache().evict(Usuario.class);
    }
	public void modificaProducto(Producto producto) {
    	em.merge(producto);
    	factory.getCache().evict(Producto.class);
    }
	public void modificaPedido(Pedido pedido) {
    	em.merge(pedido);
    	factory.getCache().evict(Pedido.class);
    }
	public void modificaCarta(Carta carta) {
    	em.merge(carta);
    	factory.getCache().evict(Carta.class);
    }
	
	public void borraRestaurante(int idRestaurante) {
		Restaurante restaurante = em.getReference(Restaurante.class, idRestaurante);
    	em.remove(restaurante);
    	factory.getCache().evict(Restaurante.class);
    }
	public void borraUsuario(int idUsuario) {
		Usuario usuario = em.getReference(Usuario.class, idUsuario);
    	em.remove(usuario);
    	factory.getCache().evict(Usuario.class);
    }
	public void borraProducto(int idProducto) {
		Producto producto = em.getReference(Producto.class, idProducto);
    	em.remove(producto);
    	factory.getCache().evict(Producto.class);
    }
	public void borraPedido(int idPedido) {
		Pedido pedido = em.getReference(Pedido.class, idPedido);
    	em.remove(pedido);
    	factory.getCache().evict(Pedido.class);
    }
	
	public void borraPedidosProductoPorProducto(Producto producto) {
		TypedQuery<PedidoProductos> q = em.createQuery("SELECT p FROM pedidosproductos p WHERE p.producto = :producto ", PedidoProductos.class);
		q.setParameter("producto", producto);
		List<PedidoProductos> resultado = q.getResultList();
		for(PedidoProductos pedidoproductos : resultado) {
			em.remove(pedidoproductos);
		}
		factory.getCache().evict(PedidoProductos.class);
	}
	
	public void borraCarta(int idCarta) {
		Carta carta = em.getReference(Carta.class, idCarta);
    	em.remove(carta);
    	factory.getCache().evict(Carta.class);
    }
	
	public void borradoCache() {
    	em.getEntityManagerFactory().getCache().evictAll();
    }

}