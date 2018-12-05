package comeycalla.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name="productos")
@Table(name="productos")
public class Producto {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String nombre;
	private String descripcion;
	@ManyToOne
	@JoinColumn(name = "idCarta")
	private Carta carta;
	private double precio;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Carta getCarta() {
		return carta;
	}
	public void setCarta(Carta carta) {
		this.carta = carta;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	
	@Override
	public String toString() {
		return "Producto [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", carta=" + carta
				+ "]";
	}
	
}
