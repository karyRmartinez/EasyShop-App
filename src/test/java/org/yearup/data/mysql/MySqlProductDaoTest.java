package org.yearup.data.mysql;

import org.junit.jupiter.api.BeforeEach;

class MySqlProductDaoTest extends BaseDaoTestClass
{
    private MySqlProductDao dao;

    @BeforeEach
    public void setup()
    {
        dao = new MySqlProductDao(dataSource);
    }

//    @Test
//    public void getById_shouldReturn_theCorrectProduct()
//    {
//        // arrange
//        int productId = 1;
//        Product expected = new Product()
//        {{
//            setProductId(1);
//            setName("Smartphone");
//            setPrice(new BigDecimal("499.99"));
//            setCategoryId(1);
//            setDescription("A powerful and feature-rich smartphone for all your communication needs.");
//            setColor("Black");
//            setStock(50);
//            setFeatured(false);
//            setImageUrl("smartphone.jpg");
//        }};
//
//        // act
//        var actual = dao.getById(productId);
//
//        // assert
//        assertEquals(expected.getPrice(), actual.getPrice(), "Because I tried to get product 1 from the database.");
//    }

}