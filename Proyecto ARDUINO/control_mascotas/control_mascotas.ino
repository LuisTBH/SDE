/* Programa de control de un comedero y bebedero*/
/* automático de mascotas controlado por Arduino*/
/* Creado por Luis García Gómez con la ayuda de */
/*            Juan Luis Posadas Yagüe           */
/*            SDE - DISCA UPV 2016/2017         */



// Incluímos la librería para poder controlar el servo
#include <Servo.h>

// Incluimos la libreria para poder poner este Arduino como
// un servidor para darle ordenes y consultar datos
#include <SoftwareSerial.h>
SoftwareSerial SerialEsp8266(3, 2); // RX, TX

int idnodo = 1;
String mi_IP = "";
int pin_LedProcesando = 13;
String nombre_SolicitudTodos = "TODOS";

// Declaramos la variable para controlar el servo
Servo servoMotor;
// Declaramos una constante para controlar el rele de la electrovalvula
const int control = 10;

//SENSORES
  //ULTRASONIDOS
  unsigned long pulso; // necesitamos una variable para medir el pulso
  float distancia; // otra para calcular la distancia
  int pin = 8;  // y otra para el pin del sensor
  String nombre_SensorUltrasonidos = "deposito_comida";
  float valor_SensorUltrasonidos = 0;
  
    //ACTUADOR DE ESTE SENSOR
    int led = 12; // Led que se enciende cuando el valor de los ultrasonidos denota que no hay comida en el deposito
    String valor_ActuadorLedUltrasonidos; //{OFF, ON}


    //SENSOR DE LIQUIDOS
  //Pin asociado al sensor de nivel de líquido.
  int pinSensor_Liquido = 4;
  String nombre_SensorLiquidos = "deposito_agua";
  String valor_SensorLiquidos = "";
  
    //ACTUADORES DE ESTE SENSOR    
    int pin_led_lleno = 6;  //Led de lleno (deposito de agua lleno)
    //String valor_ActuadorLedSensorLiq_lleno; //{OFF, ON}    
    int pin_led_vacio = 7;  //Led pin vacío (deposito de agua vacio)
    //String valor_ActuadorLedSensorLiq_vacio; //{OFF, ON}   

  //SENSOR DE PRESION FSR COMIDA (ESTA EN ANALOGICO)
  int fsrAnalagogPin = A0;
  int fsrReading;
  String nombre_SensorPresion = "cuenco_comida";
  int valor_SensorPresion = 0;

    //ACTUADORES DE ESTE SENSOR  
    int LEDPin = 11;  // Se encendera cuando baje la presión del sensor y no tenga comida en el cuenco el animal
    int LedBrillo;  // Por si queremos poner intensidad dependiendo de la presión ( Esto es por probar el map)
    String valor_ActuadorLedSensorPresion; //{OFF, ON}   
    

  //NO IMPLEMENTADO SOLO FALTARIA CONECTARLO AL ARDUINO Y DESCOMENTAR ESTAS LINEAS
  //SENSOR DE PRESION FSR AGUA (ESTA EN ANALOGICO)
  //int fsrAnalagogPin2 = A1;
  //int fsrReading2;
  //String nombre_SensorPresion2 = "cuenco_agua";



//SENSORES
String nombre_SensorLuz = "luz";
int pin_SensorLuz = 0;
int valor_SensorLuz = 0;
String nombre_SensorTemperatura = "temp";
int pin_SensorTemperatura = 1;
int valor_SensorTemperatura = 0;

//ACTUADORES
//String nombre_ActuadorLed = "led";
//int pin_ActuadorLed = 12;
//String valor_ActuadorLed; //{OFF, ON}

