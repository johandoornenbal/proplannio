package domainapp.modules.project.dom;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.jdo.JDOQLTypedQuery;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.PromptStyle;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.persistence.jdo.applib.services.IsisJdoSupport_v3_2;

import domainapp.modules.project.ProjectModule;
import domainapp.modules.simple.types.Name;

@DomainService(
        nature = NatureOfService.VIEW,
        objectType = "project.Products"
        )
public class Products {

    private final RepositoryService repositoryService;
    private final IsisJdoSupport_v3_2 isisJdoSupport;

    @Inject
    public Products(RepositoryService repositoryService, IsisJdoSupport_v3_2 isisJdoSupport) {
        this.repositoryService = repositoryService;
        this.isisJdoSupport = isisJdoSupport;
    }

    public static class ActionDomainEvent extends ProjectModule.ActionDomainEvent<Products> {}

    public static class CreateActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, domainEvent = CreateActionDomainEvent.class)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Product create(
            final String reference,
            @Name final String name,
            final Project project,
            @Nullable final Product parent) {
        Product product = new Product();
        product.setReference(reference);
        product.setName(name);
        product.setProject(project);
        product.setParentProduct(parent);
        return repositoryService.persist(product);
    }

    public List<Project> autoComplete2Create(final String name){
        return projects.findByName(name);
    }

    public List<Product> autoComplete3Create(final String name){
        return products.findByName(name);
    }

    public List<Product> findByName(final String name) {
        JDOQLTypedQuery<Product> q = isisJdoSupport.newTypesafeQuery(Product.class);
        final QProduct cand = QProduct.candidate();
        q = q.filter(
                cand.name.indexOf(q.stringParameter("name")).ne(-1)
        );
        return q.setParameter("name", name)
                .executeList();
    }

    public static class FindByReferenceActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.SAFE, domainEvent = FindByReferenceActionDomainEvent.class)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Product findByReference(
            final String reference
            ) {
        JDOQLTypedQuery<Product> q = isisJdoSupport.newTypesafeQuery(Product.class);
        final QProduct cand = QProduct.candidate();
        q = q.filter(
                cand.reference.eq(q.stringParameter("reference"))
                );
        return q.setParameter("reference", reference)
                .executeUnique();
    }

    public static class ListAllActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<Product> listAll() {
        return repositoryService.allInstances(Product.class);
    }

    @Inject
    Projects projects;

    @Inject
    Products products;

}
