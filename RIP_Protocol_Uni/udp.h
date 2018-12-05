#ifndef _UDP_H
#define _UDP_H

#include "ipv4.h"
#include <stdint.h>

#define UDP_HEADER_SIZE 8
#define UDP_PAYLOAD_SIZE 1472      //Cabeceras IP + UDP = 28
#define PROTOCOLO_UDP 17
#define BYTES_PUERTO 2

/*
* DESCRIPCION:
*  Inicializa udp y llama a ip.
* 
* PARÁMETROS:
*  'mi_puerto': Indica el valor del puerto que utilizamos para 
*    la recepción y transmision de segmentos.
*  '*filename_config': Archivo de configuración ip.
*		'*filemane_routes': Archivo para la tabla de rutas de ip.
* VALOR DEVUELTO:
*  1 si funciona.
*
*/
int open_udp(uint16_t mi_puerto, char* filename_config, char* filename_routes);


/*
*
* DESCRIPCIÓN:
* 	Envía segmento udp.
*
*	PARÁMETROS:
*		'ip_dest': Ip a la que se va a enviar el segmento.
*		'puerto_destino': Puerto al que se va a enviar el segmento.
*		'*datos': Puntero a la informacion que se va a entregar.
*				Reservar previamente.
*		'longitud_datos': Tamanyo de los datos que se pasan como parámetro.
*		
*	VALOR DEVUELTO:
*		1 si se ha enviado con éxito.
*		-1 si ha sido imposible.
*
*/
int send_udp(ipv4_addr_t ip_dest, uint16_t puerto_destino, unsigned char *datos, int longitud_datos);


/*
*
*	DESCRIPCIÓN:
*		Recibe segmento udp del puerto que escuchamos en un tiempo dado.
*	
*	PARÁMETROS:
*		'ip_origen': Ip desde la que recibimos datagramas. Nos los entrega.
*		'*origin_port': Parámetro de salida. Puerto desde el 
*				que nos envian segmentos.
*		'*buffer_datos': Parámetro de salida. Se almacenan 
*			los datos recibidos. Previamente se debe reservar memoria.
*		'longitud_datos': Tamanyo del buffer_datos.
*		'timeout': Tiempo máximo que se espera a que llegue 
*			el segmento requerido.
*
*	VALOR DEVUELTO:
*		Datos recibidos en bytes, sin contar cabeceras.
*		-1 en caso de error.
*		0 si se agota el temporizador
*
*/
int receive_udp(ipv4_addr_t ip_origen, uint16_t * puerto_origen, unsigned char * buffer_datos, int longitud_datos, long int timeout);

/*
*
*	DESCRIPCIÓN:
*		Libera el espacio de memoria reservado previamente por open_udp().
*	
*
*	VALOR DEVUELTO:
*		1 en caso de exito.
*
*/
int close_udp();

char * udp_get_iface();

#endif /* _UDP_H */
