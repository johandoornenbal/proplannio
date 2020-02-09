package domainapp.modules.project.fixture;

import org.apache.isis.applib.services.registry.ServiceRegistry;
import org.apache.isis.testing.fixtures.applib.api.PersonaWithBuilderScript;
import org.apache.isis.testing.fixtures.applib.api.PersonaWithFinder;
import org.apache.isis.testing.fixtures.applib.setup.PersonaEnumPersistAll;

import domainapp.modules.project.dom.Project;
import domainapp.modules.project.dom.Projects;
import domainapp.modules.simple.dom.so.SimpleObject;
import domainapp.modules.simple.dom.so.SimpleObjects;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Project_persona
implements PersonaWithBuilderScript<ProjectBuilder>, PersonaWithFinder<Project> {

    FOO("Foo"),
    BAR("Bar"),
    BAZ("Baz"),
    FRODO("Frodo"),
    FROYO("Froyo"),
    FIZZ("Fizz"),
    BIP("Bip"),
    BOP("Bop"),
    BANG("Bang"),
    BOO("Boo");

    private final String name;

    @Override
    public ProjectBuilder builder() {
        return new ProjectBuilder().setName(name);
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
}
