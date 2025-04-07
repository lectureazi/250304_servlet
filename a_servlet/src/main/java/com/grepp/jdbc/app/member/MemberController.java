package com.grepp.jdbc.app.member;

import com.grepp.jdbc.app.auth.Principal;
import com.grepp.jdbc.app.auth.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/member/*")
public class MemberController extends HttpServlet {
    
    private final MemberService memberService = new MemberService();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        
        String url = req.getRequestURI();
        String[] urlArr = url.split("/");
        
        switch (urlArr[urlArr.length-1]){
            case "login":
                login(req, resp);
                break;
            case "logout":
                logout(req, resp);
                break;
            default:
                resp.setStatus(404);
        }
    }
    
    private void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        session.removeAttribute("principal");
        resp.sendRedirect("/");
    }
    
    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        
        resp.setHeader("set-cookie", "JSESSIONID="
                                         + session.getId()
                                         + "; max-age=3600; path=/");
        
        Principal principal = memberService.authenticate(userId, password);
        
        session.setAttribute("principal", principal);
        resp.sendRedirect("/");
    }
    
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
