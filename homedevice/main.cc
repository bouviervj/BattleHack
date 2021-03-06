#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <lib/socket/socket.h>

#include <lib/extern/json/include/ArduinoJson.h>

#include "mraa/gpio.h"
#include "mraa/aio.h"

#include <sys/types.h>
#include <sys/socket.h>
#include <sys/ioctl.h>
#include <netinet/in.h>
#include <net/if.h>
#include <arpa/inet.h>
#include <math.h>

#include <unistd.h>

#include <time.h>

mraa_platform_t platform;
mraa_gpio_context gpio, gpio_in = NULL;

int ledstate = 1;

std::string deviceID;
std::string ownerLogin;
std::string name;
std::string type;

int init(){

    mraa_platform_t platform = mraa_get_platform_type();

    char* board_name = mraa_get_platform_name();

    switch (platform) {
        case MRAA_INTEL_GALILEO_GEN1:
            gpio = mraa_gpio_init_raw(3);
            break;
	case MRAA_INTEL_MINNOWBOARD_MAX:
	    // there is no onboard LED that we can flash on the minnowboard max
	    // but on the calamari lure pin 21 is an LED. If you don't have the
	    // lure put an LED on pin 21
	    gpio = mraa_gpio_init(21);
            break;
        default:
            //pin 13 for on board light - 8 for relay
            gpio = mraa_gpio_init(8);
    }

    fprintf(stdout, "Welcome to libmraa\n Version: %s\n Running on %s\n",
        mraa_get_version(), board_name);


    if (gpio == NULL) {
        fprintf(stdout, "Could not initilaize gpio\n");
        return 1;
    }

    // on platforms with physical button use gpio_in
    if (platform == MRAA_INTEL_MINNOWBOARD_MAX) {
        gpio_in = mraa_gpio_init(14);
	if (gpio_in != NULL) {
            mraa_gpio_dir(gpio_in, MRAA_GPIO_IN);
            // S1 on minnowboardmax's calamari lure maps to pin 14, SW1 != S1
            fprintf(stdout, "Press and hold S1 to stop, Press SW1 to shutdown!\n");
        }
    }

    mraa_gpio_dir(gpio, MRAA_GPIO_OUT);
    
    mraa_gpio_write(gpio, 0); 
	
}

std::string getDeviceName(){
 return deviceID;
}

int waitingTime = 0;
time_t beginCounter = 0;
int count = 0;

void* staticWaitThread(void* pt){

    beginCounter = time(NULL);	

    ledstate = 1;        
    mraa_gpio_write(gpio, ledstate); 
    
    count = waitingTime;
    while (count>0) {
    	sleep(1);
	count--;	
    }
    
    ledstate = 0;        
    mraa_gpio_write(gpio, ledstate); 

}

