<!-- Esta vista corresponde al punto 2 del apartado 5 del enunciado -->

<!-- NECESARIO:  -->
<!-- TODO  -->

<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import ='comeycalla.modelo.*' %>
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
		<title>Busqueda restaurantes</title>
		<style type='text/css'>
			@import url('../css/style.css');
		</style>
	</head>
	<body>
		<header id='cabecera'>
			<div id='logo'>
				<a href='../mis_pedidos'>ComeYCalla
				<span class='slogan'>y que aproveche</span></a>
			</div>			
			<div id='panel' class='aligndech'>
				<a href='../sesion_off'>Cerrar Sesion</a>
			</div> 			  
		</header>	
		
		<section id='centro'>
			<h1 class='aligndech'>Restaurantes </h1>

				<h1 class='aligndech'>búsqueda </h1><table id='tableavnzd'>

				<%
				
					List<Restaurante> restaurantes = (List<Restaurante>) request.getAttribute("restaurantes");
					String tipo;
					if(restaurantes!=null){
					for (Restaurante restaurante : restaurantes){
 	        			tipo="";
 	        			if(restaurante.isBurguer())
 	        				tipo="burguer";
 	        			if(restaurante.isKebab())
 	        				tipo="kebab";
 	        			if(restaurante.isChino())
 	        				tipo="chino";
 	        			if(restaurante.isIndio())
 	        				tipo="indio";
 	        			if(restaurante.isJapones())
 	        				tipo="japones";
				%>
					<a href='./ver/<%= restaurante.getId() %>'><article>
	        		<span class='titulo1'><%= restaurante.getNombre() %></span>
					<span class='slogan'><%= restaurante.getTelefono() %></span>
	        		<span class='slogan'><%= restaurante.getDireccion() %></span>
	        		<span class='slogan'><%= restaurante.getPostal() %></span>
	        		<span class='derecha'><%= tipo %></span>
	        		</article></a>
	        		
	        	<% 
	        	}
				}
				%>
			
		</section>
		
		
	</body>
</html>