//CONFIGURACION INICIAL
void setup() {
  pinMode(pin_LedProcesando, OUTPUT);
  digitalWrite(pin_LedProcesando, HIGH);

  //Se inicializan actuadores
//  pinMode(pin_ActuadorLed, OUTPUT);
//  digitalWrite(pin_ActuadorLed, LOW);
//  valor_ActuadorLed = "OFF";

  //Se inicializan actuadores luminosos LED!!!!
  pinMode(led, OUTPUT);
  digitalWrite(led, LOW);
  valor_ActuadorLedUltrasonidos = "OFF";
  pinMode(pinSensor_Liquido, INPUT);
  pinMode(pin_led_lleno, OUTPUT);
  digitalWrite(pin_led_lleno, HIGH);
  pinMode(pin_led_vacio, OUTPUT);
  digitalWrite(pin_led_vacio, LOW);
  pinMode(LEDPin, OUTPUT);
  digitalWrite(LEDPin,LOW);
  valor_ActuadorLedSensorPresion = "OFF";
  pinMode(control, OUTPUT);


    //Se inicializan actuadores  
  // Iniciamos el servo para que empiece a trabajar con el pin 9
  servoMotor.attach(9);
  servoMotor.write(0);

  //Rele empezamos en cerrado para la electrovalvula en modo OFF
  digitalWrite(control, LOW);

  //Se inicializan sensores
  //...........................
  
  //Para conectar con el modulo esp8266
  SerialEsp8266.begin(9600);
  //Para conectar con el modulo serie
  Serial.begin(9600);
  inicializarEsp8266();
  configurarServidorTCP(80);
  
  digitalWrite(pin_LedProcesando, LOW);
}

