#include "udp.h"

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <libgen.h>
#include <string.h>

#define TAM_DATOS 10
#define TIME 6000
#define PUERTO 1515
#define PUERTO_CLIENTE 1516

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
	ipv4_str_addr(cadena_ip, ip_destino);
	char datos[TAM_DATOS];
	strcpy(datos, "aaaaaa");

	if((open_udp(PUERTO_CLIENTE,"ipv4_config_client.txt", "ipv4_route_table_client.txt")) == -1){

		printf("Error en el Cliente UDP al inicializar\n");
		return -1;

	}

	
	if((send_udp(ip_destino, PUERTO, (unsigned char *) datos, TAM_DATOS)) == -1){

		printf("Error en el cliente al enviar\n");
		return -1;

	}

	if(close_ipv4() == -1){

		printf("Error en el cliente al cerrar\n");
		return -1;	
	
	}	
	
	return 1;	

}
