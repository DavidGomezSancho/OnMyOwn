#include "udp.h"
#include "ripv2_route_table.h"
#include "rip.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <libgen.h>
#include <string.h>

#define TIME -1
#define PUERTO 520

#include <rawnet.h>

ripv2_route_table_t *tabla_rip;


/* struct rip_entrada entrada_de_ruta_rip(ripv2_route_t *ruta_rip);
 *
 * DESCRIPCIÓN: 
 *   Con esta funcion obtenemos una entrada RIP a partir de una ruta RIP.
 *
 * PARÁMETROS:
 *   'ruta_rip': Ruta RIP de la que queremos obtener la entrada.
 * 
 * VALOR DEVUELTO:
 *   
 *
 * ERRORES:
 *   
 */
struct rip_entrada entrada_de_ruta_rip(ripv2_route_t *ruta_rip){

	struct rip_entrada entrada;
	entrada.addr_family = htons(2);
	entrada.route_tag = 0;
	memcpy(entrada.ip_address,ruta_rip->addr,4);
	memcpy(entrada.netmask,ruta_rip->subnet_mask,4);
	memcpy(entrada.next_hop,ruta_rip->next_hop,4);
	entrada.metric = htonl(ruta_rip->metrica);
	return entrada;

}

/* void mostrar_entrada(struct rip_entrada *entrada);
 *
 * DESCRIPCIÓN: 
 *   Esta función nos muestra por pantalla una entrada RIP.
 *
 * PARÁMETROS:
 *   'entrada': Entrada de la tabla RIP que queremos mostrar.
 * 
 * VALOR DEVUELTO:
 *   
 *
 * ERRORES:
 *   
 */
void mostrar_entrada(struct rip_entrada *entrada){

	int addr_family = ntohs(entrada->addr_family);
	int route_tag = ntohs(entrada->route_tag);
	char ip_address[IPv4_STR_MAX_LENGTH];
	char netmask[IPv4_STR_MAX_LENGTH];
	char next_hop[IPv4_STR_MAX_LENGTH];
	ipv4_addr_str(entrada->ip_address,ip_address);
	ipv4_addr_str(entrada->netmask,netmask);
	ipv4_addr_str(entrada->next_hop,next_hop);
	long int metric = ntohl(entrada->metric);
	printf("Addr Family: %d\nRoute Tag: %d\nIP Address: %s\nNetmask: %s\nNext Hop: %s\nMetric: %ld\n\n",addr_family,route_tag,ip_address,netmask,next_hop,metric);
}

/* mostrar_mensaje(struct rip_message *mensaje, int tam);
 *
 * DESCRIPCIÓN: 
 *   Esta función nos muestra por pantalla un mensaje RIP.
 *
 * PARÁMETROS:
 *   'mensaje': Mensaje RIP que queremos mostrar.
 * 
 *   'tam'; Tamaño del del mensaje a mostrar.
 * 
 * VALOR DEVUELTO:
 *   
 *
 * ERRORES:
 *   
 */
void mostrar_mensaje(struct rip_message *mensaje, int tam){

	int command = mensaje->command;
	if(command == 1){

		printf("\nRequest Message\n\n");
	
	}
	
	if(command == 2){

		printf("\nResponse Message\n\n");

	}
	tam = tam - 4;
	int i = 0;
	while(tam!=0){
		printf("-- Entrada: %d --\n",i);
		mostrar_entrada(&(mensaje->entradas[i]));
		tam = tam - 20;
		i++;

	}

}

/* int mensaje_response(ipv4_addr_t ip_destino,uint16_t puerto_origen);
 *
 * DESCRIPCIÓN: 
 *   Esta función se encarga enviar el mensaje response a partir de las rutas que
 *   hay guardadas en la tabla 
 *
 * PARÁMETROS:
 *   'ip_destino': Direccion IP de destino al que enviar el mensaje RIP.
 * 
 *   'puerto_origen'; Puerto al que enviamos el mensaje RIP.
 * 
 * VALOR DEVUELTO:
 *   Devuelve un 1 en caso de que se haya realizado con exito.
 *
 * ERRORES:
 *   La funcion devuelve un -1 si ha habido un problema al enviar. 
 */
