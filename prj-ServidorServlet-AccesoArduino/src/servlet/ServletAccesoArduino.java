package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ServletAccesoArduino
 */
@WebServlet("/ServletAccesoArduino")
public class ServletAccesoArduino extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static AccesoArduino AA; //Variable AA para acceso a Arduino
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletAccesoArduino() {
        super();
        // TODO Auto-generated constructor stub
        AA = new AccesoArduino("192.168.4.1",80); //Se instancia el objeto AA de la clase AccesoArduino para acceder al Arduino en la direccion y puerto indicado
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String identificador = request.getParameter("id");
		String respuesta = "";
	    if (identificador != null) {
		 //EJERCICIO: Invocar el metodo enviarOrdenArduino del objeto AA para acceder al Arduino enviandole la solicitud del valor del identificador especificado
	     respuesta = AA.enviarOrdenArduino(identificador);
	    }
	    else respuesta = "Falta añadir a la URL: ?id=nombreIdentificador";
		
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    out.println("<html>");
	    out.println("<head><title>ServletAccesoArduino</title></head>");
	    out.println("<body bgcolor=\"#ffffff\">");
	    out.println("<p>La respuesta de Arduino es: " + respuesta + "</p>");
	    out.println("</body></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String identificador = request.getParameter("id");
		String valor = request.getParameter("val");
		String orden = identificador+"="+valor;
		String respuesta = "";
	    if (orden != null) {
		 //EJERCICIO: Invocar el metodo enviarOrdenArduino del objeto AA para acceder al Arduino enviandole la orden especificada
	     respuesta = AA.enviarOrdenArduino(orden);
	    }
		
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    out.println("<html>");
	    out.println("<head><title>ServletAccesoArduino</title></head>");
	    out.println("<body bgcolor=\"#ffffff\">");
	    out.println("<p>La respuesta de Arduino es: " + respuesta + "</p>");
	    out.println("</body></html>");
	}

}
