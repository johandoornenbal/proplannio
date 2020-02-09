package domainapp.modules.project.dom;

import java.util.Comparator;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.jaxbadapters.PersistentEntityAdapter;

import lombok.Getter;
import lombok.Setter;

@javax.jdo.annotations.PersistenceCapable(identityType= IdentityType.DATASTORE, schema = "simple")
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
    private Product parent;

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

}