//BUCLE DE EJECUCION
void loop() {
  delay(50); //Minimo retardo entre iteraciones

  //SE LEEN LOS VALORES DE LOS SENSORES
  valor_SensorLuz = analogRead(pin_SensorLuz);  
  valor_SensorTemperatura = analogRead(pin_SensorTemperatura);

  delay(2000);
  pinMode(pin, OUTPUT); // ponemos el pin como salida
  digitalWrite(pin, HIGH); // lo activamos
  delayMicroseconds(10); // esperamos 10 microsegundos
  digitalWrite(pin, LOW); // lo desactivamos
  pinMode(pin, INPUT); // cambiamos el pin como entrada
  pulso = pulseIn(pin, HIGH); // medimos el pulso de salida del sensor

  Serial.println("############## DATOS SENSOR ULTRASONIDOS #################");
  Serial.print("tiempo = "); // lo pasamos a milisegundos y lo sacamos por
  // el puerto serie
  Serial.print(float(pulso / 1000.0));
  Serial.print("ms, distancia = "); // ahora calculamos la distancia en cm
  // y al puerto serie
  distancia = ((float(pulso / 1000.0)) * 34.32) / 2;
  Serial.print(distancia);
  Serial.println("cm");

  valor_SensorUltrasonidos = distancia;


    //Aquí controlamos el valor del sensor de líquidos
  Serial.println("############## DATOS SENSOR LIQUIDOS #################");
  int estado = digitalRead(pinSensor_Liquido);
  Serial.print("Estado del sensor de liquidos de Arduino: ");
  Serial.println(estado);
  if (estado == 1) {
    Serial.println("El deposito esta lleno");
    valor_SensorLiquidos = "LLENO";
    } else {
     Serial.println("El deposito esta vacio");
     valor_SensorLiquidos = "VACIO";
      }

  switch(estado) {
    case 0:
      digitalWrite( pin_led_vacio , HIGH) ;
      digitalWrite( pin_led_lleno , LOW) ;
      //Activamos la valvula del agua para llenar el tanque.
      digitalWrite(control, HIGH);
      delay(4000);
      digitalWrite(control, LOW);
      break;
      

    case 1:
      digitalWrite( pin_led_lleno , HIGH) ;
      digitalWrite( pin_led_vacio , LOW) ;    
      break;  
    }
    delay(1000);


      // Medida en vacío del sensor
  
  float Limite = 4.20 ;                  
  if ( distancia < Limite) {
    digitalWrite ( led , HIGH) ;
    valor_ActuadorLedUltrasonidos = "ON";
     Serial.println("Nivel de comida por debajo del limite");
     Serial.print("Si no se corrige en 3 minutos, se hara automaticamente esta tarea\n");
     delay(18000); //Son 180000 los 3 minutos
    // Desplazamos a la posición 180º
    servoMotor.write(180);
    // Esperamos 2 segundos
    delay(3000);
    servoMotor.write(0);
    digitalWrite( led , LOW) ;
    valor_ActuadorLedUltrasonidos = "OFF";
    

  } else {
    digitalWrite( led , LOW) ;
    valor_ActuadorLedUltrasonidos = "OFF";
    //Poner mejor más tiempo como 300000 para 5 minutos
    delay (5000) ;                  // Para limitar el número de mediciones
  }

//NOTA: Con el sensor de presión se ve que por falta de memoria no es capaz de habilitar el Arduino como server
//Por so se ha comentado todo este sensor si queremos que el ESP funcione y no de ERROR al ponerse en modo servidor.
   //Aqui empieza la parte del sensor de presión
  
  fsrReading = analogRead(fsrAnalagogPin);

  // Esta es la parte que si se descomentamos hace que de ERROR el modulo de wifi ESP!!!!!
//  Serial.println("############## DATOS SENSOR PRESION #################");
//  Serial.print("Salida analogica del sensor de peso = ");
//  Serial.println(fsrReading);

  if (fsrReading < 28 ) {
    digitalWrite(LEDPin, HIGH);
    valor_SensorPresion = "ON";
    servoMotor.write(180);
    // Esperamos 2 segundos
    delay(3000);
    servoMotor.write(0);
    
    } else {
        digitalWrite(LEDPin, LOW);
        valor_SensorPresion = "OFF";
    }
//
////  LedBrillo = map(fsrReading, 0, 1023, 0, 255);
////  analogWrite(LEDPin, LedBrillo);
//
//    delay(2000);   
  

  

  //EN FUNCION DE LOS VALORES DE LOS SENSORES SE REALIZAN LAS ACCIONES DE CONTROL CORRESPONDIENTES (AUTONOMIA E INTELIGENCIA DEL SISTEMA)
  //
  //............
  //

  //Se comprueba si han llegado datos por el puerto del esp8266 (conexiones de clientes al servidor) y se procesan:
  if (SerialEsp8266.available() > 0) { //Ver si hay mensaje del ESP8266
    if (SerialEsp8266.find("+IPD,")) { //Ver si el servidor ha recibido datos
      digitalWrite(pin_LedProcesando, HIGH);
      //+IPD,<ID>,<len>[,<remoteIP>,<remote port>]:<data>
      //Segun el formato que se ha decidido, la informacion <data> enviada por el cliente ocupa solo una linea
      String S = SerialEsp8266.readStringUntil('\r'); //Lee los datos hasta que se encuentra el carriage return character (ASCII 13, or '\r'). El caracter buscado no se devuelve en la cadena resultado.
      while (SerialEsp8266.available()) { //Se vacia el buffer.
        SerialEsp8266.read();
      }
      Serial.println("Datos recibidos: " + S);
      int pos_sig_coma = S.indexOf(",");
      String s_conexion_ID = S.substring(0, pos_sig_coma);
      int conexion_ID = s_conexion_ID.toInt(); //A continuacion del +IPD, se encuentra el identificador de la conexion <ID>, para poder responder

      int pos_dos_puntos = S.indexOf(":", pos_sig_coma); //Despues de los : se encuentran los datos <data>
      String nombre = S.substring(pos_dos_puntos + 1); //Se asigna a nombre todos los datos <data>
      String valor = "";
      //El formato que se ha decidido para <data> es: nombre[=valor]
      //Si solo aparece nombre, el cliente solicita el valor para dicho nombre
      //Si aparece =valor, el cliente solicita asignar dicho valor para dicho nombre
      int pos_igual = nombre.indexOf("="); //Se comprueba si en los datos <data> aparece =valor despues del nombre
      if (pos_igual != -1) { //Aparece =valor despues del nombre
        valor = nombre.substring(pos_igual + 1);
        nombre = nombre.substring(0, pos_igual);
      }

      //Evalua los datos recibidos y actua en consecuencia:
      //Formato respuesta: nombre1=valor1[&nombre2=valor2......&nombreN=valorN]
      String respuesta = "";
      if (nombre.equals(nombre_SolicitudTodos)) {
       respuesta = nombre_SensorUltrasonidos + "=" + valor_SensorUltrasonidos + "&" + nombre_SensorLiquidos + "=" + valor_SensorLiquidos + "&" + nombre_SensorPresion + "=" + valor_SensorPresion;
      }
      else if (nombre.equals(nombre_SensorUltrasonidos)) {
        respuesta = nombre_SensorUltrasonidos + "=" + valor_SensorUltrasonidos;
      }
      else if (nombre.equals(nombre_SensorLiquidos)) {
        respuesta = nombre_SensorLiquidos + "=" + valor_SensorLiquidos;
      }
      else if (nombre.equals(nombre_SensorPresion)) {
        respuesta = nombre_SensorPresion + "=" + valor_SensorPresion;
      }

      //Si queremos añadir más información como del servo y demás seria hacer lo mismo para todos los elementos que queramos visualizar
      //Dar un nombre_sensor_Actuador y un valor_Sensor_Actuador.
      
   /*   else if (nombre.equals(nombre_ActuadorLed)) {
        if (pos_igual != -1) { //Aparece =valor despues del nombre
          //Recibida una solicitud de asignar un valor
          if (valor.equals("ON")) {
            digitalWrite(pin_ActuadorLed, HIGH);
            valor_ActuadorLed = "ON";
            respuesta = "respuesta=" + nombre_ActuadorLed + " encendido";
          }
          else if (valor.equals("OFF")) {
            digitalWrite(pin_ActuadorLed, LOW);
            valor_ActuadorLed = "OFF";
            respuesta = "respuesta=" + nombre_ActuadorLed + " apagado";
          }
        }
        else { //Recibida una solicitud de devolver su valor actual
         respuesta = nombre_ActuadorLed + "=" + valor_ActuadorLed;
        }
      }

      */
      else { //if nothing else matches, do the default
        respuesta = "respuesta=orden no reconocida";
      }

      enviarMensaje(respuesta, conexion_ID);
      cerrarConexion(conexion_ID);
      digitalWrite(pin_LedProcesando, LOW);
    }
  }
}

