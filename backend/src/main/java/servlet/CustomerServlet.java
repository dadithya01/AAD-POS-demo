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

@WebServlet(urlPatterns = "/api/v1/customer")
public class CustomerServlet extends HttpServlet {
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
        String id = jsonObject.get("cid").getAsString();
        String name = jsonObject.get("cname").getAsString();
        String address = jsonObject.get("caddress").getAsString();

        try {
            Connection connection = ds.getConnection();
            String query = "INSERT INTO Customers (id,name,address) VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, address);
            int rowInserted = preparedStatement.executeUpdate();
            if (rowInserted > 0) {
                resp.getWriter().println("Customer Saved Successfully");
            } else {
                resp.getWriter().println("Customer Saved Failed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doGet(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp)
            throws jakarta.servlet.ServletException, java.io.IOException {
        try {
            Connection connection = ds.getConnection();
            String query = "SELECT * FROM Customers";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            JsonArray customerList = new JsonArray();
            while (resultSet.next()) {
                String cId = resultSet.getString("id");
                String cName = resultSet.getString("name");
                String cAddress = resultSet.getString("address");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("cid", cId);
                jsonObject.addProperty("cname", cName);
                jsonObject.addProperty("caddress", cAddress);
                customerList.add(jsonObject);
            }
            resp.getWriter().println(customerList);
            resp.setContentType("application/json");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(req.getReader(), JsonObject.class);
        String id = jsonObject.get("cid").getAsString();
        String name = jsonObject.get("cname").getAsString();
        String address = jsonObject.get("caddress").getAsString();

        try {
            Connection connection = ds.getConnection();
            String query = "UPDATE Customers SET name=?,address=? WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, id);
            int rowInserted = preparedStatement.executeUpdate();
            if (rowInserted > 0) {
                resp.getWriter().println("Customer Updated Successfully");
            } else {
                resp.getWriter().println("Customer Updated Failed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("cid");
        try {
            Connection connection = ds.getConnection();
            String query = "delete from Customers WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);
            int rowInserted = preparedStatement.executeUpdate();
            if (rowInserted > 0) {
                resp.getWriter().println("Customer Deleted Successfully");
            } else {
                resp.getWriter().println("Customer Deleted Failed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
