package domainapp.modules.project.dom;

import java.util.List;

import javax.inject.Inject;
import javax.jdo.JDOQLTypedQuery;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.PromptStyle;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.persistence.jdo.applib.services.IsisJdoSupport_v3_2;

import domainapp.modules.project.ProjectModule;
import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.dom.so.QSimpleObject;
import domainapp.modules.simple.dom.so.SimpleObject;
import domainapp.modules.simple.types.Name;

@DomainService(
        nature = NatureOfService.VIEW,
        objectType = "project.Projects"
        )
public class Projects {

    private final RepositoryService repositoryService;
    private final IsisJdoSupport_v3_2 isisJdoSupport;

    @Inject
    public Projects(RepositoryService repositoryService, IsisJdoSupport_v3_2 isisJdoSupport) {
        this.repositoryService = repositoryService;
        this.isisJdoSupport = isisJdoSupport;
    }

    public static class ActionDomainEvent extends ProjectModule.ActionDomainEvent<Projects> {}

    public static class CreateActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, domainEvent = CreateActionDomainEvent.class)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Project create(
            @Name final String name) {
        return repositoryService.persist(Project.withName(name));
    }

    public static class FindByNameActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.SAFE, domainEvent = FindByNameActionDomainEvent.class)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public List<Project> findByName(
            @Name final String name
            ) {
        JDOQLTypedQuery<Project> q = isisJdoSupport.newTypesafeQuery(Project.class);
        final QProject cand = QProject.candidate();
        q = q.filter(
                cand.name.indexOf(q.stringParameter("name")).ne(-1)
                );
        return q.setParameter("name", name)
                .executeList();
    }

    public Project findByNameExact(final String name) {
        JDOQLTypedQuery<Project> q = isisJdoSupport.newTypesafeQuery(Project.class);
        final QProject cand = QProject.candidate();
        q = q.filter(
                cand.name.eq(q.stringParameter("name"))
                );
        return q.setParameter("name", name)
                .executeUnique();
    }

    public static class ListAllActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<Project> listAll() {
        return repositoryService.allInstances(Project.class);
    }

}
