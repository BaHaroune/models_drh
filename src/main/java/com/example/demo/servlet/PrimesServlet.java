package com.example.demo.servlet;

import com.example.demo.entity.Prime;
import com.example.demo.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/PrimesServlet")
public class PrimesServlet extends HttpServlet {

    private EntityManager em;

    @Override
    public void init() throws ServletException {
        em = JPAUtil.getEntityManager();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        String typeMission = request.getParameter("type");
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");

        try {
            Query query = em.createQuery("SELECT p FROM Prime p");
            List<Prime> primes = query.getResultList();

            for (int i = 0; i < primes.size(); i++) {
                Prime p = primes.get(i);

                jsonBuilder.append("{")
                        .append("\"fonction\":\"").append(p.getFonction()).append("\",");

                if ("etranger".equalsIgnoreCase(typeMission)) {
                    jsonBuilder.append("\"prime\":").append(p.getEtranger());
                } else {
                    jsonBuilder.append("\"prime\":").append(p.getInterieur());
                }

                jsonBuilder.append("}");

                if (i < primes.size() - 1) {
                    jsonBuilder.append(",");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        jsonBuilder.append("]");

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonBuilder.toString());
        }
    }

    @Override
    public void destroy() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
