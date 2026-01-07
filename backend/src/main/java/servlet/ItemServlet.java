package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/api/v1/item")
public class ItemServlet extends HttpServlet{
    BasicDataSource ds;
    
    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        ds = (BasicDataSource) servletContext.getAttribute("ds");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(req.getReader(), JsonObject.class);
        String id = jsonObject.get("iid").getAsString();
        String name = jsonObject.get("iname").getAsString();
        String description = jsonObject.get("iDesc").getAsString();

        try {
            Connection connection = ds.getConnection();
            String query = "INSERT INTO Items (id,name,description) VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, description);
            int rowInserted = preparedStatement.executeUpdate();
            if (rowInserted > 0) {
                resp.getWriter().println("Item Saved Successfully");
            } else {
                resp.getWriter().println("Item Saved Failed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp)
            throws jakarta.servlet.ServletException, java.io.IOException {
        try {
            Connection connection = ds.getConnection();
            String query = "SELECT * FROM Items";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            JsonArray itemList = new JsonArray();
            while (resultSet.next()) {
                String iId = resultSet.getString("id");
                String iName = resultSet.getString("name");
                String iDesc = resultSet.getString("description");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("iid", iId);
                jsonObject.addProperty("iname", iName);
                jsonObject.addProperty("iDesc", iDesc);
                itemList.add(jsonObject);
            }
            resp.getWriter().println(itemList);
            resp.setContentType("application/json");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(req.getReader(), JsonObject.class);
        String id = jsonObject.get("iid").getAsString();
        String name = jsonObject.get("iname").getAsString();
        String description = jsonObject.get("iDesc").getAsString();

        try {
            Connection connection = ds.getConnection();
            String query = "UPDATE Items SET name=?,description=? WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, id);
            int rowInserted = preparedStatement.executeUpdate();
            if (rowInserted > 0) {
                resp.getWriter().println("Item Updated Successfully");
            } else {
                resp.getWriter().println("Item Updated Failed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("iid");
        try {
            Connection connection = ds.getConnection();
            String query = "delete from Items WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);
            int rowInserted = preparedStatement.executeUpdate();
            if (rowInserted > 0) {
                resp.getWriter().println("Item Deleted Successfully");
            } else {
                resp.getWriter().println("Item Deleted Failed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
