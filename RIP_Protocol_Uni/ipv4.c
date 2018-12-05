#include "ipv4.h"
#include "eth.h"
#include "ipv4_config.h"
#include "ipv4_route_table.h"
#include "arp.h"

#include <stdio.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <string.h>

#define IP_HEADER_SIZE 20
#define IP_VERSION_HEADER 0x45
#define IP_TOS_RUTINE_SEND 0x00
#define IP_FLAG_FRAGMENT 0x0000
#define ID_NUESTRO 0x0024
#define IP_TIME_TO_LIVE 64

char ifname[IFACE_NAME_MAX_LENGTH];
ipv4_addr_t addr;
ipv4_addr_t netmask;
eth_iface_t * iface = NULL;
ipv4_route_table_t * route_table = NULL;


/* Dirección IPv4 a cero: "0.0.0.0" */
ipv4_addr_t IPv4_ZERO_ADDR = { 0, 0, 0, 0 };


//Cabecera de datagrama IP
struct ipv4_datagram{

uint8_t version_cabecera_size;		//Parametro que define junto la version y el tamaño cabecera		(1 bytes)
uint8_t type_of_service;		//Tipo de servicio segun la calidad (prioridad)				(1 bytes)
uint16_t datagram_size;			//Longitud de la cabecera + datos					(2 bytes)
uint16_t id;				//Identificador								(2 bytes)
uint16_t flag_fragment_position;	//Parametro que define Flags y Posicion de Fragment			(2 bytes)
uint8_t ttl;				//Tiempo de Vida							(1 bytes)
uint8_t protocol;			//Protocolo de las capas superiores					(1 bytes)
uint16_t checksum;			//Suma de Control de Cabecera						(2 bytes)
ipv4_addr_t ip_origin;			//Direccion IP de origen						(4 bytes)
ipv4_addr_t ip_destination;		//Direccion IP de destino						(4 bytes)
char payload[ETH_MTU];			//Campo con los datos a enviar						(? bytes)

};

/* void ipv4_addr_str ( ipv4_addr_t addr, char* str );
 *
 * DESCRIPCIÓN:
 *   Esta función genera una cadena de texto que representa la dirección IPv4
 *   indicada.
 *
 * PARÁMETROS:
 *   'addr': La dirección IP que se quiere representar textualente.
 *    'str': Memoria donde se desea almacenar la cadena de texto generada.
 *           Deben reservarse al menos 'IPv4_STR_MAX_LENGTH' bytes.
 */
void ipv4_addr_str ( ipv4_addr_t addr, char* str )
{
  if (str != NULL) {
    sprintf(str, "%d.%d.%d.%d",
            addr[0], addr[1], addr[2], addr[3]);
  }
}


/* int ipv4_str_addr ( char* str, ipv4_addr_t addr );
 *
 * DESCRIPCIÓN:
 *   Esta función analiza una cadena de texto en busca de una dirección IPv4.
 *
 * PARÁMETROS:
 *    'str': La cadena de texto que se desea procesar.
 *   'addr': Memoria donde se almacena la dirección IPv4 encontrada.
 *
 * VALOR DEVUELTO:
 *   Se devuelve 0 si la cadena de texto representaba una dirección IPv4.
 *
 * ERRORES:
 *   La función devuelve -1 si la cadena de texto no representaba una
 *   dirección IPv4.
 */
int ipv4_str_addr ( char* str, ipv4_addr_t addr )
{
  int err = -1;

  if (str != NULL) {
    unsigned int addr_int[IPv4_ADDR_SIZE];
    int len = sscanf(str, "%d.%d.%d.%d", 
                     &addr_int[0], &addr_int[1], 
                     &addr_int[2], &addr_int[3]);

    if (len == IPv4_ADDR_SIZE) {
      int i;
      for (i=0; i<IPv4_ADDR_SIZE; i++) {
        addr[i] = (unsigned char) addr_int[i];
      }
      
      err = 0;
    }
  }
  
  return err;
}


/*
 * uint16_t ipv4_checksum ( unsigned char * data, int len )
 *
 * DESCRIPCIÓN:
 *   Esta función calcula el checksum IP de los datos especificados.
 *
 * PARÁMETROS:
 *   'data': Puntero a los datos sobre los que se calcula el checksum.
 *    'len': Longitud en bytes de los datos.
 *
 * VALOR DEVUELTO:
 *   El valor del checksum calculado.
 */
