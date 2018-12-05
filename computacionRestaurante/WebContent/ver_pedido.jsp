<!-- Esta vista corresponde al punto 4 del apartado 5 del enunciado -->

<!-- NECESARIO:  -->
<!-- Rellenar datos -->
<!-- Implementar envío de WEBSOCKET -->


<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import ='comeycalla.modelo.*' %>
<% 
	if(session.getAttribute("usuario")==null){ 
		response.sendRedirect("./index.html?entrada=denegada");
    }

	Pedido pedido = (Pedido)request.getAttribute("pedido");
	Usuario user = (Usuario)session.getAttribute("usuario");
	java.util.List<PedidoProductos> pedidoproductos = (java.util.List<PedidoProductos>) request.getAttribute("pedproductos");
	boolean gestor = user.isRestaurante() && pedido.getRestaurante().getUsuario().getId()==user.getId();
	
	String tipo;
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
<!doctype html>

<html lang='es-ES'>
	<head>

		<meta charset='UTF-8'/>
		<title>Ver pedido</title>
		<style type='text/css'>
			@import url('../../css/style.css');
		</style>
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
		<div>
			<h1>Pedido Nº: </h1><h2><%= pedido.getId() %></h2>
		</div>
		<div class="detalles_pedido">
			<table class="restaurante">
				<tr >
					<td >
						Dirección de entrega:
					</td>
					<td>
						<%= pedido.getDireccion() %>  
					</td>
				</tr>
				<tr>
					<td >
						Nombre cliente:
					</td>
					<td>
						<%= pedido.getUsuario().getLogin() %>
					</td>
				</tr>
				<tr>
					<td>
						Email cliente:
					</td>
					<td>
						<%= pedido.getUsuario().getEmail() %>
					</td>
				</tr>	
				<tr>
					<td>
						Teléfono:
					</td>
					<td>
						<%= pedido.getUsuario().getTelefono() %> 
					</td>
				</tr>
				<tr>
					<td>
						Fecha:
					</td>
					<td>
						<%= pedido.getFecha().toString() %> 
					</td>
				</tr>
				<tr>
					<td>
						Estado:
					</td>
					<td id="estadoTXT">
					<%= pedido.getEstado() %>
					
					</td>
					<td >
						<% if(gestor){ %>
					<!-- Aqui se emplea websocket -->
					<!-- Solo aparece en la vista del gestor del restaurante -->
						<select name="estado" id="estado" class="input_estado">
							<option value="">Cambiar Estado</option>
							<option value="pendiente">Pendiente</option>
							<option value="preparando">Preparando</option>
							<option value="enviando">Enviando</option>
							<option value="entregado">Entregado</option>
						</select>
					<% } %>
					</td>
				</tr>					
			</table>
		</div>
		<div >
			<h2><%= pedido.getRestaurante().getNombre() %></h2>
			<h1><div ></div></h1>
			<img src='../../img/<%= pedido.getRestaurante().getId() %>.jpg' width='150' height='150' id="img"/>
		</div>
		<div class="caracRestaurante">
			<table class='restaurante'>						
					<tr >
						<td >
							Tipo:
						</td>
						<td>
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
							<%= pedido.getRestaurante().getDireccion() %>
						</td>
						<td>
								
						</td>
					</tr>
					<tr>
						<td>
							Código postal:
						</td>
						<td>
							<%= pedido.getRestaurante().getPostal() %> 
						</td>
						<td>
								
						</td>
					</tr>	
					<tr>
						<td>
							Teléfono:
						</td>
						<td>
							<%= pedido.getRestaurante().getTelefono() %>
						</td>
						<td>
								
						</td>
					</tr>					
				</table>								
		</div>
		
		<div class="cartaRestaurante">
			<h2 >Carta</h2>
			<table class='restaurante'>
			<% 	
				for(PedidoProductos pedidoproducto : pedidoproductos ){ %>
				<tr >
					<td >
						<%= pedidoproducto.getProducto().getNombre() %>
					</td>
					<td class="descripcion_td">
						<%= pedidoproducto.getProducto().getDescripcion() %>
					</td>
					<td>
						<%= pedidoproducto.getProducto().getPrecio() %>€
					</td>
					<td >
						x<%= pedidoproducto.getCantidad() %>
					</td>
					
				</tr>
			<% } %>
				
			</table>
		</div>
		</div>
		<script src="../../js/jquery-3.3.1.min.js"></script>
		<script type="text/javascript">
		<% if(gestor){ %>
		var socket = new WebSocket("ws://localhost:8080/computacionRestaurante/cambiar_estado");
		
		$('select#estado').on('change',function(){
		    var valor = $(this).val();
		    if (valor!="")
		    	enviar_estado(<%= pedido.getId() %>, valor);
		});
		
		function enviar_estado(val_id, estado_string){
			var msg = {id:val_id, estado:estado_string};
			socket.send(JSON.stringify(msg));
			$("#estadoTXT").html(estado_string);
		}
		<% }else{ %>
		var socket = new WebSocket("ws://localhost:8080/computacionRestaurante/consultar_estado");
		
		socket.onopen = function(){
			enviar_id("<%= pedido.getId() %>");
		}
		
		socket.onmessage = function(event){
			var msg = JSON.parse(event.data);
			$("#estadoTXT").html(msg.estado);
		}
		
		function enviar_id(val_id){
			var msg = {id:val_id};
			socket.send(JSON.stringify(msg));
		}
		<% } %>
		</script>
	</body>
</html>