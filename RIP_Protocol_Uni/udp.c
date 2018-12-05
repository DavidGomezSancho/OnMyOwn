
#include "udp.h"
#include "ipv4.h"

#include <stdio.h>
#include <stdlib.h>
#include <timerms.h>
#include <netinet/in.h>
#include <string.h>




uint16_t puerto_origen;


//Cabecera udp
struct udp_segment{
	uint16_t source_port; 			//Puerto origen           				(2 bytes)
	uint16_t destination_port; 		//Puerto destino					(2 bytes)
	uint16_t length;			//Longitud de todo el segmento				(2 bytes)
	uint16_t checksum;			//Suma de verificacion(cabecera + datos)		(2 bytes)
	char payload[UDP_PAYLOAD_SIZE];		//Campo Payload						(? bytes)
};
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
int open_udp(uint16_t mi_puerto, char* filename_config, char* filename_routes){
	
	open_ipv4(filename_config, filename_routes);
	puerto_origen = mi_puerto;
	return 1;

}

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
int send_udp(ipv4_addr_t ip_dest, uint16_t puerto_destino, unsigned char *datos, int longitud_datos){
	
	struct udp_segment segmento;
	int enviados;

	segmento.source_port=htons(puerto_origen);
	segmento.destination_port=htons(puerto_destino);
	segmento.length=htons(longitud_datos + UDP_HEADER_SIZE);
	segmento.checksum=0;
	memcpy(segmento.payload, datos, longitud_datos);
	
	enviados = send_ipv4(ip_dest, PROTOCOLO_UDP, (unsigned char *)(&segmento),longitud_datos + UDP_HEADER_SIZE);
	if(enviados == -1){

		return -1;		
	
	}
	return 1;
}



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
int receive_udp(ipv4_addr_t ip_origen, uint16_t * origin_port, unsigned char * buffer_datos, int longitud_datos, long int timeout){
	

	unsigned char buffer[UDP_PAYLOAD_SIZE];
	int recibidos;
	struct udp_segment * segmento_recv = NULL;
	timerms_t timer;
  	timerms_reset(&timer, timeout);
	uint16_t aux_port;

	do{
		long int time_left = timerms_left(&timer);
		recibidos = recv_ipv4(ip_origen, PROTOCOLO_UDP, buffer, UDP_PAYLOAD_SIZE, time_left);
		if(recibidos == -1){
			
			printf("Error al recibir en UDP\n");
			return -1;
			
		}

		if(recibidos == 0){
			
			printf("Se ha agotado el temporizador de UDP\n");
			return 0;
			
		}
		
		segmento_recv = (struct udp_segment *) buffer;
		
		//printf("Recibido\n");
	}while(!(puerto_origen == ntohs(segmento_recv->destination_port)));
		
		memcpy(buffer_datos, segmento_recv->payload, longitud_datos);
		aux_port = ntohs(segmento_recv->source_port);
		memcpy(origin_port, &aux_port, BYTES_PUERTO);
		recibidos = ntohs(segmento_recv->length) - UDP_HEADER_SIZE;
		return recibidos;
	
}


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
int close_udp(){

	close_ipv4();
	return 1;

}

char * udp_get_iface(){

	return ipv4_get_iface();

}
