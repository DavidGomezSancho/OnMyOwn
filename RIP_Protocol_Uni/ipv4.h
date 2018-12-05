#ifndef _IPv4_H
#define _IPv4_H

#include <stdint.h>
#include <timerms.h>
#define IPv4_PROTOCOL_TYPE 0X0800
#define IPv4_ADDR_SIZE 4
#define IPv4_STR_MAX_LENGTH 16

typedef unsigned char ipv4_addr_t [IPv4_ADDR_SIZE];

/* Dirección IPv4 a cero "0.0.0.0" */
extern ipv4_addr_t IPv4_ZERO_ADDR;

/* Logitud máxmima del nombre de un interfaz de red */
#define IFACE_NAME_MAX_LENGTH 32


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
void ipv4_addr_str ( ipv4_addr_t addr, char* str );


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
int ipv4_str_addr ( char* str, ipv4_addr_t addr );


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
uint16_t ipv4_checksum ( unsigned char * data, int len );


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
int open_ipv4(char* filename_config, char* filename_routes);


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
int close_ipv4();


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
int send_ipv4(ipv4_addr_t destino, uint8_t protocolo_superior, unsigned char * datos, int longitud_datos);


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
int recv_ipv4(ipv4_addr_t origen, uint8_t protocolo_superior,unsigned char * buffer_datos, int longitud_datos,long int timeout);

int comprobar_multicast(ipv4_addr_t ip);

char * ipv4_get_iface();

#endif /* _IPv4_H */
