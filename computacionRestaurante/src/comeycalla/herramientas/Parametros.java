package comeycalla.herramientas;

import javax.servlet.http.HttpServletRequest;

public class Parametros {

	public static int obtieneId(HttpServletRequest req) {
        String extraPathInfo = req.getPathInfo();
        if(extraPathInfo == null) {
            return -1;
        } else {
            String strId = extraPathInfo.substring(1);
            try {
                return Integer.parseInt(strId);
            } catch(NumberFormatException e) {
                return -1;
            }
        }
    }
	
	public static int obtieneId(HttpServletRequest req, int indiceParametro) {
        String extraPathInfo = req.getPathInfo();
        if(extraPathInfo == null) {
            return -1;
        } else {
        	String[] params = extraPathInfo.substring(1).split("/");
        	
        	if (params.length <= indiceParametro)
        		return -1;
        	else {	
	            String strId = params[indiceParametro];
	            try {
	                return Integer.parseInt(strId);
	            } catch(NumberFormatException e) {
	                return -1;
	            }
        	}
        }
    }
	
//	public Parametros() {
//		// TODO Auto-generated constructor stub
//	}

}