int mensaje_response(ipv4_addr_t ip_destino,uint16_t puerto_origen){
		struct rip_message mensaje;
		mensaje.command = RIP_RESPONSE;
		mensaje.version = RIP_VERSION;
		mensaje.zeros = 0;
		int resultado_send = -1;
		int contador;
		int num_entradas = 0;
		
		for(contador = 0;contador<25;contador++){

			ripv2_route_t *ruta_aux;
			ruta_aux = ripv2_route_table_get(tabla_rip,contador);
			if(ruta_aux != NULL){

				struct rip_entrada entrada_aux;
				entrada_aux = entrada_de_ruta_rip(ruta_aux);
				memcpy(&(mensaje.entradas[num_entradas]),&entrada_aux,TAM_ENTRADA_RIP);
				mostrar_entrada(&mensaje.entradas[num_entradas]);
				num_entradas ++;
			}
		}
		int tam_datos;
		printf("Numero entradas a enviar: %d\n",num_entradas);
		tam_datos = TAM_RIP_HEADER + (TAM_ENTRADA_RIP * num_entradas);
		printf("Tamaño datos a enviar: %d\n",tam_datos);
		mostrar_mensaje(&mensaje,tam_datos);
		resultado_send = send_udp(ip_destino,puerto_origen,(unsigned char *)&mensaje, tam_datos);
		return resultado_send;
	}

	
/* void revisar_timers();
 *
 * DESCRIPCIÓN: 
 *   Esta función se encarga de eliminar las rutas cuyo timer este a 0
 *
 * PARÁMETROS:
 *   
 * 
 * VALOR DEVUELTO:
 * 
 *
 * ERRORES:
 *   
 */	
void revisar_timers(){

	ripv2_route_t *ruta;
	int i;
	for(i = 0;i<RIPv2_ROUTE_TABLE_SIZE;i++){

		ruta = ripv2_route_table_get(tabla_rip,i);
		if(ruta !=NULL){

			int tiempo = timerms_left(&(ruta->timer));
			if(tiempo == 0){

				printf("Eliminando ruta expirada\n");
				free(ripv2_route_table_remove(tabla_rip,i));

			}
		}
	}
}

/* int main ( int argc, char * argv[] );
 * 
 * DESCRIPCIÓN:
 *   Función principal del programa.
 *
 * PARÁMETROS:
 *   'argc': Número de parámetros pasados al programa por la línea de
 *           comandos, incluyendo el propio nombre del programa.
 *   'argv': Array con los parámetros pasado al programa por línea de
 *           comandos. 
 *           El primer parámetro (argv[0]) es el nombre del programa.
 *
 * VALOR DEVUELTO:
 *   Código de salida del programa.
 */
