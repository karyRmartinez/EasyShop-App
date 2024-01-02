package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.HashMap;
import java.util.List;

// add the annotations to make this a REST controller -DONE✅
@RestController
// add the annotation to make this controller the endpoint for the following url ✅
@RequestMapping("/categories")
    // http://localhost:8080/categories
// add annotation to allow cross site origin requests✅
@CrossOrigin
public class CategoriesController
{
    private CategoryDao categoryDao;
    private ProductDao productDao;


    // create an Autowired controller to inject the categoryDao and ProductDao ✅
@Autowired
    // add the appropriate annotation for a get action ✅
    @GetMapping
    public List<Category> getAll()
    {
        // find and return all categories ✅
        return categoryDao.getAllCategories();
    }

    // add the appropriate annotation for a get action ✅
    @GetMapping("/{categoryId}")
    public Category getById(@PathVariable int id) {
        System.out.println("category id" + id);

        // get the category by id ✅
       // return null;
        return categoryDao.getById(id);

    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        // get a list of product by categoryId ✅
       // return null;
        return productDao.listByCategoryId(categoryId);
    }

    // add annotation to call this method for a POST action ✅
    @PostMapping()
    // add annotation to ensure that only an ADMIN can call this function
    public Category addCategory(@RequestBody Category category)
    {
        // insert the category ✅
        try{
            return categoryDao.create(category);
        }catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR!");
        }
    }

    // ✅ add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @PutMapping("{id}")
    public HashMap<String, String> updateCategory(@PathVariable int id, @RequestBody Category category) {
        // update the category by id ✅
    categoryDao.update(id, category);

    HashMap<String, String> response = new HashMap<>();

    response.put("Status", "Successful");
    response.put("Message", "Category Updated Successfully");

    return response;

}


    // ✅add annotation to call this method for a DELETE action - the url path must include the categoryId
    @DeleteMapping("{categoryId}")
    // add annotation to ensure that only an ADMIN can call this function
    public void deleteCategory(@PathVariable int id) {

        // delete the category by id
        try{
            var category = categoryDao.getById(id);

            if(category == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            categoryDao.delete(id);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "There was an ERROR!");
        }

    }
}
