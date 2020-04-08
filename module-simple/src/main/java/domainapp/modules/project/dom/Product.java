package domainapp.modules.project.dom;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.jaxbadapters.PersistentEntityAdapter;

import lombok.Getter;
import lombok.Setter;

@javax.jdo.annotations.PersistenceCapable(identityType= IdentityType.DATASTORE, schema = "project")
@javax.jdo.annotations.DatastoreIdentity(strategy= IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")
@javax.jdo.annotations.Unique(name="Product_reference_UNQ", members = {"reference"})
@DomainObject()
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
public class Product implements Comparable<Product> {

    public String title() {
        return "Product: " + getReference() + "-" + getName();
    }

    @Getter @Setter
    private Project project;

    @Getter @Setter
    private String reference;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private Product parentProduct;

    @Action(semantics = SemanticsOf.SAFE)
    public LocalDate getDeadLine(){
        if (getChildProducts().isEmpty()) {
            final ProductSpecification deadline = getSpecifications().stream()
                    .filter(s -> s.getType() == SpecificationType.DEADLINE).findFirst().orElse(null);
            if (deadline != null) {
                ProductSpecificationWithDate casted = (ProductSpecificationWithDate) deadline;
                return casted.getDate();
            }
        } else {
            final Optional<LocalDate> max = getChildProducts().stream().filter(p -> p.getDeadLine() != null)
                    .map(Product::getDeadLine).max(LocalDate::compareTo);
            return max.isPresent() ? max.get() : null;
        }
        return null;
    }

    @Action(semantics = SemanticsOf.SAFE)
    public Integer getEffortInDays(){
        if (getChildProducts().isEmpty()) {
            final ProductSpecification effort = getSpecifications().stream()
                    .filter(s -> s.getType() == SpecificationType.EFFORT_IN_WORKING_DAYS).findFirst().orElse(null);
            if (effort != null) {
                ProductSpecificationWithNumber casted = (ProductSpecificationWithNumber) effort;
                return casted.getNumber();
            }
        } else {
            return getChildProducts().stream().map(Product::getEffortInDays).reduce(0, Integer::sum);
        }
        return null;
    }

    @lombok.Getter @lombok.Setter
    @Persistent(mappedBy = "product", dependentElement = "true")
    private SortedSet<ProductSpecification> specifications = new TreeSet<>();

    @Action()
    public Product createDeadline(final LocalDate deadline){
        specificationWithDateRepository.create(this, SpecificationType.DEADLINE, deadline);
        return this;
    }

    public boolean hideCreateDeadline(){
        return this.getSpecifications().stream().filter(s->s.getType()==SpecificationType.DEADLINE).findFirst().isPresent();
    }

    @Action()
    public Product createEffort(final Integer numberOfDays){
        specificationWithNumberRepository.create(this, SpecificationType.EFFORT_IN_WORKING_DAYS, numberOfDays);
        return this;
    }

    public boolean hideCreateEffort(){
        return this.getSpecifications().stream().filter(s->s.getType()==SpecificationType.EFFORT_IN_WORKING_DAYS).findFirst().isPresent();
    }

    @Action(semantics = SemanticsOf.SAFE)
    public List<Product> getChildProducts(){
        return products.findChildren(this);
    }

    @Override
    public String toString() {
        return getReference();
    }

    private final static Comparator<Product> comparator =
            Comparator.comparing(Product::getReference);

    @Override
    public int compareTo(final Product other) {
        return comparator.compare(this, other);
    }

    @Inject
    ProductSpecificationWithDateRepository specificationWithDateRepository;

    @Inject
    ProductSpecificationWithNumberRepository specificationWithNumberRepository;

    @Inject
    Products products;

}
