package me.catzy.prestiz.exceptions;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

@Component
public class ExceptionHandler extends AbstractHandlerExceptionResolver {
  protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    logStacktrace(ex);
    ModelAndView mav = new ModelAndView();
    if (ex instanceof ExceptionWithHttpCode) {
      ExceptionWithHttpCode exceptionWithHttpCode = (ExceptionWithHttpCode)ex;
      response.setStatus(exceptionWithHttpCode.getHttpCode());
    } else {
      response.setStatus(500);
    } 
    returnError(response, ex);
    return mav;
  }
  
  public static void returnError(HttpServletResponse response, Exception ex) {
    try {
      JsonObject o = new JsonObject();
      o.addProperty("message", ex.getMessage());
      if (ex instanceof UserException) {
        UserException ex2 = (UserException)ex;
        o.addProperty("userMessage", ex2.getUserMessage());
      } 
      response.getWriter().print(o.toString());
      response.setHeader("Content-Type", "application/json");
    } catch (IOException e) {
      logStacktrace(e);
    } 
  }
  
  public static void logStacktrace(Exception ex) {
    ex.printStackTrace();
  }
  
  @Deprecated
  public static void logStacktraceOLD(Exception ex) {
    System.out.println("Exception: " + ex.getMessage());
    boolean skipping = false;
    StackTraceElement[] stackTrace = ex.getStackTrace();
    for (int length = stackTrace.length, i = 0; i < length; i++) {
      StackTraceElement exLine = stackTrace[i];
      if (exLine.getClassName() != null)
        if (!exLine.getClassName().startsWith("me")) {
          skipping = true;
        } else {
          if (skipping) {
            System.out.println(" [hidden]");
            skipping = false;
          } 
          System.out.println("  " + exLine.getClassName() + "(" + exLine.getMethodName() + ".java:" + exLine.getLineNumber());
        }  
    } 
  }
}
