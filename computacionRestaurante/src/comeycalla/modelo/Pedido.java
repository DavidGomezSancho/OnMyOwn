package comeycalla.modelo;

import java.sql.Date;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name="pedidos")
@Table(name="pedidos")
public class Pedido {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	public enum Estado {
		Pendiente("Pendiente"), 
		Preparando("Preparando"),
		Enviando("Enviando"),
		Entregado("Entregado");
		
		private String nombreEstado;
		
		private Estado(String estado) {
			nombreEstado = estado;
		}
		
		public String getEstado() {
			return nombreEstado;
		}
	}
	private String direccion;
	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;
	@ManyToOne
	@JoinColumn(name = "idRestaurante")
	private Restaurante restaurante;
	private Date fecha;
	@Enumerated(EnumType.STRING)
	private Estado estado;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Restaurante getRestaurante() {
		return restaurante;
	}
	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	@Override
	public String toString() {
		return "Pedido [id=" + id + ", direccion=" + direccion + ", usuario=" + usuario + ", restaurante=" + restaurante
				+ ", fecha=" + fecha + "]";
	}
	
}
