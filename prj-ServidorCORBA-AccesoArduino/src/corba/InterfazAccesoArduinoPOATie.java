package corba;


/**
* corba/InterfazAccesoArduinoPOATie.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/Users/Luis/workspace-MARS/prj-ServidorCORBA-AccesoArduino/InterfazAccesoArduino.idl
* s�bado 8 de abril de 2017 00H51' CEST
*/

public class InterfazAccesoArduinoPOATie extends InterfazAccesoArduinoPOA
{

  // Constructors

  public InterfazAccesoArduinoPOATie ( corba.InterfazAccesoArduinoOperations delegate ) {
      this._impl = delegate;
  }
  public InterfazAccesoArduinoPOATie ( corba.InterfazAccesoArduinoOperations delegate , org.omg.PortableServer.POA poa ) {
      this._impl = delegate;
      this._poa      = poa;
  }
  public corba.InterfazAccesoArduinoOperations _delegate() {
      return this._impl;
  }
  public void _delegate (corba.InterfazAccesoArduinoOperations delegate ) {
      this._impl = delegate;
  }
  public org.omg.PortableServer.POA _default_POA() {
      if(_poa != null) {
          return _poa;
      }
      else {
          return super._default_POA();
      }
  }
  public String enviarOrdenArduino (String msg)
  {
    return _impl.enviarOrdenArduino(msg);
  } // enviarOrdenArduino

  private corba.InterfazAccesoArduinoOperations _impl;
  private org.omg.PortableServer.POA _poa;

} // class InterfazAccesoArduinoPOATie