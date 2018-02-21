package ru.whatsleft.domain.ajax;

import ru.whatsleft.domain.Category;

import java.util.ArrayList;
import java.util.List;

public class AjaxCategoryList {

    private List<AjaxCategory> result = new ArrayList<>();

    public AjaxCategoryList(List<Category> categories) {
        for (Category category : categories) {
            AjaxCategory ajaxCategory = new AjaxCategory(category);
            result.add(ajaxCategory);
        }
    }

    public List<AjaxCategory> getResult() {
        return result;
    }

    public void setResult(List<AjaxCategory> result) {
        this.result = result;
    }
}
