package domainapp.modules.project.fixture;

import java.util.Arrays;
import java.util.List;

import org.apache.isis.applib.services.registry.ServiceRegistry;
import org.apache.isis.testing.fixtures.applib.api.PersonaWithBuilderScript;
import org.apache.isis.testing.fixtures.applib.api.PersonaWithFinder;
import org.apache.isis.testing.fixtures.applib.setup.PersonaEnumPersistAll;

import domainapp.modules.project.dom.Project;
import domainapp.modules.project.dom.Projects;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public enum Project_persona
implements PersonaWithBuilderScript<ProjectBuilder>, PersonaWithFinder<Project> {

    P1("Project 1", Arrays.asList(
            new ProductSpec("P1PD1", "Product 1", null),
            new ProductSpec("P1PD2", "Product 2", null),
            new ProductSpec("P1PD1.1", "Product 1 child 1", "P1PD1")
    )),
    P2("Project 2", Arrays.asList()),
    P3("Project 3", Arrays.asList());

    private final String name;
    private final List<ProductSpec> productSpecs;

    @Override
    public ProjectBuilder builder() {
        return new ProjectBuilder().setName(name).setProductSpecs(productSpecs);
    }

    @Override
    public Project findUsing(final ServiceRegistry serviceRegistry) {
        Projects projects = serviceRegistry.lookupService(Projects.class).orElse(null);
        return projects.findByNameExact(name);
    }

    public static class PersistAll
    extends PersonaEnumPersistAll<Project_persona, Project> {

        public PersistAll() {
            super(Project_persona.class);
        }
    }

    @AllArgsConstructor
    @Data
    static class ProductSpec {

        private String reference;
        private String name;
        private String parentReference;

    }

}
