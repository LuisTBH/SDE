package server;
/**
 * This class is the implemetation object for your IDL interface.
 *
 * Let the Eclipse complete operations code by choosing 'Add unimplemented methods'.
 */
public class InterfazAccesoArduinoServerImpl extends corba.InterfazAccesoArduinoPOA {
	/**
	 * Constructor for InterfazAccesoArduinoServerImpl 
	 */
	public InterfazAccesoArduinoServerImpl() {
	}	

	@Override
	public String enviarOrdenArduino(String msg) {
		// TODO Auto-generated method stub
		AccesoArduino acceso = new AccesoArduino("192.168.4.1", 80);
		return acceso.enviarOrdenArduino(msg);
	}
}
