package org.yearup.data.mysql;


import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    //TO DO: ✅
    @Override
    public List<Category> getAllCategories() {
        // get all categories
        List<Category> categories = new ArrayList<>();
        String query = "Select * From categories;";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {

//                Category category = categoryShaper(resultSet);
                Category category = new Category(resultSet.getInt("category_id"), resultSet.getString("name"), resultSet.getString("description"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId) {
        // get category by id
        Category category = null;
        String sql = """
                SELECT * FROM categories
                WHERE category_id = ?;
                """;
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                category = mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return category;
    }

    @Override
    public Category create(Category category) {
        // create a new category
        String sql = """
                INSERT INTO categories(name, description)
                VALUES(?, ?);
                """;
        try(Connection connection =  getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the generated keys
                ResultSet generatedKeys = ps.getGeneratedKeys();

                if (generatedKeys.next()) {
                    // Retrieve the auto-incremented ID
                    int categoryId = generatedKeys.getInt(1);

                    // get the newly inserted category
                    category.setCategoryId(categoryId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return category;
    }
        @Override
//        @PutMapping
        public void update( int categoryId, Category category){
            // update category
            String query = """
                    Update categories Set name=?, description=? Where category_id=?;
                    """;
            try (
                    Connection connection = dataSource.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
            ) {
                preparedStatement.setString(1, category.getName());
                preparedStatement.setString(2, category.getDescription());
                preparedStatement.setInt(3, categoryId);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
              throw new RuntimeException(e);
            }
        }

        @Override
       //
        public void delete(int categoryId){
            // delete category
            String sql = """
                DELETE FROM categories
                WHERE category_id = ?;
                """;
            try (Connection c = getConnection()) {
                PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, categoryId);

                ps.executeUpdate();
                // Executes the given SQL statement, which may be an INSERT, UPDATE, or DELETE statement
                // or an SQL statement that returns nothing, such as an SQL DDL statement.

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
//            String query = "Delete From categories Where category_id=?;";
//
//            try (
//                    Connection connection = dataSource.getConnection();
//                    PreparedStatement preparedStatement = connection.prepareStatement(query);
//            ) {
//                preparedStatement.setInt(1, categoryId);
//                preparedStatement.executeUpdate();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }

        }

        private Category mapRow(ResultSet row) throws SQLException {
            int categoryId = row.getInt("category_id");
            String name = row.getString("name");
            String description = row.getString("description");

            Category category = new Category() {{
                setCategoryId(categoryId);
                setName(name);
                setDescription(description);
            }};

            return category;
        }
        public Category categoryShaper (ResultSet resultSet) throws SQLException {

            int categoryId = resultSet.getInt("category_id");
            String categoryName = resultSet.getString("name");
            String categoryDesc = resultSet.getString("description");

            Category category = new Category(categoryId, categoryName, categoryDesc);
            return category;
        }

    }
