package domainapp.modules.project.dom;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.PromptStyle;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.persistence.jdo.applib.services.IsisJdoSupport_v3_2;

@Service
public class ProductSpecificationWithNumberRepository {

    private final RepositoryService repositoryService;
    private final IsisJdoSupport_v3_2 isisJdoSupport;

    @Inject
    public ProductSpecificationWithNumberRepository(RepositoryService repositoryService, IsisJdoSupport_v3_2 isisJdoSupport) {
        this.repositoryService = repositoryService;
        this.isisJdoSupport = isisJdoSupport;
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public ProductSpecificationWithNumber create(
            final Product product,
            final SpecificationType type,
            final Integer number) {
        ProductSpecificationWithNumber spec = new ProductSpecificationWithNumber();
        spec.setProduct(product);
        spec.setType(type);
        spec.setNumber(number);
        return repositoryService.persist(spec);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<ProductSpecificationWithNumber> listAll() {
        return repositoryService.allInstances(ProductSpecificationWithNumber.class);
    }

}
