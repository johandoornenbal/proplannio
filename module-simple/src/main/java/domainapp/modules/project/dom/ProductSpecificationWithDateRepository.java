package domainapp.modules.project.dom;

import java.util.List;

import javax.inject.Inject;
import javax.jdo.JDOQLTypedQuery;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

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

@Service
public class ProductSpecificationWithDateRepository {

    private final RepositoryService repositoryService;
    private final IsisJdoSupport_v3_2 isisJdoSupport;

    @Inject
    public ProductSpecificationWithDateRepository(RepositoryService repositoryService, IsisJdoSupport_v3_2 isisJdoSupport) {
        this.repositoryService = repositoryService;
        this.isisJdoSupport = isisJdoSupport;
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public ProductSpecificationWithDate create(
            final Product product,
            final SpecificationType type,
            final LocalDate date) {
        ProductSpecificationWithDate spec = new ProductSpecificationWithDate();
        spec.setProduct(product);
        spec.setType(type);
        spec.setDate(date);
        return repositoryService.persist(spec);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<Project> listAll() {
        return repositoryService.allInstances(Project.class);
    }

}
