package domainapp.modules.project.integtests.tests;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import domainapp.modules.project.dom.Product;
import domainapp.modules.project.dom.Products;
import domainapp.modules.project.dom.Project;
import domainapp.modules.project.dom.Projects;
import domainapp.modules.project.fixture.Project_persona;
import domainapp.modules.project.integtests.ProjectModuleIntegTestAbstract;
import domainapp.modules.simple.fixture.SimpleObject_persona;
import domainapp.modules.simple.integtests.SimpleModuleIntegTestAbstract;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
public class Projects_IntegTest extends ProjectModuleIntegTestAbstract {

    @Inject
    Projects menu;

    public static class listAll extends Projects_IntegTest {

        @Test
        public void happyCase() {

            // given
            fixtureScripts.run(new Project_persona.PersistAll());
            transactionService.flushTransaction();

            // when
            final List<Project> all = wrap(menu).listAll();

            // then
            assertThat(all).hasSize(Project_persona.values().length);
        }

        @Test
        public void whenNone() {

            // when
            final List<Project> all = wrap(menu).listAll();

            // then
            assertThat(all).hasSize(0);
        }
    }




}