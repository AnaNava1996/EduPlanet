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

public class FileManager extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        FileReader fileReader;
        fileReader = new FileReader(request.getServletContext().getRealPath("/")+"data/planetas.xml");
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
        if(index.equals("save"))saveToXML(request,response,OBJson,name);
        if(index.equals("delete"))deleteFromXML(request,response,name);
        
    }
    protected void saveToXML(HttpServletRequest request, HttpServletResponse response,String OBJson,String name)
            throws ServletException, IOException {
        try {
            //ABRIR Y VALIDAR XML
            SchemaFactory schemafac = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemafac.newSchema(new File(request.getServletContext().getRealPath("/")+"data/planetas.xsd"));
            XMLReaderJDOMFactory factory = new XMLReaderSchemaFactory(schema);
            SAXBuilder sb = new SAXBuilder(factory);
            
            File planetitas = new File(request.getServletContext().getRealPath("/")+"data/planetas.xml");
            Document doc = (Document) sb.build(planetitas);
            List<Element> planetas = doc.getRootElement().getChildren("planeta");
            //CREAR NUEVO ELEMENTO O REEMPLAZA UNO EXISTENTE
            Element root = doc.getRootElement();
            Element child = new Element("planeta");
            child.setAttribute("name", name);
            child.setText(OBJson);
            boolean elementExists = false;
            for(Element planeta : planetas){
                int id = planetas.indexOf(planeta);
                System.out.println(planeta.getAttributeValue("name")+" == "+name+" : "+id);
                if(planeta.getAttributeValue("name").equals(name)){
                    System.out.println(name + " I exist!");
                    elementExists = true;
                    planeta.setText(child.getText());
                    break;
                }
            }
            if(elementExists){
                //root.setContent(planetas);
            }
            else root.addContent(child);
            //CONSTRUIR ARCHIVO
            doc.setContent(root);
            
            //GUARDAR ARCHIVO
            try (FileWriter writer = new FileWriter(planetitas)) {
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
            Schema schema = schemafac.newSchema(new File(request.getServletContext().getRealPath("/")+"data/planetas.xsd"));
            XMLReaderJDOMFactory factory = new XMLReaderSchemaFactory(schema);
            SAXBuilder sb = new SAXBuilder(factory);
            
            File planetitas = new File(request.getServletContext().getRealPath("/")+"data/planetas.xml");
            Document doc = (Document) sb.build(planetitas);
            List<Element> planetas = doc.getRootElement().getChildren("planeta");
            //CREAR NUEVO ELEMENTO O REEMPLAZA UNO EXISTENTE
            Element root = doc.getRootElement();
            for(Element planeta : planetas){
                int id = planetas.indexOf(planeta);
                System.out.println(planeta.getAttributeValue("name")+" == "+name+" : "+id);
                if(planeta.getAttributeValue("name").equals(name)){
                    System.out.println(name + " I exist!");
                    planeta.getParent().removeContent(planeta);
                    break;
                }
            }
            //CONSTRUIR ARCHIVO
            doc.setContent(root);
            
            //GUARDAR ARCHIVO
            try (FileWriter writer = new FileWriter(planetitas)) {
                XMLOutputter outputter = new XMLOutputter();
                outputter.setFormat(Format.getPrettyFormat());
                outputter.output(doc, writer);
                outputter.output(doc, System.out);
                writer.close();
            }
        } catch (SAXException | JDOMException ex) {response.getWriter().write("failed");}
        response.getWriter().write(name);
    }
}