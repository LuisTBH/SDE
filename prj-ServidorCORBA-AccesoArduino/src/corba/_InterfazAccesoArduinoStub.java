package corba;


/**
* corba/_InterfazAccesoArduinoStub.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/Users/Luis/workspace-MARS/prj-ServidorCORBA-AccesoArduino/InterfazAccesoArduino.idl
* s�bado 8 de abril de 2017 00H51' CEST
*/

public class _InterfazAccesoArduinoStub extends org.omg.CORBA.portable.ObjectImpl implements corba.InterfazAccesoArduino
{

  public String enviarOrdenArduino (String msg)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("enviarOrdenArduino", true);
                $out.write_string (msg);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return enviarOrdenArduino (msg        );
            } finally {
                _releaseReply ($in);
            }
  } // enviarOrdenArduino

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:corba/InterfazAccesoArduino:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }

  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException
  {
     String str = s.readUTF ();
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     org.omg.CORBA.Object obj = orb.string_to_object (str);
     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();
     _set_delegate (delegate);
   } finally {
     orb.destroy() ;
   }
  }

  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException
  {
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     String str = orb.object_to_string (this);
     s.writeUTF (str);
   } finally {
     orb.destroy() ;
   }
  }
} // class _InterfazAccesoArduinoStub
