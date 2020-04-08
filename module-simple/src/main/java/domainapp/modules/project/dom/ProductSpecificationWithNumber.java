package domainapp.modules.project.dom;

import javax.jdo.annotations.InheritanceStrategy;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.DomainObject;

import lombok.Getter;
import lombok.Setter;

@javax.jdo.annotations.PersistenceCapable(schema = "project")
@javax.jdo.annotations.Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@javax.jdo.annotations.Discriminator("domainapp.modules.project.dom.ProductSpecificationWithNumber")
@DomainObject
public class ProductSpecificationWithNumber extends ProductSpecification {

    @Getter @Setter
    private Integer number;

}
