package comeycalla.websocket;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.Session;

public class IdsPorSesion{
	private Session sesion;
	private List <Integer> idList;
	
	public IdsPorSesion(Session sesion, int id) {
		this.sesion=sesion;
		this.idList=new ArrayList<>();
		this.idList.add(id);
	}
	
	public IdsPorSesion(Session sesion) {
		this.sesion=sesion;
		this.idList=new ArrayList<>();
	}
	
	public Session getSesion() {
		return sesion;
	}
	public void setSesion(Session sesion) {
		this.sesion = sesion;
	}
	public List <Integer> getIdList() {
		return idList;
	}
	public void setIdList(List <Integer> id) {
		this.idList = id;
	}
	
	public void anyadirId(int id) {
		idList.add(id);
	}
	
	
}
