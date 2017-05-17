package server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Properties;

import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.IdAssignmentPolicyValue;
// import org.omg.PortableServer.LifespanPolicyValue;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.ThreadPolicyValue;

public class Server_AOM {

	public static void main(String[] args) {

		Properties props = System.getProperties();
		props.setProperty("org.omg.CORBA.ORBClass", "com.sun.corba.se.internal.POA.POAORB");
		props.setProperty("org.omg.CORBA.ORBSingletonClass", "com.sun.corba.se.internal.corba.ORBSingleton");
		// Añadimos estas líneas para indicar el Host y el Puerto del servicio
		// de nombres. Comentar si no usamos el servicio de nombres.
		props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
		props.setProperty("org.omg.CORBA.ORBInitialPort", "1050");
		try {
			// Initialize the ORB.
			org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);

			// get a reference to the root POA
			org.omg.CORBA.Object obj = orb.resolve_initial_references("RootPOA");
			POA poaRoot = POAHelper.narrow(obj);

			// Create policies for our persistent POA
			org.omg.CORBA.Policy[] policies = {
					// poaRoot.create_lifespan_policy(LifespanPolicyValue.PERSISTENT),
					poaRoot.create_id_assignment_policy(IdAssignmentPolicyValue.USER_ID),
					poaRoot.create_thread_policy(ThreadPolicyValue.ORB_CTRL_MODEL) };

			// Create myPOA with the right policies
			POA poa = poaRoot.create_POA("InterfazAccesoArduinoServerImpl_poa", poaRoot.the_POAManager(), policies);

			// Create the servant
			InterfazAccesoArduinoServerImpl servant = new InterfazAccesoArduinoServerImpl();

			// Activate the servant with the ID on myPOA
			byte[] objectId = "AnyObjectID".getBytes();
			poa.activate_object_with_id(objectId, servant);

			// Activate the POA manager
			poaRoot.the_POAManager().activate();

			// Get a reference to the servant and write it down.
			obj = poa.servant_to_reference(servant);

			// ---- Uncomment below to enable Naming Service access. ----
			org.omg.CORBA.Object ncobj = orb.resolve_initial_references("NameService");
			NamingContextExt nc = NamingContextExtHelper.narrow(ncobj);
			nc.bind(nc.to_name("ServidorCORBA_AccesoArduino"), obj);

			// Parte a comentar si queremos usar el servicio de nombres en lugar
			// de un archivo .ior
			// PrintWriter ps = new PrintWriter(new FileOutputStream(new File("server.ior")));
			// ps.println(orb.object_to_string(obj));
			// ps.close();

			// System.out.println("CORBA Server ready...");

			System.out.println("CORBA Server ready with name service...");

			// Wait for incoming requests
			orb.run();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
