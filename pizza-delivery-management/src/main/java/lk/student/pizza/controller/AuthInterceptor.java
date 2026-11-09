
package lk.student.pizza.controller;

import jakarta.servlet.http.*;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

public class AuthInterceptor implements HandlerInterceptor {

 public boolean preHandle(
         HttpServletRequest request,
         HttpServletResponse response,
         Object handler) throws Exception {

  String path = request.getRequestURI();

  Object value = request.getSession(false) == null
          ? null
          : request.getSession(false).getAttribute("user");

  @SuppressWarnings("unchecked")
  Map<String, String> user = value instanceof Map
          ? (Map<String, String>) value
          : null;

  boolean protectedPath =
          path.equals("/menu")
                  || path.startsWith("/admin/")
                  || path.startsWith("/orders")
                  || path.startsWith("/customers");

  if (protectedPath && user == null) {

   response.sendRedirect(
           "/login?next=" +
                   java.net.URLEncoder.encode(
                           path,
                           java.nio.charset.StandardCharsets.UTF_8
                   )
   );

   return false;
  }

  if ((path.startsWith("/admin/")
          || path.startsWith("/orders/status")
          || path.startsWith("/orders/delete")
          || path.startsWith("/customers"))
          && user != null
          && !"ADMIN".equals(user.get("role"))) {

   response.sendError(
           403,
           "Administrator access required"
   );

   return false;
  }

  if (path.startsWith("/orders/new")
          || path.equals("/orders/place")) {

   if (user != null && !"CUSTOMER".equals(user.get("role"))) {

    response.sendError(
            403,
            "Customer access required"
    );

    return false;
   }
  }

  return true;
 }
}
