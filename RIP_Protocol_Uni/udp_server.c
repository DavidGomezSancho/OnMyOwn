#include "udp.h"

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <libgen.h>
#include <string.h>

#define TIME 6000
#define PUERTO 1515

#include <rawnet.h>


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
	char buffer[UDP_PAYLOAD_SIZE];
	int resultado_recv;
	ipv4_addr_t ip_destino;
	char ip_recv_str[IPv4_STR_MAX_LENGTH];
	uint16_t puerto_origen;

	
	if((open_udp(PUERTO,"ipv4_config_server.txt", "ipv4_route_table_server.txt")) == -1){

		printf("Error en el Servidor UDP al inicializar\n");
		return -1;

	}

	while(1){
		resultado_recv = receive_udp(ip_destino, (&puerto_origen),(unsigned char *) buffer, UDP_PAYLOAD_SIZE, TIME);
		if(resultado_recv == -1){
			
			return -1;
		
		}
		
		if(resultado_recv > 0){
			
			ipv4_addr_str(ip_destino,ip_recv_str);
			printf("Bytes recibidos: %i por el puerto %x de %s\n", resultado_recv,puerto_origen,ip_recv_str);

		}	

	}
}
