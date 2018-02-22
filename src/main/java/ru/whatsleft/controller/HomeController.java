package ru.whatsleft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.DateUtils;
import ru.whatsleft.domain.*;
import ru.whatsleft.domain.security.Role;
import ru.whatsleft.domain.security.RoleEnum;
import ru.whatsleft.repository.RoleRepository;
import ru.whatsleft.service.*;
import ru.whatsleft.utility.SecurityUtility;
import ru.whatsleft.utility.comparator.ChangeComparatorByDateDecs;
import ru.whatsleft.utility.comparator.CommentComparatorByDateDecs;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final ChangeService changeService;
    private final CommentService commentService;
    private final RequestService requestService;

    @Autowired
    public HomeController(UserService userService, RoleRepository roleRepository, CategoryService categoryService, ProductService productService, ChangeService changeService,
                          CommentService commentService, RequestService requestService) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.categoryService = categoryService;
        this.productService = productService;
        this.changeService = changeService;
        this.commentService = commentService;
        this.requestService = requestService;
    }


    @RequestMapping("")
    public String home(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model = prepareHomeModel(model, user);
        return "home";
    }

    private Model prepareHomeModel(Model model, User user) {
        model.addAttribute("user", user);
        if (!userIsAdmin(user)) {
            List<User> teamList;
            List<Category> teamCategories;
            if (userIsLeader(user)) {
                model.addAttribute("userIsLeader", true);
                teamList = new ArrayList<>(userService.findByLeader(user));
                if (!model.containsAttribute("teamCategories")) {
                    teamCategories = prepareTeamCategories(user.getId());
                    model.addAttribute("teamCategories", teamCategories);
                }
            } else {
                teamList = new ArrayList<>(userService.findByLeader(user.getLeader()));
                if (!model.containsAttribute("teamCategories")) {
                    teamCategories = prepareTeamCategories(user.getLeader().getId());
                    model.addAttribute("teamCategories", teamCategories);
                }
            }
            model.addAttribute("teamList", teamList);
            model.addAttribute("undoDisabled", true);
        } else {
            model.addAttribute("userIsAdmin", true);
            List<Request> requestList = requestService.findAllRequests();
            model.addAttribute("requestList", requestList);
        }
        return model;
    }

    List<Category> prepareTeamCategories(Long id) {
        List<Category> teamCategories = categoryService.findByTeamLeaderId(id);
        List<Category> categoriesToRemove = new ArrayList<>();
        for (Category category : teamCategories) {
            if (category.getProducts() == null || category.getProducts().size() == 0) {
                categoriesToRemove.add(category);
            } else {
                List<Product> productsToRemove = new ArrayList<>();
                for (Product product : category.getProducts()) {
                    if (product.getAmount() == 0) {
                        productsToRemove.add(product);
                    }
                }
                category.getProducts().removeAll(productsToRemove);
            }
        }
        teamCategories.removeAll(categoriesToRemove);
        return teamCategories;
    }

    @RequestMapping("newUser")
    public String newUser(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        if (userIsAdmin(user)) {
            model.addAttribute("userIsAdmin", true);
        } else if (userIsLeader(user)) {
            model.addAttribute("userIsLeader", true);
        } else {
            model.addAttribute("unauthorized", true);
            return "index";
        }
        model.addAttribute("userCreated", false);
        model.addAttribute("usernameExists", false);
        model.addAttribute("noRoleSelected", false);
        return "newUser";
    }

    @RequestMapping(value = "/newUser", method = RequestMethod.POST)
    public String newUserPost(
            Principal principal,
            HttpServletRequest request,
            @ModelAttribute("username") String username,
            @ModelAttribute("name") String name,
            @ModelAttribute("password") String password,
            @ModelAttribute("userRole") String userRole,
            Model model,
            RedirectAttributes redirect
    ) {
        User user = userService.findByUsername(principal.getName());
        User newUser;
        try {
            if (userIsAdmin(user)) {
                if (userService.findByUsername(username) != null) {
                    redirect.addFlashAttribute("usernameExists", true);
                    return "redirect:newUser";
                } else {
                    switch (userRole) {
                        case "admin":
                            Role roleAdmin = roleRepository.findByName(RoleEnum.ROLE_ADMIN);
                            newUser = createUser(roleAdmin, username, name, password, null);
                            break;
                        case "leader":
                            Role roleLeader = roleRepository.findByName(RoleEnum.ROLE_LEADER);
                            newUser = createUser(roleLeader, username, name, password, null);
                            Category category = new Category();
                            category.setName(" --- ");
                            category.setTeamLeader(newUser);
                            category = categoryService.save(category);
                            break;
                        default:
                            redirect.addFlashAttribute("noRoleSelected", true);
                            return "redirect:newUser";
                    }
                }
            } else if (userIsLeader(user)) {
                if (userService.findByUsername(username) != null) {
                    redirect.addFlashAttribute("usernameExists", true);
                    return "redirect:newUser";
                } else {
                    newUser = createUser(null, username, name, password, user);
                }
            } else {
                redirect.addFlashAttribute("unauthorized", true);
                return "home";
            }
        } catch (Exception e) {
            if (e.getMessage() != null) {
                if (e.getMessage().equals("username taken")) {
                    model.addAttribute("usernameExists", true);
                }
            }
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "redirect:newUser";
        }
        redirect.addFlashAttribute("newUser", newUser);
        return "redirect:newUser";
    }

    @RequestMapping(value = "createNewCategory", method = RequestMethod.POST)
    public String createNewCategoryPost(Model model, @ModelAttribute("category") String name, Principal principal, RedirectAttributes redirect) {
        User user = userService.findByUsername(principal.getName());
        if (user.getLeader() == null) {
            redirect.addFlashAttribute("noTeamAssigned", true);
        } else if (name.isEmpty()) {
            redirect.addFlashAttribute("categoryNameIsEmpty", true);
        } else {
            Category category = new Category();
            category.setName(name);
            category.setTeamLeader(user.getLeader());
            category = categoryService.save(category);
            redirect.addFlashAttribute("categoryCreated", category);
        }
        return "redirect:createNewProduct";
    }

    @RequestMapping(value = "deleteCategory", method = RequestMethod.POST)
    public String deleteCategoryPost(Model model, @ModelAttribute("categoryId") Long categoryId, Principal principal, RedirectAttributes redirect) {
        User user = userService.findByUsername(principal.getName());
        if (userIsLeader(user)) {
            Category categoryToDelete = categoryService.findById(categoryId);
            if (categoryToDelete != null) {
                categoryService.delete(categoryToDelete.getId());
                redirect.addFlashAttribute("categoryDeleted", categoryToDelete);

            }
        }
        return "redirect:createNewProduct";
    }

    @RequestMapping(value = "createNewProduct")
    public String createNewProduct(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        if (!userIsAdmin(user)) {
            model.addAttribute("teamCategories", categoryService.findByTeamLeaderId(user.getLeader().getId()));
            model = prepareHomeModel(model, user);
        }
        return "newProduct";
    }

    @RequestMapping(value = "createNewProduct", method = RequestMethod.POST)
    public String createNewProductPost(Model model,
                                       @ModelAttribute("productName") String name,
                                       @ModelAttribute("productCategory") String categoryId,
                                       @ModelAttribute("productAmount") String amount,
                                       @ModelAttribute("productDefaultChange") String defaultChange,
                                       Principal principal,
                                       RedirectAttributes redirect) {
        User user = userService.findByUsername(principal.getName());
        Category productCategory = categoryService.findById(Long.parseLong(categoryId));
        Product product = productService.findByName(name);
        if (product == null) {
            Product newProduct = new Product();
            newProduct.setName(name);
            newProduct.setCategory(productCategory);
            newProduct.setAmount(Integer.parseInt(amount.replaceAll("[^\\d-]", "")));
            newProduct.setDefaultChange(Integer.parseInt(defaultChange.replaceAll("[^\\d-]", "")));
            newProduct.setTeamLeader(user.getLeader());
            newProduct = productService.save(newProduct);
            Change initialChange = new Change();
            initialChange.setUser(user);
            initialChange.setProduct(newProduct);
            initialChange.setChangeAmount(newProduct.getAmount());
            initialChange.setRemainingAmount(newProduct.getAmount());
            initialChange = changeService.save(initialChange);
            redirect.addFlashAttribute("productCreated", newProduct);
        } else {
            redirect.addFlashAttribute("productExists", true);
        }
        return "redirect:createNewProduct";
    }

    @RequestMapping(value = "undoLastChange", method = RequestMethod.POST)
    public String undoLastChangePost(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Change lastChange = changeService.findLastByUserId(user.getId());
        Product lastProduct = lastChange.getProduct();
        lastProduct.setAmount(lastProduct.getAmount() - lastChange.getChangeAmount());
        lastProduct = productService.save(lastProduct);
        changeService.delete(lastChange);
        return "redirect:";
    }

    @RequestMapping(value = "product")
    public String product(Model model, @RequestParam("id") Long id, @RequestParam("fullChangeList") Optional<Integer> fullChangeList, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Product product = productService.findById(id);
        if (product.getTeamLeader().equals(user.getLeader())) {
            if (!fullChangeList.isPresent() || fullChangeList.get() == 0) {
                product.setChangeList(changeService.findLast10ByProductId(product.getId()));
                model.addAttribute("shortList", true);
            }
            if (product.getChangeList().size() < 10 || !model.containsAttribute("shortList")) {
                model.addAttribute("shortList", false);
            }
            product.getChangeList().sort(ChangeComparatorByDateDecs.getInstance());
            product.getCommentList().sort(CommentComparatorByDateDecs.getInstance());
            model = prepareHomeModel(model, user);
            model.addAttribute("product", product);
            return "product";
        } else {
            model.addAttribute("unauthorized", true);
            return "redirect:";

        }
    }

    @RequestMapping(value = "/newComment", method = RequestMethod.POST)
    public String newCommentPost(Model model, @ModelAttribute("id") String productId, @ModelAttribute("commentText") String commentText, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Product product = productService.findById(Long.parseLong(productId));
        Comment newComment = new Comment();
        newComment.setUser(user);
        newComment.setProduct(product);
        newComment.setText(commentText);
        newComment.setCreated(DateUtils.createNow().toInstant());
        newComment = commentService.save(newComment);
        return "redirect:product?id=" + product.getId();
    }

    @RequestMapping(value = "editComment")
    public String editCommentPost(Model model, @ModelAttribute("id") String id, @ModelAttribute("commentText") String commentText, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Comment comment = commentService.findById(Long.parseLong(id));
        if (!user.getId().equals(comment.getUser().getId())) {
            model.addAttribute("unauthorized", true);
        } else {
            if (!commentText.isEmpty()) {
                comment.setText(commentText);
                comment = commentService.save(comment);
            }
        }
        return "redirect:product?id=" + comment.getProduct().getId();
    }

    @RequestMapping(value = "deleteComment")
    public String deleteComment(Model model, @RequestParam("id") String id, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Comment comment = commentService.findById(Long.parseLong(id));
        if (!user.getId().equals(comment.getUser().getId())) {
            model.addAttribute("unauthorized", true);
        } else {
            commentService.delete(comment.getId());
        }
        return "redirect:product?id=" + comment.getProduct().getId();
    }

    Change applyChange(Product product, int changeAmount, User user) {
        if (changeAmount != 0) {
            Change change = new Change();
            change.setUser(user);
            change.setProduct(product);
            change.setChangeAmount(changeAmount);
            int remainingAmount = product.getAmount() + changeAmount;
            change.setRemainingAmount(remainingAmount);
            product.setAmount(remainingAmount);
            product = productService.save(product);
            change = changeService.save(change);
            return change;
        }
        return null;
    }

    private User createUser(Role specialRole, String username, String name, String password, User leader) throws Exception {
        User user = new User();
        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
        user.setPassword(encryptedPassword);
        user.setUsername(username);
        user.setName(name);
        Role roleUser = roleRepository.findByName(RoleEnum.ROLE_USER);
        user.getRoles().add(roleUser);
        if (specialRole == null) {
            user.setLeader(leader);
        } else {
            switch (specialRole.getName()) {
                case ROLE_ADMIN:
                    user.getRoles().add(specialRole);
                    break;
                case ROLE_LEADER:
                    user.getRoles().add(specialRole);
                    user.setLeader(user);
                    break;
            }
        }
        user = userService.createUser(user);
        return user;
    }

    private boolean userIsAdmin(User user) {
        boolean isAdmin = false;
        for (Role role : user.getRoles()) {
            if (role.getName().equals(RoleEnum.ROLE_ADMIN)) {
                isAdmin = true;
                break;
            }
        }
        return isAdmin;
    }

    private boolean userIsLeader(User user) {
        boolean isLeader = false;
        for (Role role : user.getRoles()) {
            if (role.getName().equals(RoleEnum.ROLE_LEADER)) {
                isLeader = true;
                break;
            }
        }
        return isLeader;
    }
}
