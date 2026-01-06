import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.commons.dbcp2.BasicDataSource;

@WebListener
public class ContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
        System.out.println("contextInitialized");
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/javaeeapp");
        ds.setUsername("adithya");
        ds.setPassword("2006");
        ds.setInitialSize(5);
        ds.setMaxTotal(5);

        ServletContext ctx = event.getServletContext();
        ctx.setAttribute("ds", ds);


    }

    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("contextDestroyed");
    }
}