#ifndef _ARP_H
#define _ARP_H

#include "eth.h"
#include "ipv4.h"
#include <stdio.h>
#include <string.h>


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
int arp_resolve (eth_iface_t * iface, ipv4_addr_t dest,ipv4_addr_t origin, mac_addr_t mac);


#endif /* _ARP_H */