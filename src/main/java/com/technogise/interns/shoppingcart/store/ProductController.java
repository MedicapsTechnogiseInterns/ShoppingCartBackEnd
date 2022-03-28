package com.technogise.interns.shoppingcart.store;
import com.technogise.interns.shoppingcart.dto.Product;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController {
    final List<Product> productList = new ArrayList<>();
    @GetMapping("/products")
    @ApiOperation(value = "Get all the products",
            notes = "Returns all the products from the shopping cart",
            response = Product.class)
    public ResponseEntity<CollectionModel<EntityModel<Product>>>getAllProducts() {
        List<EntityModel<Product>> entityModelList= new ArrayList<>();
        for(Product product : productList){
            EntityModel<Product> resource = EntityModel.of(product);
            WebMvcLinkBuilder linkToSelf = linkTo(methodOn(this.getClass()).getProduct(product.getId()));
            resource.add(linkToSelf.withSelfRel());
            entityModelList.add(resource);
        }
        CollectionModel<EntityModel<Product>> resourceList = CollectionModel.of(entityModelList);
        WebMvcLinkBuilder linkToSelf = linkTo(methodOn(this.getClass()).getAllProducts());
        resourceList.add(linkToSelf.withSelfRel());
        return ResponseEntity.ok().cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS)).body(resourceList);
    }

    @GetMapping("/products/{id}")
    @ApiOperation(value = "Get a single product by id",
            notes = "Returns a single product. Use the id to get the desired product.",
            response = Product.class)
    public EntityModel<Product> getProduct(@ApiParam(value = "Enter Id of the product to be returned", required = true) @PathVariable(value = "id")UUID productId) {
        Product product1 = findById(productId);
        EntityModel<Product> resource = EntityModel.of(product1);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).getAllProducts());
        WebMvcLinkBuilder linkToSelf = linkTo(methodOn(this.getClass()).getProduct(productId));

        resource.add(linkTo.withRel("all-products"));
        resource.add(linkToSelf.withSelfRel());

        return resource;
        //return new ResponseEntity<>(product1, HttpStatus.OK);
    }
    @PostMapping(path = "/products")
    @ApiOperation(value = "Create new product",
            notes = "Creates product and add it in the shopping cart.Add the attributes of the new product. Any attribute of product if not added ,by default " +
                    "null value will be stored. Id will be auto-generated, so no need to add it.",
            response = Product.class)
    public EntityModel<Product> createProduct(@ApiParam(value = "Enter new product", required = true)@RequestBody Product newProduct) {
        newProduct.setId(UUID.randomUUID());
        productList.add(newProduct);
        EntityModel<Product> resource = EntityModel.of(newProduct);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).getAllProducts());
        WebMvcLinkBuilder linkToGetSelf = linkTo(methodOn(this.getClass()).getProduct(newProduct.getId()));

        resource.add(linkTo.withRel("all-products"));
        resource.add(linkToGetSelf.withSelfRel());

        return resource;
        //return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }
    @PutMapping("/products/{id}")
    @ApiOperation(value = "Update a product by id",
            notes = "Returns an updated product. Provide an id to Update specific product in the shopping cart and " +
                    "only specify the attributes which are to be updated, rest fields will remain unchanged" ,
            response = Product.class)
    public ResponseEntity<Product> replaceCustomer(@ApiParam(value = "Enter product attributes to be updated.") @RequestBody Product newProduct, @ApiParam(value = "Enter id of the product to be updated.", required = true) @PathVariable(value = "id")UUID productId)
    {
        Product product = findById(productId);
        if (product != null)
        {
            if(newProduct.getName() != null)
                product.setName(newProduct.getName());
            if(newProduct.getImage() != null)
                product.setImage(newProduct.getImage());
            if(newProduct.getPrice() != null)
                product.setPrice(newProduct.getPrice());
            if(newProduct.getDescription() != null)
                product.setDescription(newProduct.getDescription());
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/products/{id}")
    @ApiOperation(value = "Delete a product by id",
            notes = "Returns OK status on successfully deletion of the product. Use the Id of the specific product to delete " +
                    "it and if Id doesn't match status NOT_found will be returned.",
            response = Product.class)
    public ResponseEntity<HttpStatus> deleteProduct(@ApiParam(value = "Enter the id of product to be deleted.", required = true) @PathVariable(value = "id") UUID productID) {
        Product product = findById(productID);
        if (product != null) {
            productList.remove(product);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    public Product findById(UUID productID){
        for(Product product : productList )
        {
            if (productID.equals(product.getId()))
            {return product;}
        }
        return null;
    }
}