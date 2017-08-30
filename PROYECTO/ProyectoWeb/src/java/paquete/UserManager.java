package paquete;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderSchemaFactory;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.SAXException;

public class UserManager extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        FileReader fileReader;
        fileReader = new FileReader(request.getServletContext().getRealPath("/")+"data/login.xml");
        String fileContents = "";
        int i ;
        while((i =  fileReader.read())!=-1){
            char ch = (char)i;
            fileContents = fileContents + ch;
        }
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(fileContents);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String OBJson = request.getParameter("json");
        String name = request.getParameter("name");
        String index = request.getParameter("index");
        String password = request.getParameter("pass");
        String type = request.getParameter("type");
        if(index.equals("save"))saveToXML(request,response,name,password,type);
        if(index.equals("delete"))deleteFromXML(request,response,name);
        
    }
    protected void saveToXML(HttpServletRequest request, HttpServletResponse response,String name,String password,String type)
            throws ServletException, IOException {
        try {
            //ABRIR Y VALIDAR XML
            SchemaFactory schemafac = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemafac.newSchema(new File(request.getServletContext().getRealPath("/")+"data/login.xsd"));
            XMLReaderJDOMFactory factory = new XMLReaderSchemaFactory(schema);
            SAXBuilder sb = new SAXBuilder(factory);
            
            File usrs = new File(request.getServletContext().getRealPath("/")+"data/login.xml");
            Document doc = (Document) sb.build(usrs);
            List<Element> users = doc.getRootElement().getChildren("usuario");
            //CREAR NUEVO ELEMENTO O REEMPLAZA UNO EXISTENTE
            Element root = doc.getRootElement();
            Element child = new Element("usuario");
            Element nombre = new Element("nombre");
            Element tipo = new Element("tipo");
            Element contra = new Element("password");
            nombre.setText(name);
            tipo.setText(type);
            contra.setText(password);
            child.addContent(nombre);
            child.addContent(tipo);
            child.addContent(contra);
            boolean elementExists = false;
            for(Element user : users){
                int id = users.indexOf(user);
                System.out.println(user.getAttributeValue("name")+" == "+name+" : "+id);
                if(user.getAttributeValue("name").equals(name)){
                    elementExists = true;
                    user.setText(child.getText());
                    break;
                }
            }
            if(!elementExists){
                //root.setContent(planetas);
                root.addContent(child);
            }
            //CONSTRUIR ARCHIVO
            doc.setContent(root);
            
            //GUARDAR ARCHIVO
            try (FileWriter writer = new FileWriter(usrs)) {
                XMLOutputter outputter = new XMLOutputter();
                outputter.setFormat(Format.getPrettyFormat());
                outputter.output(doc, writer);
                outputter.output(doc, System.out);
                writer.close();
            }
        } catch (SAXException | JDOMException ex) {response.getWriter().write("failed");}
        response.getWriter().write(name);
    }
    protected void deleteFromXML(HttpServletRequest request, HttpServletResponse response,String name)
            throws ServletException, IOException {
        try {
            //ABRIR Y VALIDAR XML
            SchemaFactory schemafac = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemafac.newSchema(new File(request.getServletContext().getRealPath("/")+"data/login.xsd"));
            XMLReaderJDOMFactory factory = new XMLReaderSchemaFactory(schema);
            SAXBuilder sb = new SAXBuilder(factory);
            
            File usrs = new File(request.getServletContext().getRealPath("/")+"data/login.xml");
            Document doc = (Document) sb.build(usrs);
            List<Element> users = doc.getRootElement().getChildren("usuario");
            System.out.println(users.size());
            //CREAR NUEVO ELEMENTO O REEMPLAZA UNO EXISTENTE
            Element root = doc.getRootElement();
            for(Element user : users){
                int id = users.indexOf(user);
                System.out.println(user.getChild("nombre").getText()+" == "+name+" : "+id);
                if(user.getChild("nombre").getText().equals(name)){
                    System.out.println(name + " I exist!");
                    user.getParent().removeContent(user);
                    break;
                }
            }
            //CONSTRUIR ARCHIVO
            doc.setContent(root);
            
            //GUARDAR ARCHIVO
            try (FileWriter writer = new FileWriter(usrs)) {
                XMLOutputter outputter = new XMLOutputter();
                outputter.setFormat(Format.getPrettyFormat());
                outputter.output(doc, writer);
                outputter.output(doc, System.out);
                writer.close();
                response.getWriter().write(name);
            }
        } catch (SAXException | JDOMException ex) {response.getWriter().write("failed");}
        
    }
}