uint16_t ipv4_checksum ( unsigned char * data, int len )
{
  int i;
  uint16_t word16;
  unsigned int sum = 0;
    
  /* Make 16 bit words out of every two adjacent 8 bit words in the packet
   * and add them up */
  for (i=0; i<len; i=i+2) {
    word16 = ((data[i] << 8) & 0xFF00) + (data[i+1] & 0x00FF);
    sum = sum + (unsigned int) word16;	
  }

  /* Take only 16 bits out of the 32 bit sum and add up the carries */
  while (sum >> 16) {
    sum = (sum & 0xFFFF) + (sum >> 16);
  }

  /* One's complement the result */
  sum = ~sum;

  return (uint16_t) sum;
}


/* open_ipv4(char* filename_config, char* filename_routes);
 *
 * DESCRIPCIÓN: 
 *   Esta funcion se encarga de cargar los archivos de configuracion y tabla de
 *   rutas asi como crear la tabla de rutas.
 *
 * PARÁMETROS:
 *   'filename_config': Archivo de configuracion con la direccion IP, la interfaz
 *                      y la subred.
 *   'filename_routes'; Archivo con las rutas segun una direccion IP y su mascara.
 * 
 * VALOR DEVUELTO:
 *   Devuelve un 1 en caso de abrir todo correctamente, ademas obtenemos nuestra
 *   direccion IP, mascara de subred, tabla de rutas y controlador de interfaz.
 *
 * ERRORES:
 *   La funcion devuelve -1 en caso de que se haya producido algun error. 
 */
int open_ipv4(char* filename_config, char* filename_routes){
	if((ipv4_config_read ( filename_config ,ifname, addr, netmask)) != 0){
		return -1;
	}
	iface = eth_open (ifname);
	route_table = ipv4_route_table_create();
	if(route_table == NULL){
		return -1;
	}
	if(ipv4_route_table_read ( filename_routes, route_table) == -1){
		return -1;
	}
	printf("Abierto\n");	
	return 1;
}



/* close_ipv4();
 *
 * DESCRIPCIÓN: 
 *   Esta funcion se encarga de cerrar la interfaz a traves del controlador de esta,
 *   ademas debe liberar la tabla de rutas que construimos en el open_ipv4.
 *
 * PARÁMETROS:
 *
 * 
 * VALOR DEVUELTO:
 *   Devuelve un 1 en caso de que cerrar la interfaz y liberar la memoria se haga de
 *   forma correcta..
 *
 * ERRORES:
 *   La funcion devuelve -1 en caso de que se haya producido algun error. 
 */
int close_ipv4(){
	
	if((eth_close (iface))!=0){
		
		printf("Error al cerrar la interfaz\n");
		return -1;

	}

	ipv4_route_table_free ( route_table );
	printf("Cerrado\n");	
	return 1;
}


/* send_ipv4(ipv4_addr_t destino, uint8_t protocolo_superior, unsigned char * datos, int longitud_datos);
 *
 * DESCRIPCIÓN: 
 *   Esta funcion se encarga de enviar los datos recibidos encapsulandolos en un
 *   datagrama IP.
 *
 * PARÁMETROS:
 *   'destino': Direccion IP donde debemos enviar los datos.
 *   'protocolo_superior'; Numero que representa al protocolo de la capa superior
 *                         a IP al cual pertenecen los datos enviados
 *   'datos': Son los datos que vamos a enviar encapsulados en el datagrama IP.
 *   'longitud_datos': El valor del tamaño del buffer de datos a enviar.
 * VALOR DEVUELTO:
 *   Devuelve un 1 en caso de realizar el envio de forma correcta.
 *
 * ERRORES:
 *   La funcion devuelve -1 en caso de que se haya producido algun error. 
 */
