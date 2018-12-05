package comeycalla.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name="cartas")
@Table(name="cartas")
public class Carta {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String nombre;
	@ManyToOne
	@JoinColumn(name = "idRestaurante")
	private Restaurante restaurante;
	
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
	
	@Override
	public String toString() {
		return "Carta [id=" + id + ", nombre=" + nombre + ", restaurante=" + getRestaurante() + ", productos=" + productos
				+ "]";
	}

	@OneToMany(mappedBy="carta", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private List<Producto> productos = new ArrayList<Producto>();
	
	public List<Producto> getProductos() {
		return productos;
	}
	public Restaurante getRestaurante() {
		return restaurante;
	}
	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}
}
