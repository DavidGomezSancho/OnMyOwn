package comeycalla.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
 
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

import comeycalla.modelo.Conexion;
import comeycalla.modelo.Pedido;
import comeycalla.modelo.Pedido.Estado;

@ServerEndpoint(value = "/cambiar_estado")
public class CambioEstado {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private static final List <Session> conectados = new ArrayList<>();
	
    @OnOpen
    public void onOpen(Session session) {
        logger.info("Connected ... " + session.getId());
        conectados.add(session);
    }
 
    @OnMessage
    public void onMessage(String message, Session session) throws IOException, InterruptedException {
    	Gson gson = new Gson();
    	try {
    		MsgEstado estado = gson.fromJson(message, MsgEstado.class);
    		Conexion conexion = Conexion.conexion();
    		Pedido pedido = conexion.cargaPedido(estado.getId());
    		Estado estadoSQL = null;
    		if(estado.getEstado().equals("pendiente")) {
    			estadoSQL=Estado.Pendiente;
    		}
    		else if(estado.getEstado().equals("preparando")) {
    			estadoSQL=Estado.Preparando;
    		}
    		else if(estado.getEstado().equals("enviando")) {
    			estadoSQL=Estado.Enviando;
    		}
    		else if(estado.getEstado().equals("entregado")) {
    			estadoSQL=Estado.Entregado;
    		}
    		
    		if(estadoSQL!=null) {
    			pedido.setEstado(estadoSQL);
    			conexion.modificaPedido(pedido);
    			conexion.commit();
    			conexion.borradoCache();
    		}
    		
        	for(Session sesion : sesionesPorId(estado.getId())) {
        		EstadoPedido.enviar(estado, sesion);
        	}
    	}catch(Exception e) {
    		System.out.println(e.getMessage());
    	}
    }
 
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
        conectados.remove(session);
    }
	
    private List<Session> sesionesPorId(int idBuscar){
    	List<Session> sesiones = new ArrayList<>();
    	List<IdsPorSesion> idsLista=EstadoPedido.getIdsPorSesion();
    	for(IdsPorSesion ids : idsLista) {
    		for(int id : ids.getIdList()) {
    			if(id==idBuscar) {
    				sesiones.add(ids.getSesion());
    				break;
    			}
    		}
    	}
    	return sesiones;
    }
	
	
}
