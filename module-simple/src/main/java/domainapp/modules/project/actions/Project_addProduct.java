package domainapp.modules.project.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.isis.applib.annotation.*;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.jdo.annotations.*;

import domainapp.modules.project.dom.Product;
import domainapp.modules.project.dom.Products;
import domainapp.modules.project.dom.Project;
import lombok.Getter;
import lombok.Setter;

@Mixin
public class Project_addProduct {

    private final Project project;

    public Project_addProduct(Project project) {
        this.project = project;
    }

    @Action()
    public Project $$(final String reference, final String name, @Nullable final Product parent) {
        products.create(reference, name, project,parent);
        return project;
    }

    public List<Product> choices2$$(){
        return new ArrayList<>(project.getProducts());
    }

    @Inject
    Products products;

}