boolean inicializarEsp8266() {

  do { //Se reinicia el modulo esp8266
    Serial.print("Reseteando modulo esp8266");
    SerialEsp8266.println("AT+RST"); //Se envía el comando correspondiente al ESP8266
    do {
      delay(100);//Se espera contestacion del ESP8266
    } while (!SerialEsp8266.available());
    //Se comprueba si se ha obtenido la respuesta esperada:
    if (SerialEsp8266.find("ready")) {
      Serial.println("... Modulo preparado");
      break;
    } else {
      Serial.println("... ERROR");
    };
  } while (true);

  do { //Se configura como modo 2: SOFTAP (router)
    Serial.print("Estableciendo modo 2");
    SerialEsp8266.println("AT+CWMODE_DEF=2"); //Se envía el comando correspondiente al ESP8266
    do {
      delay(100);//Se espera contestacion del ESP8266
    } while (!SerialEsp8266.available());
    //Se comprueba si se ha obtenido la respuesta esperada:
    if (SerialEsp8266.find("OK")) {
      Serial.println("... Modo 2 soft-AP (router) establecido");
      break;
    } else {
      Serial.println("... ERROR");
    };
  } while (true);

  do { //Se habilita DHCP
    Serial.print("Habilitando DHCP");
    SerialEsp8266.println("AT+CWDHCP_DEF=0,1"); //Se envía el comando correspondiente al ESP8266
    do {
      delay(100);//Se espera contestacion del ESP8266
    } while (!SerialEsp8266.available());
    //Se comprueba si se ha obtenido la respuesta esperada:
    if (SerialEsp8266.find("OK")) {
      Serial.println("... DHCP habilitado");
      break;
    } else {
      Serial.println("... ERROR");
    };
  } while (true);

  do { //Se configura el router
    Serial.print("Configurando el router Arduino_Luis");
    SerialEsp8266.println("AT+CWSAP_DEF=\"Arduino_Luis\",\"\",5,0"); //Se envía el comando correspondiente al ESP8266
    do {
      delay(100);//Se espera contestacion del ESP8266
    } while (!SerialEsp8266.available());
    //Se comprueba si se ha obtenido la respuesta esperada:
    if (SerialEsp8266.find("OK")) {
      Serial.println("... router configurado");
      break;
    } else {
      Serial.println("... ERROR");
    };
  } while (true);

  //Se obtiene la IP asignada
  Serial.print("Obteniendo la IP asignada");
  SerialEsp8266.println("AT+CIFSR"); //Se envía el comando correspondiente al ESP8266
  do {
    delay(100);//Se espera contestacion del ESP8266
  } while (!SerialEsp8266.available());
  //Se comprueba si se ha obtenido la respuesta esperada:
  if (SerialEsp8266.find("\"")) {
    mi_IP = SerialEsp8266.readStringUntil('\"');
    Serial.print("... IP obtenida=");
    Serial.println(mi_IP);
  }

}