int send_ipv4(ipv4_addr_t destino, uint8_t protocolo_superior, unsigned char * datos, int longitud_datos){


	int enviados;
	mac_addr_t mac;
	ipv4_route_t * route = NULL;
	struct ipv4_datagram ip_send;
	ip_send.version_cabecera_size = IP_VERSION_HEADER;
	ip_send.type_of_service = IP_TOS_RUTINE_SEND;
	ip_send.datagram_size = htons(IP_HEADER_SIZE + longitud_datos);
	ip_send.id=htons(ID_NUESTRO);
	ip_send.flag_fragment_position = htons(IP_FLAG_FRAGMENT);
	ip_send.ttl = IP_TIME_TO_LIVE;
	ip_send.protocol = protocolo_superior;
	ip_send.checksum = 0x0000;
	memcpy(ip_send.ip_origin, addr, IPv4_ADDR_SIZE);
	memcpy(ip_send.ip_destination, destino, IPv4_ADDR_SIZE);
	
	memcpy(ip_send.payload, datos, longitud_datos);
	

	ip_send.checksum = htons(ipv4_checksum ((unsigned char *)&ip_send,  IP_HEADER_SIZE));

	route = ipv4_route_table_lookup ( route_table, destino );			
	if(memcmp(route -> gateway_addr,IPv4_ZERO_ADDR, IPv4_ADDR_SIZE) == 0){
		//printf("Direccion de destino en la misma subred\n");
		if((arp_resolve (iface, destino,addr, mac)) != 1){

			return -1;

		}

		enviados = eth_send (iface, mac, IPv4_PROTOCOL_TYPE, (unsigned char *)(&ip_send), IP_HEADER_SIZE + longitud_datos);	
		if(enviados != IP_HEADER_SIZE + longitud_datos){

			printf("Error al enviar datagrama\n");
			return -1;

		}
		printf("Enviado\n");			
		return 1;
	
	}else{
		//printf("Direccion de destino otra subred\n");
		if((arp_resolve (iface, route->gateway_addr,addr, mac)) != 1){

			return -1;

		}

		enviados = eth_send (iface, mac, IPv4_PROTOCOL_TYPE, (unsigned char *)(&ip_send), IP_HEADER_SIZE + longitud_datos);	
		if(enviados != IP_HEADER_SIZE + longitud_datos){
				
			printf("Error al enviar datagrama\n");
			return -1;

		}
		printf("Enviado\n");
		return 1;
		
	}
}



/* recv_ipv4(ipv4_addr_t origen, uint8_t protocolo_superior,unsigned char * buffer_datos, int longitud_datos,long int timeout);
 *
 * DESCRIPCIÓN: 
 *   Esta funcion se encarga de recibir el buffer de informacion y obtener de el 
 *   los datos quitando la cabecera IP.
 *
 * PARÁMETROS:
 *   'protocolo_superior'; Numero que representa al protocolo de la capa superior
 *                         a IP al cual pertenecen los datos recibidos.
 *   'buffer_datos': Son los datos que hemos recibido.
 *   'longitud_datos': El valor del tamaño del buffer de datos recibido.
 *   'timeout': Valor del temporizador.
 * VALOR DEVUELTO:
 *   Devuelve la direccion IP de la cual recibimos el flujo de datos y devuelve
 *   el tamaño del payload que se ha encapsulado en el datagrama IP que hemos
 *   recibido en el buffer. 
 *
 * ERRORES:
 *   La funcion devuelve -1 en caso de que se haya producido algun error.
 *   Devuelve 0 en caso de que el temporizador haya llegado a 0. 
 */
int recv_ipv4(ipv4_addr_t origen, uint8_t protocolo_superior,unsigned char * buffer_datos, int longitud_datos,long int timeout){
	
	mac_addr_t destino_mac;
	timerms_t timer;
	int recibidos;
	int multicast = 0;
	int datos_size;
	struct ipv4_datagram * ip_recv = NULL;
	unsigned char buffer[ETH_MTU];
	timerms_reset(&timer, timeout);
	do{
		printf("Entrando en recibir ip\n");
		long int time_left = timerms_left(&timer);
		recibidos = eth_recv (iface, destino_mac, IPv4_PROTOCOL_TYPE, buffer, ETH_MTU, time_left);
		if(recibidos < 0){

			printf("Datos no recibidos\n");
			return -1;


		}
		if(recibidos == 0){
			return 0;

		}
		ip_recv = (struct ipv4_datagram *) buffer;
		printf("Recibido\n");
		multicast = comprobar_multicast(ip_recv->ip_destination);

	} while(!((multicast || (memcmp(ip_recv->ip_destination,addr, IPv4_ADDR_SIZE) == 0)) && (ip_recv->protocol) == protocolo_superior));
	memcpy(buffer_datos, ip_recv->payload,longitud_datos);
	datos_size = (ntohs(ip_recv->datagram_size) - IP_HEADER_SIZE);
	memcpy(origen, ip_recv->ip_origin, IPv4_ADDR_SIZE);
	
	/*
	char recibido[datos_size];
	memcpy(recibido, ip_recv->payload, datos_size);
	printf("Datos recibidos: %s\n",recibido);
	*/
	return datos_size;
}

int comprobar_multicast(ipv4_addr_t ip){

	if((ip[0] > 223) && (ip[0] < 240)){
		printf("Direccion multicast\n");
		return 1;
	}
	return 0;

}


char * ipv4_get_iface(){

	return eth_getname(iface);

}
