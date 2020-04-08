package domainapp.modules.project;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import org.apache.isis.testing.fixtures.applib.fixturescripts.FixtureScript;
import org.apache.isis.testing.fixtures.applib.modules.ModuleWithFixtures;
import org.apache.isis.testing.fixtures.applib.teardown.TeardownFixtureAbstract;

import domainapp.modules.project.dom.Product;
import domainapp.modules.project.dom.ProductSpecificationWithDate;
import domainapp.modules.project.dom.ProductSpecificationWithNumber;
import domainapp.modules.project.dom.Project;
import domainapp.modules.simple.dom.so.SimpleObject;

@Configuration
@Import({})
@ComponentScan
public class ProjectModule implements ModuleWithFixtures {

    @Override
    public FixtureScript getTeardownFixture() {
        return new TeardownFixtureAbstract() {
            @Override
            protected void execute(ExecutionContext executionContext) {
                deleteFrom(ProductSpecificationWithNumber.class);
                deleteFrom(ProductSpecificationWithDate.class);
                deleteFrom(Product.class);
                deleteFrom(Project.class);
            }
        };
    }

    public static class PropertyDomainEvent<S,T>
    extends org.apache.isis.applib.events.domain.PropertyDomainEvent<S,T> {}
    
    public static class CollectionDomainEvent<S,T>
    extends org.apache.isis.applib.events.domain.CollectionDomainEvent<S,T> {}
    
    public static class ActionDomainEvent<S> extends
    org.apache.isis.applib.events.domain.ActionDomainEvent<S> {}
    
}