boolean configurarServidorTCP(int port) {
  do { //Aceptar multiples conexiones
    Serial.print("Estableciendo aceptar multiples conexiones");
    SerialEsp8266.println("AT+CIPMUX=1");//Se envía el comando correspondiente al ESP8266
    do {
      delay(100);//Se espera contestacion del ESP8266
    } while (!SerialEsp8266.available());
    //Se comprueba si se ha obtenido la respuesta esperada:
    if (SerialEsp8266.find("OK")) { //Si ya está configurado devuelve "Link is builded"
      Serial.println("... establecido");
      break;
    } else {
      Serial.println("... ERROR");
    };
  } while (true);

  do { //Configurar como Server TCP en el puerto "port"
    Serial.print("Configurando servidor TCP en puerto: " + String(port));
    String cmd = "AT+CIPSERVER=1,";
    cmd += String(port);
    SerialEsp8266.println(cmd);//Se envía el comando correspondiente al ESP8266
    //Con SerialEsp8266.println("AT+CIPSERVER=1,"+port) NO FUNCIONA
    do {
      delay(100);//Se espera contestacion del ESP8266
    } while (!SerialEsp8266.available());
    //Se comprueba si se ha obtenido la respuesta esperada:
    if (SerialEsp8266.find("OK")) {
      Serial.println("... configurado.");
      break;
    } else {
      Serial.println("... ERROR");
    };
  } while (true);

  do { //Configurar el timeout
    Serial.print("Configurando el timeout del server...");
    String cmd = "AT+CIPSTO=15";
    SerialEsp8266.println(cmd);//Se envía el comando correspondiente al ESP8266
    do {
      delay(100);//Se espera contestacion del ESP8266
    } while (!SerialEsp8266.available());
    //Se comprueba si se ha obtenido la respuesta esperada:
    if (SerialEsp8266.find("OK")) {
      Serial.println("... configurado.");
      break;
    } else {
      Serial.println("... ERROR");
    };
  } while (true);

  Serial.println("Esperando conexion...");
}

//Envia mensaje por la conexión establecida
boolean enviarMensaje(String mensaje, int id_conexion) {
  //Al aceptar multiples conexiones (+CIPMUX=1) hay que indicar el identificador de la conexion
  boolean resultado = false;
  //Se prepara el mensaje
  String men = mensaje;
  //men += "\r\n"; //No hace falta porque enviamos estos dos caracteres con el println (no con el print) pero sí hay que tenerlos en cuenta en la longitud del mensaje a enviar (men.length()+2)
  //pinntln: ASCII text followed by a carriage return character (ASCII 13, or '\r') and a newline character (ASCII 10, or '\n')
  //Se prepara el comando con el tamaño de los datos a enviar
  String cmd = "AT+CIPSEND=";
  cmd += String(id_conexion);
  cmd += ",";
  cmd += String(men.length() + 2); //Muy importante sumar +2 correspondientes a los caracteres "\r\n"
  //que se envían con el println (no con el print, en ese caso habría que añadir los caracteres al mensaje (men += "\r\n";)
  //Se realiza la peticion de envío
  Serial.print("Enviando mensaje");
  SerialEsp8266.println(cmd);//Se envía el comando correspondiente al ESP8266
  do {
    delay(100);//Se espera contestacion del ESP8266
  } while (!SerialEsp8266.available());
  //Se comprueba si se ha obtenido la respuesta esperada:
  if (SerialEsp8266.find(">")) {
    // Se envía el mensaje
    SerialEsp8266.println(men);
    do {
      delay(100);//Se espera contestacion del ESP8266
    } while (!SerialEsp8266.available());
    if (SerialEsp8266.find("OK")) {
      Serial.println("... enviado: " + men);
      resultado = true;
    } else {
      Serial.println("... ERROR en el envio de: " + men);
    };
  } else {
    Serial.println("... ERROR. No recibido >");
    //Si no se ha recibido el caracter > es que ya no existe la conexion
  };
  return resultado;
}

void cerrarConexion(int id_conexion) {
  //Al aceptar multiples conexiones (+CIPMUX=1) hay que indicar el identificador de la conexion
  String cmd = "AT+CIPCLOSE";
  cmd += "=";
  cmd += String(id_conexion);
  //Se cierra la conexion
  Serial.println("Cerrando conexion");
  SerialEsp8266.println(cmd); //Se envía el comando correspondiente al ESP8266
}


