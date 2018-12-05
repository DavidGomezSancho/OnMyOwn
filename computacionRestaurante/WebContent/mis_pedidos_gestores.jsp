<!-- Esta vista corresponde al punto 5 del apartado 5 del enunciado -->


<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import ='comeycalla.modelo.*' %>
<% 
	if(session.getAttribute("usuario")==null){ 
    	response.sendRedirect("./index.html?entrada=denegada");
    }
	
	int idRestaurante = (Integer)request.getAttribute("idRestaurante");
	
%>
<!doctype html>

<html lang='es-ES'>
	<head>

		<meta charset='UTF-8'/>
		<title>Mis pedidos</title>
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
				<a href='./restaurantes/ver/<%= idRestaurante %>'>Ver restaurante</a>
				<a href='./sesion_off'>Cerrar Sesion</a>
			</div> 			  
		</header>	
		<section id='centro'>
			<h1 class='aligndech'>Pedidos </h1>
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
	        <span class='titulo1'><%= pedido.getUsuario().getLogin() %></span>
			<span class='slogan'>Fecha: <%= pedido.getFecha().toString() %></span>
	        <span class='slogan'>Dirección: <%= pedido.getDireccion()%></span>
	        <span class='slogan'>Nº pedido: <%= pedido.getId() %></span>
	        <span class='derecha'>Estado: <%= pedido.getEstado() %></span>
	        </article></a>
	        <% } %>
	        <% } %>
		</section>
	</body>
</html>