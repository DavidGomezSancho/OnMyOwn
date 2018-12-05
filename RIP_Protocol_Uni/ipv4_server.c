#include "ipv4.h"
#include "eth.h"

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <libgen.h>
#include <netinet/in.h>
#include <string.h>
#define TAM_DATOS 10
#define PROTOCOLO_SUPERIOR 0x93
#define TIME 6000

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
	char buffer[ETH_MTU];
	int resultado_recv;
	ipv4_addr_t ip_destino;
	char ip_recv_str[IPv4_STR_MAX_LENGTH];


	if((open_ipv4("ipv4_config_server.txt", "ipv4_route_table_server.txt")) == -1){

		printf("Error en el Servidor IP al inicializar\n");
		return -1;

	}
	while(1){
		resultado_recv = recv_ipv4(ip_destino, PROTOCOLO_SUPERIOR,(unsigned char *)buffer, ETH_MTU,TIME);
		if(resultado_recv == -1){
			
			printf("Error en el Servidor IP al recibir\n");
			return -1;
		
		}
		if(resultado_recv == 0){
			
			printf("Se ha agotado el temporizador del receive\n");
			
		}
		if(resultado_recv > 0){
			
			ipv4_addr_str(ip_destino,ip_recv_str);
			printf("Bytes recibidos: %i de %s\n", resultado_recv,ip_recv_str);

		}	

	}
}
