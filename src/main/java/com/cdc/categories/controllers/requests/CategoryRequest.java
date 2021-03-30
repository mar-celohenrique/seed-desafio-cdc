package com.cdc.categories.controllers.requests;

import com.cdc.categories.entities.Category;
import com.cdc.commons.validations.UniqueValue;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
public class CategoryRequest {

    @NotBlank
    @UniqueValue(columnName = "name", domainClass = Category.class)
    private String name;

    public Category toModel() {
        return new Category(this.name);
    }

}
