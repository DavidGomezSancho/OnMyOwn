#include "udp.h"

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <libgen.h>
#include <string.h>
#include "rip.h"

#define TIME 6000
#define PUERTO 520
#define PUERTO_CLIENTE 1520

#include <rawnet.h>

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
	if ((argc <1) || (argc > 2) || (argv[1] == NULL)) {
		printf("Argumentos equivocados\n");    		
		printf("Uso: <ip> \n");
    		printf("        <ip>: Campo IP de destino\n");
    		exit(-1);	
	}
	
	printf("Nombre : %s\n", argv[0]);
	printf("Direccion IP : %s\n", argv[1]);
	char * cadena_ip = argv [1];
	ipv4_addr_t ip_destino;
	ipv4_addr_t ip_recv;
	ipv4_str_addr(cadena_ip, ip_destino);
	char buffer[UDP_PAYLOAD_SIZE];
	uint16_t puerto_origen;
	int cantidad_datos;
	struct rip_message *msg_recibido;

	struct rip_message prueba;
	struct rip_entrada primera;
	prueba.command = RIP_REQUEST;
	prueba.version = RIP_VERSION;
	prueba.zeros = 0;
	primera.addr_family = 0;
	primera.route_tag = 0;
	memset(primera.ip_address, 0, IPv4_ADDR_SIZE);
	memset(primera.netmask, 0, IPv4_ADDR_SIZE);
	memset(primera.next_hop, 0, IPv4_ADDR_SIZE);
	primera.metric = htonl(16);
	prueba.entradas[0] = primera;
	int tam_prueba = TAM_RIP_HEADER + TAM_ENTRADA_RIP;
	
	mostrar_mensaje(&prueba,24);
	if((open_udp(PUERTO_CLIENTE,"ipv4_config_client.txt", "ipv4_route_table_client.txt")) == -1){

		printf("Error en el Cliente UDP al inicializar\n");
		return -1;

	}

	
	if((send_udp(ip_destino, PUERTO, (unsigned char *) &prueba, tam_prueba)) == -1){

		printf("Error en el cliente al enviar\n");
		return -1;

	}
	
	cantidad_datos = (receive_udp(ip_recv,&(puerto_origen),(unsigned char *) buffer,UDP_PAYLOAD_SIZE,-1));
	if(cantidad_datos == -1){
	  
		printf("Error en el cliente al recibir del servidor\n");
		return -1;
	    
	}

	
	printf("Recibido Response\n");
	printf("Cantidad datos recibidos: %d\n",cantidad_datos);
	msg_recibido = (struct rip_message *)buffer;
	mostrar_mensaje(msg_recibido,cantidad_datos);
	

	if(close_ipv4() == -1){

		printf("Error en el cliente al cerrar\n");
		return -1;	
	
	}	
	
	return 1;	

}
