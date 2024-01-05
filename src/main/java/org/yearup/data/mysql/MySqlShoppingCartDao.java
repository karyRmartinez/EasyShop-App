package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    ProductDao productDao;

    @Autowired
    public MySqlShoppingCartDao(DataSource dataSource, ProductDao productDao) {
        super(dataSource);
        this.productDao = productDao;
    }

    @Override
    public ShoppingCart getByUserId(int userId) {

        ShoppingCart sc = new ShoppingCart();

        String sql = """
                select * from shopping_cart
                where user_id = ?;
                """;

        try (Connection connection = getConnection()) {
            PreparedStatement s = connection.prepareStatement(sql);
            s.setInt(1, userId);
            ResultSet rs = s.executeQuery();

            while(rs.next()){
                // create a shoppingcartitem
                ShoppingCartItem sci = new ShoppingCartItem();

                // put product in the shoppingcartitem
                Product p = productDao.getById(rs.getInt("product_id"));
                int q = rs.getInt("quantity");

                sci.setProduct(p);
                sci.setQuantity(q);
                sc.add(sci);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sc;
    }

    @Override
    public void addItem(int userId, int productId, int quantity) {

        String sql = """
                insert into shopping_cart
                values(?, ?, ?);
                """;

        try (Connection connection = getConnection()) {
            PreparedStatement s = connection.prepareStatement(sql);
            s.setInt(1, userId);
            s.setInt(2, productId);
            s.setInt(3, quantity);

            int row = s.executeUpdate();

            System.out.println(row);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int setQuantity(int userId, int productId, int quantity) {
        int q = 0;

        String sql = """
                update shopping_cart
                set quantity = ?
                where user_id = ? and product_id = ?;
                """;

        try (Connection connection = getConnection()) {
            PreparedStatement s = connection.prepareStatement(sql);
            s.setInt(1, quantity);
            s.setInt(2, userId);
            s.setInt(3, productId);

            q = s.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return q;
    }

    @Override
    public void clearCart(int userId) {

    }

}