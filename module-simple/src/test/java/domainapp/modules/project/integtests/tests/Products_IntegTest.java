package domainapp.modules.project.integtests.tests;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import domainapp.modules.project.actions.Project_addProduct;
import domainapp.modules.project.dom.Product;
import domainapp.modules.project.dom.Products;
import domainapp.modules.project.dom.Project;
import domainapp.modules.project.dom.Projects;
import domainapp.modules.project.fixture.Project_persona;
import domainapp.modules.project.integtests.ProjectModuleIntegTestAbstract;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class Products_IntegTest extends ProjectModuleIntegTestAbstract {

    @Inject
    Products products;

    @Inject
    Projects projects;

    public static class ListAll extends Products_IntegTest {

        @Test
        public void happyCase() {

            // given
            fixtureScripts.run(new Project_persona.PersistAll());
            transactionService.flushTransaction();

            // when
            final List<Product> all = wrap(products).listAll();

            // then
            assertThat(all).isNotEmpty();
        }

        @Test
        public void whenNone() {

            // when
            final List<Product> all = wrap(products).listAll();

            // then
            assertThat(all).hasSize(0);
        }
    }

    public static class Finders extends Products_IntegTest {

        // This one fails when not run in isolation
        //
        @Test
        public void findersWork_problematic() {

            // given
            final Project project = projects.create("P");
            mixin(Project_addProduct.class, project).$$("PD1", "Product 1", null);
            mixin(Project_addProduct.class, project).$$("PD2", "Product 2", null);

            // when
            final Product pd1 = products.findByReference("PD1");

            // then
            assertThat(pd1).isNotNull();

            // and when
            mixin(Project_addProduct.class, project).$$("PD1A", "Product 1A", pd1);
            mixin(Project_addProduct.class, project).$$("PD1B", "Product 1B", pd1);
            transactionService.flushTransaction(); // does not surface the issue
            // commenting in TransactionServic#nextTransaction produces an error ...
            // java.lang.AssertionError at org.apache.isis.persistence.jdo.datanucleus5.persistence.IsisTransactionJdo.markAsAborted(IsisTransactionJdo.java:412)
            //  transactionService.nextTransaction();

            final List<Product> directChildrenOfPd1 = products.findByParent(pd1);
            // then
            assertThat(directChildrenOfPd1).hasSize(2);

        }

        @Test
        public void findersWork_ok() {

            // given
            final Project project = projects.create("Q");
            mixin(Project_addProduct.class, project).$$("QPD1", "Product 1", null);
            mixin(Project_addProduct.class, project).$$("QPD2", "Product 2", null);

            // when
            final Product pd1 = products.findByReference("QPD1");

            // then
            assertThat(pd1).isNotNull();

            // and when
            mixin(Project_addProduct.class, project).$$("QPD1A", "Product 1A", pd1);
            mixin(Project_addProduct.class, project).$$("QPD1B", "Product 1B", pd1);
            // commenting in TransactionServic#nextTransaction produces an error ...
            // java.lang.AssertionError at org.apache.isis.persistence.jdo.datanucleus5.persistence.IsisTransactionJdo.markAsAborted(IsisTransactionJdo.java:412)
            //  transactionService.nextTransaction();

            final List<Product> directChildrenOfPd1 = products.findByParent(pd1);
            // then
            assertThat(directChildrenOfPd1).hasSize(2);

        }

    }

}