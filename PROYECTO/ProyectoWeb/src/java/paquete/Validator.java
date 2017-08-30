package paquete;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderSchemaFactory;
import org.xml.sax.SAXException;

public class Validator extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        String pop;
        String nombre = (String)request.getParameter("nombre");
        String password = (String)request.getParameter("password");
        try {
            SchemaFactory schemafac = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemafac.newSchema(new File(request.getServletContext().getRealPath("/")+"data/login.xsd"));
            XMLReaderJDOMFactory factory = new XMLReaderSchemaFactory(schema);
            SAXBuilder sb = new SAXBuilder(factory);
            try {
                Document doc = (Document) sb.build(new File(request.getServletContext().getRealPath("/")+"data/login.xml"));
                List<Element> list = doc.getRootElement().getChildren("usuario");
            for ( int i = 0; i < list.size(); i++ ){
                Element user_element = (Element) list.get(i);
                if(nombre.equals(user_element.getChildText("nombre")))
                    if(password.equals(user_element.getChildText("password"))){
                        HttpSession session = request.getSession();
                        session.setAttribute("userName",user_element.getChildText("nombre"));
                        session.setAttribute("userType",user_element.getChildText("tipo"));
                        session.setAttribute("lista",list);
                        if(user_element.getChildText("tipo").equals("Admin"))response.sendRedirect("adminservlet");
                        if(user_element.getChildText("tipo").equals("Profesor"))response.sendRedirect("welcome");
                        if(user_element.getChildText("tipo").equals("Alumno"))response.sendRedirect("welcome");
                        break;
                    }
                else{
                    PrintWriter out=response.getWriter();
                out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Servlet1</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("Fail:"+nombre);
            out.println("<a href='index.html' >Regresar</a>");            
            out.println("</body>");
            out.println("</html>");break;}
            }
                pop = "Success";
            } catch (JDOMException ex) {
                pop = "Fail";
                Logger.getLogger(Validator.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(pop.equals("Fail"))response.sendRedirect("index.html");
            
        }   catch (SAXException ex) {
            Logger.getLogger(Validator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
