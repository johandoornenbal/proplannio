package domainapp.modules.project.integtests.tests;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.services.wrapper.DisabledException;
import org.apache.isis.applib.services.wrapper.InvalidException;
import org.apache.isis.persistence.jdo.datanucleus5.jdosupport.mixins.Persistable_datanucleusIdLong;

import domainapp.modules.project.dom.Project;
import domainapp.modules.project.fixture.Project_persona;
import domainapp.modules.project.integtests.ProjectModuleIntegTestAbstract;
import domainapp.modules.simple.dom.so.SimpleObject;
import domainapp.modules.simple.integtests.SimpleModuleIntegTestAbstract;
import lombok.Getter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
public class Project_IntegTest extends ProjectModuleIntegTestAbstract {

    Project project;

    @BeforeEach
    public void setUp() {
        // given
        project = fixtureScripts.runPersona(Project_persona.FOO);
    }

    public static class name extends Project_IntegTest {

        @Test
        public void accessible() {
            // when
            final String name = wrap(project).getName();

            // then
            assertThat(name).isEqualTo(project.getName());
        }

        @Test
        public void not_editable() {

            // expect
            assertThrows(DisabledException.class, ()->{

                // when
                wrap(project).setName("new name");
            });
        }

    }

    public static class updateName extends Project_IntegTest {

        @DomainService
        public static class UpdateNameListener {

            @Getter
            List<Project.UpdateNameActionDomainEvent> events = new ArrayList<>();

            @EventListener(Project.UpdateNameActionDomainEvent.class)
            public void on(Project.UpdateNameActionDomainEvent ev) {
                events.add(ev);
            }
        }

        @Inject
        UpdateNameListener updateNameListener;


        @Test
        public void can_be_updated_directly() {

            // given
            updateNameListener.getEvents().clear();

            // when
            wrap(project).updateName("new name");
            transactionService.flushTransaction();

            // then
            assertThat(wrap(project).getName()).isEqualTo("new name");
            assertThat(updateNameListener.getEvents()).hasSize(5);
        }

        @Test
        public void failsValidation() {

            // expect
            InvalidException cause = assertThrows(InvalidException.class, ()->{

                // when
                wrap(project).updateName("new name!");
            });

            // then
            assertThat(cause.getMessage(), containsString("Exclamation mark is not allowed."));
        }
    }

    public static class dataNucleusId extends Project_IntegTest {

        @Test
        public void should_be_populated() {
            // when
            final Long id = mixin(Persistable_datanucleusIdLong.class, project).prop();

            // then
            assertThat(id).isGreaterThanOrEqualTo(0);
        }
    }

}