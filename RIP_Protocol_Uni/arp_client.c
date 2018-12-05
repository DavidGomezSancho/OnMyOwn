#include "arp.h"

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <libgen.h>
#include <netinet/in.h>

#include <rawnet.h>

int main ( int argc, char *argv[])
{
	if ((argc < 2) || (argc >3)) {
    		printf("Uso: %s <iface> <ip> \n");
    		printf("       <iface>: Nombre de la interfaz Ethernet\n");
    		printf("        <ip>: Campo IP de destino\n");
    		exit(-1);
  	}
	
	char * iface_name = argv[1];
	char * cadena_ip = argv [2];
	ipv4_addr_t ip_destino;
	mac_addr_t mac_destino;

	eth_iface_t * eth_iface = eth_open(iface_name);

	mac_addr_t client_addr;
  	eth_getaddr(eth_iface, client_addr);  
  	char client_addr_str[MAC_STR_LENGTH];
  	mac_addr_str(client_addr, client_addr_str);
  	printf("Abriendo interfaz Ethernet %s. Dirección MAC: %s\n", 
         iface_name, client_addr_str);

	ipv4_str_addr(cadena_ip, ip_destino);
	
	int resultado = arp_resolve (eth_iface, ip_destino, mac_destino);
	if(resultado==1){
	
	return 1;

	}
	if(resultado == -1){

	return -1;

	}
}
