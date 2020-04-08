package domainapp.modules.project.integtests.tests;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.services.wrapper.DisabledException;
import org.apache.isis.applib.services.wrapper.InvalidException;
import org.apache.isis.persistence.jdo.datanucleus5.jdosupport.mixins.Persistable_datanucleusIdLong;

import domainapp.modules.project.dom.Product;
import domainapp.modules.project.dom.Products;
import domainapp.modules.project.dom.Project;
import domainapp.modules.project.dom.Projects;
import domainapp.modules.project.fixture.Project_persona;
import domainapp.modules.project.integtests.ProjectModuleIntegTestAbstract;
import lombok.Getter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
public class Product_IntegTest extends ProjectModuleIntegTestAbstract {

    @Inject Projects projects;

    @Inject Products products;

    Project project;
    Product product;

    @BeforeEach
    public void setUp() {
        // given
        project = projects.create("Project");
        product = products.create("PD1", "Product 1", project, null);

    }

    public static class children extends Product_IntegTest {

        @Test
        public void childrenWorks(){

            // given, when, then
            assertThat(product.getChildProducts()).isEmpty();

            // and when
            final Product child1 = products.create("PD1A", "Product 1A", project, product);
            final Product child2 = products.create("PD1B", "Product 1B", project, product);

            // then
            assertThat(product.getChildProducts()).hasSize(2);

            // and when
            final Product child1_1 = products.create("PD1AA", "Product 1AA", project, child1);
            products.create("PD1AAA", "Product 1AAA", project, child1_1);
            products.create("PD1AAB", "Product 1AAB", project, child1_1);
            products.create("PD1BA", "Product 1BA", project, child2);

            // then
            assertThat(product.getChildProducts()).hasSize(6);

        }




    }

}