void executeAction(const std::string& id, const std::string& actioncode, int timeToWait, net::socketconnect* sock, net::message* msg, const std::string& from){

    printf("Trying executing actions #%s#\n",id.c_str());
    if (strcmp(actioncode.c_str(),"activate")==0) {
  
  	pthread_t _thread;	  
    	waitingTime = timeToWait;
    	pthread_create(&_thread, NULL, &staticWaitThread, NULL);	
	
    } 
    
    if (strcmp(actioncode.c_str(),"deactivate")==0) {
        
	time_t diff =  0;
	if (beginCounter!=0) {
	   diff = time(NULL) - beginCounter;
	}
	
	ledstate = 0;        
        mraa_gpio_write(gpio, ledstate); 
	
        std::string name = getDeviceName();

        msg->_type = 1;

        //char tempValueStr[10];
        //memset(tempValueStr, 0, sizeof(tempValueStr));
        //sprintf(tempValueStr, "%.2f", tempValueFahrenheit);

        DynamicJsonBuffer jsonNewBuffer;

        JsonObject& rootNew = jsonNewBuffer.createObject();
        rootNew["from"] = name.c_str();
        rootNew["to"] = from.c_str();

        JsonArray& arrayData = rootNew.createNestedArray("actions");
  
        JsonObject& objectTemperature = jsonNewBuffer.createObject();
        objectTemperature["rest"] = waitingTime-diff;
	beginCounter = 0;
	waitingTime = 0;
	count = 0;
        arrayData.add(objectTemperature);
  
        char buffer[500];
        memset(buffer, 0, sizeof(buffer));
        rootNew.printTo(buffer, sizeof(buffer));

        msg->_payload = std::string(buffer, strlen(buffer));
        sock->sendMessage(msg);
		
    }
    
    if (strcmp(actioncode.c_str(),"counter")==0) {
       
        std::string name = getDeviceName();

        msg->_type = 1;

        DynamicJsonBuffer jsonNewBuffer;

        JsonObject& rootNew = jsonNewBuffer.createObject();
        rootNew["from"] = name.c_str();
        rootNew["to"] = from.c_str();

        JsonArray& arrayData = rootNew.createNestedArray("actions");
  
        JsonObject& objectTemperature = jsonNewBuffer.createObject();
        objectTemperature["rest"] = count;
	
        arrayData.add(objectTemperature);
  
        char buffer[500];
        memset(buffer, 0, sizeof(buffer));
        rootNew.printTo(buffer, sizeof(buffer));

        msg->_payload = std::string(buffer, strlen(buffer));
        sock->sendMessage(msg);
		
    }	
	
}

void callbackReceiveMessage(net::socketconnect* sock, net::message* msg){

    DynamicJsonBuffer jsonBuffer;

    JsonObject& root = jsonBuffer.parseObject((char*) msg->_payload.c_str());
 
    const char* from = root["from"];
    printf("JSON from:%s\n", from);
    
    if (msg->_type==0) {
    	
	
	if (root.containsKey("actions") && root.containsKey("from")) {
	
	  printf("Need to execute action(s)\n");
	
	  JsonArray& nestedArray = root["actions"];
	  const char* from = root["from"];

	  for (int i=0; i<nestedArray.size(); i++) {
	     const char* id = nestedArray[i]["id"];
	     printf("Trying executing actions on device %s\n",id);
             const char* actioncode = nestedArray[i]["actioncode"];
	     printf("Trying executing action code %s\n",actioncode);
	     const int time = nestedArray[i]["time"];
	     printf("Trying for %d secondes\n",time);
	     
	     executeAction(std::string(id), std::string(actioncode), time, sock, msg, std::string(from));
	  }
	
	} else {
	
          std::string name = getDeviceName();

    	  msg->_type = 1;

          DynamicJsonBuffer jsonNewBuffer;
      
          JsonObject& rootNew = jsonNewBuffer.createObject();
          rootNew["from"] = name.c_str();
          rootNew["to"] = "test";
      
          JsonArray& arrayData = rootNew.createNestedArray("devices");
      
          JsonObject& objectLight = jsonNewBuffer.createObject();
          objectLight["ID"] = deviceID.c_str();
          objectLight["OWNER"] = ownerLogin.c_str();
	  objectLight["NAME"] = name.c_str();
	  objectLight["TYPE"] = type.c_str();
	  
          arrayData.add(objectLight);

          char buffer[500];
          memset(buffer, 0, sizeof(buffer));
          rootNew.printTo(buffer, sizeof(buffer));

    	  msg->_payload = std::string(buffer, strlen(buffer));
	  sock->sendMessage(msg);
    
    	}
    
    }

    	

}

int main(int argc, char *argv[])
{

    if(argc <= 4)
    {
        printf("\n Usage: %s <ip of server> <device id> <owner login> <name> <type>\n",argv[0]);
        return 1;
    } 
    
    deviceID = std::string(argv[2]);
    
    ownerLogin = std::string(argv[3]);
    
    name = std::string(argv[4]);
    
    type = std::string(argv[5]);

    init();

    net::socketconnect aSck(argv[1], 5000);
    aSck.setCallback(&callbackReceiveMessage);

    pthread_exit(0);    	    

    return 0;
}
