package domainapp.modules.project.dom;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.jaxbadapters.PersistentEntityAdapter;

import lombok.Getter;
import lombok.Setter;

@javax.jdo.annotations.PersistenceCapable(identityType= IdentityType.DATASTORE, schema = "project")
@javax.jdo.annotations.Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@javax.jdo.annotations.Discriminator(
        strategy = DiscriminatorStrategy.VALUE_MAP,
        column = "discriminator",
        value = "domainapp.modules.project.dom.ProductSpecification"
)
@javax.jdo.annotations.DatastoreIdentity(strategy= IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")
@javax.jdo.annotations.Unique(name="ProductSpecification_product_type_UNQ", members = {"product","type"})
@DomainObject()
public abstract class ProductSpecification implements Comparable<ProductSpecification> {

    public ProductSpecification(){

    }

    @Getter @Setter
    private Product product;

    @Getter @Setter
    private SpecificationType type;

    private final static Comparator<ProductSpecification> comparator =
            Comparator.comparing(ProductSpecification::getProduct).thenComparing(ProductSpecification::getType);

    @Override
    public int compareTo(final ProductSpecification other) {
        return comparator.compare(this, other);
    }

}
