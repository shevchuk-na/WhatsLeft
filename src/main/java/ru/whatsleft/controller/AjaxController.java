package ru.whatsleft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.whatsleft.domain.Product;
import ru.whatsleft.domain.User;
import ru.whatsleft.domain.ajax.AjaxCategoryList;
import ru.whatsleft.domain.ajax.AjaxChange;
import ru.whatsleft.service.CategoryService;
import ru.whatsleft.service.ProductService;
import ru.whatsleft.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/ajax")
public class AjaxController {

    private final UserService userService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final HomeController homeController;

    @Autowired
    public AjaxController(UserService userService, ProductService productService, CategoryService categoryService, HomeController homeController) {
        this.userService = userService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.homeController = homeController;
    }

    @RequestMapping(value = "/newChange", method = RequestMethod.POST)
    public ResponseEntity<?> newChangePost(Principal principal, @RequestBody AjaxChange ajaxChange) {
        if (principal != null) {
            User user = userService.findByUsername(principal.getName());
            Product product = productService.findById(ajaxChange.getProductId());
            if (user.getLeader().getId().equals(product.getTeamLeader().getId())) {
                homeController.applyChange(product, ajaxChange.getChangeAmount(), user);
                return ResponseEntity.ok(new AjaxCategoryList(homeController.prepareTeamCategories(user.getLeader().getId())));
            } else {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }
}
