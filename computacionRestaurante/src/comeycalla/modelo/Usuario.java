package comeycalla.modelo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity(name="usuarios")
@Table(name="usuarios")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
    private String login;
    private String pass;
    private String email;
    private int telefono;
    private Date creacion;
    private boolean restaurante;
    
    
	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getLogin() {
		return login;
	}



	public void setLogin(String login) {
		this.login = login;
	}



	public String getPass() {
		return pass;
	}



	public void setPass(String pass) {
		this.pass = pass;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public int getTelefono() {
		return telefono;
	}



	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}



	public Date getCreacion() {
		return creacion;
	}



	public void setCreacion(Date creacion) {
		this.creacion = creacion;
	}


	public boolean isRestaurante() {
		return restaurante;
	}



	public void setRestaurante(boolean restaurante) {
		this.restaurante = restaurante;
	}
	
	@Override
	public String toString() {
		return "Usuario [id=" + id + ", login=" + login + ", pass=" + pass + ", email=" + email + ", telefono="
				+ telefono + ", creacion=" + creacion + ", restaurante=" + restaurante
				+ ", restaurantes=" + restaurantes + ", pedidos=" + pedidos + "]";
	}

	@OneToMany(mappedBy="usuario", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private List<Restaurante> restaurantes = new ArrayList<Restaurante>();
	
	public List<Restaurante> getRestaurantes() {
		return restaurantes;
	}
	
	@OneToMany(mappedBy="usuario", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private List<Pedido> pedidos = new ArrayList<Pedido>();
	
	public List<Pedido> getPedidos() {
		return pedidos;
	}

}
