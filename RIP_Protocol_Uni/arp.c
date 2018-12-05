#include "arp.h"

#include <netinet/in.h>

#define IPv4_PROTOCOL_TYPE 0X0800
#define ARP_MESSAGE_SIZE 28
#define ETHERNET_HARDWARE_TYPE 0x0001
#define ARP_PROTOCOL_TYPE 0x0806
#define ARP_REQUEST 0x0001
#define ARP_ANSWER 0x0002
//#define TIME 1500


// Cabecera de una trama ARP
struct arp_message {
 
  uint16_t hardware_type;        		// Tipo de Hardware 2 bytes				2 byte
  uint16_t protocol_type;			// Tipo de protocolo					2 byte
  uint8_t mac_size;				// Tamaño de la direccion MAC(6 bytes)			1 byte
  uint8_t ip_size;				// Tamaño de la direccion IP(4 bytes)			1 byte
  uint16_t operation_code;			// Codigo de la operacion; 1 Solicitud, 2 Respuesta	2 bytes
  mac_addr_t mac_origin;			// Direccion de la MAC de origen			6 bytes
  ipv4_addr_t ip_origin;			// Direccion de la IP de origen				4 bytes
  mac_addr_t mac_destination;                   // Direccion de la MAC de destino			6 bytes
  ipv4_addr_t ip_destination;			// Direccion de la IP de destino			4 bytes

};

/* arp_resolve (eth_iface_t * iface, ipv4_addr_t dest,ipv4_addr_t origin, mac_addr_t mac);
 *
 * DESCRIPCIÓN: 
 *   Esta función se encarga de optener la direccion MAC a partir de la direccion
 *   una direccion IP
 *
 * PARÁMETROS:
 *   'iface': Manejador de la interfaz ethernet del equipo desde el que se envia
 *            la peticion ARP.
 *   'dest'; Direccion IP del host del cual queremos obtener su direccion MAC.
 * 
 * VALOR DEVUELTO:
 *   Devuelve un 1 en caso de que se haya realizado con exito, ademas de actualizar
 *   el valor de mac con la direccion MAC por la cual hemos preguntado.
 *
 * ERRORES:
 *   La funcion devuelve -1 en caso de que se haya producido algun error. 
 */
int arp_resolve (eth_iface_t * iface, ipv4_addr_t dest,ipv4_addr_t origin, mac_addr_t mac){
	
	struct arp_message arp_request;
	
	arp_request.hardware_type = htons(ETHERNET_HARDWARE_TYPE);
	arp_request.protocol_type = htons(IPv4_PROTOCOL_TYPE);
	arp_request.mac_size = MAC_ADDR_SIZE;                       	//eth.h
	arp_request.ip_size = IPv4_ADDR_SIZE;				//ipv4.h
	arp_request.operation_code = htons(ARP_REQUEST);
	eth_getaddr(iface, arp_request.mac_origin);
	memcpy(arp_request.ip_origin,origin,IPv4_ADDR_SIZE);
	memset(arp_request.mac_destination,0x00,MAC_ADDR_SIZE);
	memcpy(arp_request.ip_destination, dest, IPv4_ADDR_SIZE);
	
	
	eth_send (iface, MAC_BCAST_ADDR, ARP_PROTOCOL_TYPE, (unsigned char *)(&arp_request), ARP_MESSAGE_SIZE );
	
	struct arp_message * arp_answer = NULL;
	//long int timer=1500;
	mac_addr_t destino_mac;
	ipv4_addr_t destino_ip;
	unsigned char buffer[ARP_MESSAGE_SIZE];
	uint16_t op_code_recv;
	mac_addr_t mac_solicitada;
	eth_recv ( iface, destino_mac, ARP_PROTOCOL_TYPE, buffer,  ARP_MESSAGE_SIZE, -1 );
	arp_answer = (struct arp_message *) buffer;
	memcpy(destino_ip,arp_answer->ip_origin,IPv4_ADDR_SIZE);
	op_code_recv= ntohs(arp_answer-> operation_code);
	if(op_code_recv != ARP_ANSWER){
		printf("Error, no se trata de un ARP Request\n");
		return -1;

	}	
	
	if ((memcmp(destino_ip,dest, IPv4_ADDR_SIZE))!=0){
	    	printf("Error, la direccion destino no coincide\n");
	    	return -1;

	}
	
	memcpy(mac_solicitada,arp_answer->mac_origin,MAC_ADDR_SIZE);
	memcpy(mac, mac_solicitada, MAC_ADDR_SIZE);
	char ip_solicitada_str[IPv4_ADDR_SIZE];
	char mac_solicitada_str[MAC_STR_LENGTH];
	mac_addr_str(mac_solicitada, mac_solicitada_str);
	ipv4_addr_str(dest, ip_solicitada_str);
	printf("%s -> %s\n", ip_solicitada_str, mac_solicitada_str);
	return 1;	
}
