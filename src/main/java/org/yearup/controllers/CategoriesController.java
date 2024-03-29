package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

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

@Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    // create an Autowired controller to inject the categoryDao and ProductDao ✅


    // add the appropriate annotation for a get action ✅
    @GetMapping
    public List<Category> getAll()
    {
        // find and return all categories ✅
        return categoryDao.getAllCategories();
    }

    // add the appropriate annotation for a get action ✅
    @GetMapping("/{id}")
    public Category getById(@PathVariable int id, HttpServletResponse response) {


        // get the category by id ✅
       // return null;
//        return categoryDao.getById(id);
        {
            // get the category by id
            Category c = categoryDao.getById(id);
            if(c == null) {
                response.setStatus(NOT_FOUND.value());
            }
            else{
                response.setStatus(OK.value());
            }
            return categoryDao.getById(id);
        }

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

    @RequestMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    // add annotation to ensure that only an ADMIN can call this function
    public Category addCategory(@RequestBody Category category, HttpServletResponse response)
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(path="/categories/{id}",method = RequestMethod.PUT)
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateCategory(@PathVariable int id, @RequestBody Category category) {

       try {
            categoryDao.update(id, category);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR!");
        }
    }


    // ✅add annotation to call this method for a DELETE action - the url path must include the categoryId
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // add annotation to ensure that only an ADMIN can call this function
    public void deleteCategory(@PathVariable int id) {

        // delete the category by id
        try{
            var category = categoryDao.getById(id);

            if(category == null)
                throw new ResponseStatusException(NOT_FOUND);

            categoryDao.delete(id);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR!");
        }

    }
}
