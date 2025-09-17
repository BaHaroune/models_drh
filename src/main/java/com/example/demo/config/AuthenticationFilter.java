package com.example.demo.config;

import com.example.demo.entity.User;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/", "/index.jsp", "/admin/*"})
public class AuthenticationFilter implements Filter {

    private static final String[] PUBLIC_PATHS = {
            "/login",
            "/register",
            "/login.jsp",
            "/register.jsp"
    };

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();

        // Vérifier si le chemin est public
        for (String publicPath : PUBLIC_PATHS) {
            if (requestURI.endsWith(publicPath)) {
                chain.doFilter(request, response);
                return;
            }
        }

        // Vérifier si l'utilisateur est connecté
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(contextPath + "/login.jsp");
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
