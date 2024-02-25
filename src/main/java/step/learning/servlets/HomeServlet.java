package step.learning.servlets;

import com.google.inject.Singleton;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Singleton
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("appsettings.json")){
            System.out.println( readStreamToEnd(inputStream) );
        }
        catch (IOException ex){
            System.err.println(ex.getMessage());
        }

        req.setAttribute( "page-body", "home.jsp" ) ;
        req.getRequestDispatcher( "WEB-INF/_layout.jsp" )  // перенаправлення (внутрішнє)
                .forward(req, resp);
    }

    private String readStreamToEnd( InputStream inputStream ) throws IOException {
        final byte[] buffer = new byte[32 * 1024];  // 32k
        ByteArrayOutputStream byteBuilder = new ByteArrayOutputStream() ;
        int len ;
        while( ( len = inputStream.read( buffer ) ) > -1 ) {
            byteBuilder.write( buffer, 0, len ) ;
        }
        return byteBuilder.toString() ;
    }
}


/*
Сервлет - клас для мережних задач,
HttpServlet - аналог контролерів у веб-проєктах
*/