package domainapp.modules.project.fixture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.services.factory.FactoryService;
import org.apache.isis.testing.fixtures.applib.fixturescripts.BuilderScriptWithResult;

import domainapp.modules.project.actions.Project_addProduct;
import domainapp.modules.project.dom.Product;
import domainapp.modules.project.dom.Products;
import domainapp.modules.project.dom.Project;
import domainapp.modules.project.dom.Projects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class ProjectBuilder extends BuilderScriptWithResult<Project> {

    @Getter @Setter
    private String name;

    @Getter @Setter
    private List<Project_persona.ProductSpec> productSpecs = new ArrayList<>();

    @Override
    protected Project buildResult(final ExecutionContext ec) {
        
        checkParam("name", ec, String.class);
        checkParam("productSpecs", ec, List.class);

        final Project project = wrap(projects).create(name);
        productSpecs.forEach(ps->{
            Product parentProduct = null;
            if (ps.getParentReference()!=null){
                parentProduct = project.getProducts().stream()
                        .filter(p -> p.getReference().equals(ps.getParentReference())).findFirst().orElse(null);
            }
            final Product product = products.create(ps.getReference(), ps.getName(), project, parentProduct);
            if (ps.getDeadline()!=null){
                product.createDeadline(ps.getDeadline());
            }
            if (ps.getEffortInWorkingDays()!=null){
                product.createEffort(ps.getEffortInWorkingDays());
            }
        });

        return project;
    }
    
    // -- DEPENDENCIES

    @Inject Projects projects;

    @Inject Products products;

}
