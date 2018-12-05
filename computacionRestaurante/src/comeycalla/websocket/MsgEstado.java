package comeycalla.websocket;
public class MsgEstado{
	private int id;
	private String estado;
	
	public MsgEstado(int id, String estado){
		this.id=id;
		this.estado=estado;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setEstado(String estado) {
		this.estado=estado;
	}
	
	public String getEstado() {
		return estado;
	}
}
