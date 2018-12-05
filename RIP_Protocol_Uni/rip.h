#ifndef _RIP_H
#define _RIP_H

#define TAM_ENTRADA_RIP 20
#define TAM_RIP_HEADER 4
#define RIP_REQUEST 1
#define RIP_RESPONSE 2
#define RIP_VERSION 2
#define RIP_TIMEOUT 180000
#include "eth.h"
#include "ipv4.h"

#include <netinet/in.h>


struct rip_entrada{

	uint16_t addr_family;			//2 bytes
	uint16_t route_tag;			//2 bytes
	ipv4_addr_t ip_address;			//4 bytes
	ipv4_addr_t netmask;			//4 bytes
	ipv4_addr_t next_hop;			//4 bytes
	uint32_t metric;			//4 bytes

};


struct rip_message{

	uint8_t command;			//1 byte
	uint8_t version;			//1 byte
	uint16_t zeros;				//2 byte
	struct rip_entrada entradas[25];	//20*num entradas

};



#endif /* _RIP_H */
