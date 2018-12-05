<!-- Esta vista corresponde al punto 3 del apartado 5 del enunciado -->

<!-- NECESARIO:  -->
<!-- Hacer servlets cambiar_precio, eliminar_producto y hacer_pedido -->



<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import ='comeycalla.modelo.*' %>
<% 
	Usuario user = (Usuario)session.getAttribute("usuario");
	if(user==null){ 
    	response.sendRedirect("./index.html?entrada=denegada");
    }

	Restaurante restaurante = (Restaurante) request.getAttribute("restaurante");
	
	Carta carta = (Carta) request.getAttribute("carta");
	
	java.util.List<Producto> productos = carta.getProductos();
	
	boolean gestor = user.isRestaurante() && restaurante.getUsuario().getId()==user.getId();
	String tipo="";
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

<!doctype html>

<html lang='es-ES'>
	<head>

		<meta charset='UTF-8'/>
		<title>Ver restaurante</title>
		<style type='text/css'>
			@import url('../../css/style.css');
		</style>
		<script src="../../js/jquery-3.3.1.min.js"></script>
	</head>
	<body>
		<header id='cabecera'>
			<div id='logo'>
				<a href='../../mis_pedidos'>ComeYCalla
				<span class='slogan'>y que aproveche</span></a>
			</div>			
			<div id='panel' class='aligndech'>
				<a href='../../sesion_off'>Cerrar Sesion</a>
			</div> 			  
		</header>	
		<div class="main_layout">
		<div class="fotoRestaurante">
			<h1 ><%= restaurante.getNombre() %> </h1>
			<h1></h1>
			<img src='../../img/<%= restaurante.getId() %>.jpg' width='150' height='150' id="img"/>
		</div>
		<div class="caracRestaurante">
			<table class='restaurante'>						
					<tr >
						<td >
							Tipo:
						</td>
						<td >
							<%= tipo %>  
						</td>
						<td>
								
						</td>
					</tr>
					<tr>
						<td >
							Dirección:
						</td>
						<td>
							<%= restaurante.getDireccion() %> 
						</td>
						<td>
								
						</td>
					</tr>
					<tr>
						<td>
							Código postal:
						</td>
						<td>
							<%= restaurante.getPostal() %>  
						</td>
						<td>
								
						</td>
					</tr>	
					<tr>
						<td>
							Teléfono:
						</td>
						<td >
							<%= restaurante.getTelefono() %>  
						</td>
						<td>
								
						</td>
					</tr>					
				</table>								
		</div>
		
		<div class="cartaRestaurante">
			<h2 >Carta <%= carta.getNombre() %></h2>
			<% if(gestor){ %>
			<form action="./anyadir_producto" method="post">
<!-- 			Es necesario implementar servlet anyadir, que añade un nuevo producto a la carta -->
				<input type="hidden" name="id_carta" value="<%= carta.getId() %>">
				<input type="hidden" name="id_restaurante" value="<%= restaurante.getId() %>">
				<input class="boton_input" type="button" onclick="add_elemento()" value="+" /> Añadir elemento a la carta
				<table class="restaurante" id="anyadir">
				
				</table>
			</form>
			<p>
			<% } %>
			
			
			
				<!-- Vista para gestor solo -->
			<% if(gestor){ %>
			<form action="./" name="editar_producto" method="post" >
				<input type="hidden" name="id_carta" value="<%= carta.getId() %>">
				<input type="hidden" name="id_producto" id="id_producto" value="">
				<input type="hidden" name="id_restaurante" value="<%= restaurante.getId() %>">
				<table class='restaurante' id="carta">
				<% for(Producto producto : productos){ %>
<!-- 					PONER ELEMENTOS DE LA CARTA, productos -->
				<tr >
					<td>
						<div class="box_precio"><input class="precio" type="number" placeholder="Precio" name="precio<%= producto.getId() %>" id="precio<%= producto.getId() %>" value="<%= producto.getPrecio() %>"/> €</div>
					</td>
					<td >
						<%= producto.getNombre() %>
					</td>
					<td>
						<%= producto.getDescripcion() %>
					</td>
					<td>
					<div class="caja_num">
						<input class="boton_input" onclick="cambiar_precio('<%= producto.getId() %>')" type="button" value="+" /> Cambiar precio
					</div>
					</td>
					<td>
					<div class="caja_num">
						<input class="boton_input" onclick="eliminar('<%= producto.getId() %>')" type="button" value="-" /> Eliminar
					</div>
					</td>
				</tr>
				<% } %>
				</table>
			</form>
			<% }else{ %>
			<form action="./hacer_pedido" method="post">
				<input type="hidden" name="id_carta" value="<%= carta.getId() %>">
				<input type="hidden" name="id_restaurante" value="<%= restaurante.getId() %>">
				<table class='restaurante' id="carta">
				<% for(Producto producto : productos){ %>
				<tr>
					<td>
						<%= producto.getPrecio() %>€
					</td>
					<td >
						<%= producto.getNombre() %>
					</td>
					<td>
						<%= producto.getDescripcion() %> 
					</td>
					<td>
						<div class="caja_num">
							<input class="boton_input" type="button" onclick="restar_num('<%= producto.getId() %>')" value="-" class="button" /> 
							<input class="input" type="number" name="<%= producto.getId() %>" min="0" value="0"/>
							<input class="boton_input" type="button" onclick="sumar_num('<%= producto.getId() %>')" value="+" class="button" />
						</div>
					</td>
				</tr>
				<% } %>
			</table>
			<!-- Vista para cliente solo -->
			<table class='restaurante'>
				<tr>
					<td>
						Dirección:
					</td>
					<td>
						<input id='direccion_cliente' name='direccion_cliente' type='text' 
										placeholder='Dirección de entrega...' required/>
					</td>
				</tr>
				<tr>
					<td>
						Código postal:
					</td>
					<td>
						<input id='cod_postal' name='cod_postal' type='number' 
										placeholder='Código postal...' required/>
					</td>
				</tr>
			</table>
			<p><input type="submit" class="boton_enviar" value="Cargar pedido" />
			</form>
			<% } %>
		</div>
		</div>
		
		<script type="text/javascript">
		<% if(gestor==false){ %>
			function sumar_num(nombre){
				var input = document.getElementsByName(nombre);
				if(isNaN(parseInt(input[0].value))){
					input[0].value=1;
				}else{
					input[0].value=parseInt(input[0].value)+1;
				}
				
			}
			
			function restar_num(nombre){
				var input = document.getElementsByName(nombre);
				if(isNaN(parseInt(input[0].value))){
					input[0].value=0;
				}else{
					input[0].value=parseInt(input[0].value)-1;
				}
				
			}
			
		<% }else{ %>
			
			function add_elemento(){
				var comprobar = document.getElementById("nombre");
				if(comprobar==null){
					var current = "<tr>";
					current+="<td>";
					current+='<div class="box_precio"><input class="precio" type="number" placeholder="Precio" name="precio" id="precio"/> €</div>';
					current+="</td>";
					current+="<td >";
					current+='<input class="nombre" type="text" placeholder="Nombre" name="nombre" id="nombre"/>';
					current+="</td>";
					current+="<td>";
					current+='<textarea rows="3" cols="30" placeholder="Descripción" name="descripcion" id="descripcion"></textarea>';
					current+="</td>";
					current+="<td>";
					current+='<div class="caja_num">';
					current+='<input class="boton_input" type="submit" value="+" /> Guardar';
					current+="</div>";
					current+="</td>";
					current+="</tr>";
					$("#anyadir").prepend(current);
				}
			}
			
			function cambiar_precio(id_producto){
				var formulario = document.forms["editar_producto"];
				formulario.action="cambiar_precio";
				$("#id_producto").val(id_producto);
				formulario.submit();
			}
			
			function eliminar(id_producto){
				var formulario = document.forms["editar_producto"];
				formulario.action="eliminar_producto";
				$("#id_producto").val(id_producto);
				formulario.submit();
			}
		<% } %>
		</script>
	</body>
</html>