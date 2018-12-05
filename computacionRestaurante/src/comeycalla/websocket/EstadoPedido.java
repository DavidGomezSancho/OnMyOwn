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
 
@ServerEndpoint(value = "/consultar_estado")
public class EstadoPedido {
 
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private static final List <IdsPorSesion> idsporsesion = new ArrayList<>();
    private static Gson gson = new Gson();

    @OnOpen
    public void onOpen(Session session) {
        logger.info("Connected ... " + session.getId());
        IdsPorSesion sesion = new IdsPorSesion(session);
        getIdsPorSesion().add(sesion);
    }
 
    @OnMessage
    public void onMessage(String message, Session session) throws IOException, InterruptedException {
    	
    	ValorId id = gson.fromJson(message, ValorId.class);
    	
    	IdsPorSesion idsporsesion = buscarPorSesion(session);
    	
    	idsporsesion.anyadirId(id.getId());
    }
 
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
        getIdsPorSesion().remove(buscarPorSesion(session));
    }
    
    public IdsPorSesion buscarPorSesion(Session sesion) {
    	for (IdsPorSesion idSesion : getIdsPorSesion()) {
    		if(idSesion.getSesion().getId()==sesion.getId())
    			return idSesion;
    	}
    	return null;
    }
    
    
    public static void enviar(MsgEstado msgEnviar, Session sesion) throws IOException {
    	sesion.getBasicRemote().sendText(gson.toJson(msgEnviar));
    }
    
    
    public static List <IdsPorSesion> getIdsPorSesion() {
		return idsporsesion;
	}


	public class ValorId{
    	private int id;
    	
    	public ValorId(int id){
    		this.id=id;
    	}
    	
    	public void setId(int id) {
    		this.id=id;
    	}
    	
    	public int getId() {
    		return id;
    	}
    };

}
