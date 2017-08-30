package paquete;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PageManager extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String name= (String)session.getAttribute("name");
        response.getWriter().write(name);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        HttpSession session = request.getSession();
        session.setAttribute("name", name);
        if(type.equals("prof"))response.getWriter().write("planet.html");
        else if(type.equals("alumno"))response.getWriter().write("planet_1.html");
    }

}
