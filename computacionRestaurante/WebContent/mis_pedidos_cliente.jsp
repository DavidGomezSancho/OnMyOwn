<!-- Esta vista corresponde al punto 1 del apartado 5 del enunciado -->


<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import ='comeycalla.modelo.*' %>
<%@	page import ='java.util.ArrayList'%>
<%@	page import ='java.util.List'%>
<%@	page import ='java.lang.*'%>

	
<% 
	if(session.getAttribute("usuario")==null){ 
    	response.sendRedirect("./index.html?entrada=denegada");
    }
%>
<!doctype html>

<html lang='es-ES'>
	<head>
		<meta charset='UTF-8'/>
		<title>Mis restaurantes</title>
		<style type='text/css'>
			@import url('css/style.css');
		</style>
	</head>
	<body>
		<header id='cabecera'>
			<div id='logo'>
				<a href='mis_pedidos'>ComeYCalla
				<span class='slogan'>y que aproveche</span></a>
			</div>			
			<div id='panel' class='aligndech'>
				<a href='./sesion_off'>Cerrar Sesion</a>
			</div> 			  
		</header>		
		<section id='izquierda'>
			<h1 class='aligndech'>Pedidos recientes</h1>
			<div id='izqrd'>
			<%
				java.util.List<Pedido> pedidos = (java.util.List<Pedido>) request.getAttribute("pedidos_recientes");
				String tipo;
				if(pedidos!=null){
				for (Pedido pedido : pedidos){
 	        		tipo="";
 	        		if(pedido.getRestaurante().isBurguer())
 	        			tipo="burguer";
 	        		if(pedido.getRestaurante().isKebab())
 	        			tipo="kebab";
 	        		if(pedido.getRestaurante().isChino())
 	        			tipo="chino";
 	        		if(pedido.getRestaurante().isIndio())
 	        			tipo="indio";
 	        		if(pedido.getRestaurante().isJapones())
 	        			tipo="japones";
			%>
			
				<a href='./pedidos/ver/<%= pedido.getId() %>'><article>
	        		<span class='titulo1'><%= pedido.getRestaurante().getNombre() %></span>
					<span class='slogan'>Fecha: <%= pedido.getFecha().toString() %></span>
	        		<span class='slogan'>Direccion: <%= pedido.getDireccion()%></span>
 	        		<span class='slogan'>Tipo: <%= tipo%></span> 
			
	        		<span class='derecha'>Estado: <%= pedido.getEstado() %></span>
	        		
	        	</article></a>
	        <%
	        	}
				}
			%>
			</div>
		</section>
		<form action="./restaurantes/busqueda" method="post">
			<section id='izquierda'>
			<!-- Aqui se busca -->
				<h1 class='aligndech'>Búsqueda de restaurantes</h1>
				<div id='drcha'>
					<table id='tableavnzd'>
						<tr><td><label for='Nombre'>Nombre:</label></td>
						<td><input id='nombre' type='text' name='nombre' placeholder='Nombre...'/></td></tr>
						<tr><td><label for='direccion'  >Direccion:</label></td>
							<td><input id='direccion' name='direccion' type='text' placeholder='DirecciÃ³n:...'/>
							</td>
						</tr>
						<tr>
							<td>
								<label for='tipo'  >Tipo:</label>
							</td>
							<td>
								<select name="tipo">
 									<option value="chino">Chino</option>
 									<option value="kebab">Kebab</option>
									<option value="burguer">Burguer</option>
									<option value="indio">Indio</option>
  									<option value="japones">Japones</option>
								</select>
							</td>
						</tr>
						<tr>
							<td><label for='postal' >Postal:</label></td>
							<td>
								<input id='postal' name='postal' type='number'/>
							</td>
						</tr>
						<tr>
							<td><label for='telefono' >Teléfono:</label></td>
							<td><input id='telefono' name='telefono' type='tel'/></td>
						</tr>
					</table>
					<input type="submit" value="Búsqueda"/>
				</div>
			</section>
		</form>
	</body>
</html>