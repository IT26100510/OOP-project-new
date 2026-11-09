package lk.student.pizza.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        Object value = request.getSession(false) == null ? null : request.getSession(false).getAttribute("user");
        @SuppressWarnings("unchecked") Map<String, String> user = value instanceof Map ? (Map<String, String>) value : null;
        boolean protectedPath = path.equals("/menu") || path.startsWith("/admin/") || path.startsWith("/orders")
                || path.startsWith("/customers") || path.startsWith("/users") || path.startsWith("/deliveries") || path.startsWith("/feedback");
        if (protectedPath && user == null) {
            response.sendRedirect("/login?next=" + URLEncoder.encode(path, StandardCharsets.UTF_8));
            return false;
        }
        boolean adminPath = path.startsWith("/admin/") || path.startsWith("/customers") || path.startsWith("/users")
                || path.startsWith("/deliveries") || path.startsWith("/orders/edit") || path.startsWith("/orders/update")
                || path.startsWith("/orders/status") || path.startsWith("/orders/delete") || path.startsWith("/feedback/reply");
        if (adminPath && user != null && !"ADMIN".equals(user.get("role"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Administrator access required");
            return false;
        }
        if ((path.startsWith("/orders/new") || path.equals("/orders/place")) && user != null && !"CUSTOMER".equals(user.get("role"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Customer access required");
            return false;
        }
        return true;
    }
}