int main ( int argc, char *argv[])
{
	if ((argc <0) || (argc > 1)) {
		printf("Argumentos equivocados\n");    		
    		exit(-1);	
	}
	
	printf("Nombre : %s\n", argv[0]);
	unsigned char buffer[UDP_PAYLOAD_SIZE];
	int resultado_recv;
	//int resultado_send;
	ipv4_addr_t ip_destino;
	//char ip_recv_str[IPv4_STR_MAX_LENGTH];
	uint16_t puerto_origen;
	tabla_rip = ripv2_route_table_create();
	long int timeout;
	timeout = TIME;	
	if((open_udp(PUERTO,"ipv4_config_server.txt", "ipv4_route_table_server.txt")) == -1){

		printf("Error en el Servidor UDP al inicializar\n");
		return -1;

	}

	int num_entradas = ripv2_route_table_read("rip_route_table.txt",tabla_rip);
	printf("Numero de entradas: %d\n",num_entradas);
	ripv2_route_table_print (tabla_rip);
	while(1){
		timeout = TIME;
		int j;
		int cambio = 0;
		for(j = 0;j<RIPv2_ROUTE_TABLE_SIZE;j++){
			cambio = 0;
			if(tabla_rip->routes[j] != NULL){
				
				long int tiempo_aux = timerms_left(&(tabla_rip->routes[j]->timer));
				//printf("tiempo_aux: %d\n",tiempo_aux);
				//printf("Timeout: %ld\n",timeout);
				if(timeout < 0){
					timeout = tiempo_aux;
					cambio = 1;
				}
				if((timeout > 0) && (cambio == 0)){
					if(timeout > tiempo_aux){
						timeout = tiempo_aux;
						cambio = 1;
					}
				}
			}
		}
		printf("Timeout: %ld\n",timeout);
		resultado_recv = receive_udp(ip_destino, (&puerto_origen),buffer, UDP_PAYLOAD_SIZE, timeout);
		if(resultado_recv == -1){
			
			return -1;
		
		}
		if(resultado_recv == 0){

		revisar_timers();

		}
		
		if(resultado_recv > 0){
			struct rip_message *recibido;
			recibido = (struct rip_message *) buffer;
			if(recibido->command == RIP_REQUEST){

				mensaje_response(ip_destino, puerto_origen);

			}else if(recibido->command == RIP_RESPONSE){

				char * iface = udp_get_iface();
				int  cantidad_entradas = 0;
				cantidad_entradas = (resultado_recv - TAM_RIP_HEADER)/TAM_ENTRADA_RIP;
				int indice;
				for (indice = 0;indice < cantidad_entradas;indice++){

					int metrica = ntohl(recibido->entradas[indice].metric);
					if(metrica < 16){

						metrica ++;

					}else{
					  
						printf("Ruta con metrica 16\n");
					  
					}
					
					int posicion = ripv2_route_table_find(tabla_rip,recibido->entradas[indice].ip_address,recibido->entradas[indice].netmask);
					if((posicion == -1) && (metrica < 16)){
						
						ripv2_route_t *ruta_aux = NULL;
						ruta_aux = ripv2_route_create(recibido->entradas[indice].ip_address,recibido->entradas[indice].netmask,iface,ip_destino,metrica,RIP_TIMEOUT);
						ripv2_route_table_add(tabla_rip,ruta_aux);
						printf("Ruta Añadida\n");
						struct rip_entrada entrada_aux = entrada_de_ruta_rip(ruta_aux); 
						mostrar_entrada(&entrada_aux);
					}else{
						if(metrica != 16){
							ripv2_route_t *ruta_aux = NULL;
							ruta_aux = ripv2_route_table_get(tabla_rip,posicion);
							if(memcmp(ip_destino,ruta_aux->next_hop,IPv4_ADDR_SIZE) == 0){

								printf("Modificando Ruta\n");
								struct rip_entrada entrada_aux = entrada_de_ruta_rip(ruta_aux); 
								mostrar_entrada(&entrada_aux);
								ruta_aux->metrica = metrica;
								timerms_reset(&(ruta_aux->timer),RIP_TIMEOUT);
												
								

							}else{

								if(metrica < ruta_aux->metrica){

									printf("Modificando Ruta\n");
									struct rip_entrada entrada_aux = entrada_de_ruta_rip(ruta_aux); 
									mostrar_entrada(&entrada_aux);
									memcpy(ruta_aux->next_hop,ip_destino,IPv4_ADDR_SIZE);
									ruta_aux->metrica = metrica;
									timerms_reset(&(ruta_aux->timer),RIP_TIMEOUT);

								}
							}						
						}else{
						  
							free(ripv2_route_table_remove(tabla_rip,posicion));
						  
						}
					}
				}
			}
		}
		ripv2_route_table_print (tabla_rip);
	}
}
