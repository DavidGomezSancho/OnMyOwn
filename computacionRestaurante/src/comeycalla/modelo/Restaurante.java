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

@Entity(name="restaurantes")
@Table(name="restaurantes")
public class Restaurante {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
    private String nombre;
    private boolean chino;
    private boolean kebab;
    private boolean burguer;
    private boolean indio;
    private boolean japones;
    private String direccion;
    private int postal;
    private int telefono;
    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;
    
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
	public boolean isChino() {
		return chino;
	}
	public void setChino(boolean chino) {
		this.chino = chino;
	}
	public boolean isKebab() {
		return kebab;
	}
	public void setKebab(boolean kebab) {
		this.kebab = kebab;
	}
	public boolean isBurguer() {
		return burguer;
	}
	public void setBurguer(boolean burguer) {
		this.burguer = burguer;
	}
	public boolean isIndio() {
		return indio;
	}
	public void setIndio(boolean indio) {
		this.indio = indio;
	}
	public boolean isJapones() {
		return japones;
	}
	public void setJapones(boolean japones) {
		this.japones = japones;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public int getPostal() {
		return postal;
	}
	public void setPostal(int postal) {
		this.postal = postal;
	}
	public int getTelefono() {
		return telefono;
	}
	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "Restaurante [id=" + id + ", nombre=" + nombre + ", chino=" + chino + ", kebab=" + kebab + ", burguer="
				+ burguer + ", indio=" + indio + ", japones=" + japones + ", direccion=" + direccion + ", postal="
				+ postal + ", telefono=" + telefono + ", usuario=" + usuario + ", cartas=" + cartas
				+ ", pedidos=" + pedidos + "]";
	}

	@OneToMany(mappedBy="restaurante", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private List<Carta> cartas = new ArrayList<Carta>();
	
	public List<Carta> getCartas() {
		return cartas;
	}
	
	@OneToMany(mappedBy="restaurante", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private List<Pedido> pedidos = new ArrayList<Pedido>();
	
	public List<Pedido> getPedidos() {
		return pedidos;
	}
}
