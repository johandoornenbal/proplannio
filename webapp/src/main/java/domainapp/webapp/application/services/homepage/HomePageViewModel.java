package domainapp.webapp.application.services.homepage;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.HomePage;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.services.i18n.TranslatableString;

import domainapp.modules.project.dom.Project;
import domainapp.modules.project.dom.Projects;
import domainapp.modules.simple.dom.so.SimpleObject;
import domainapp.modules.simple.dom.so.SimpleObjects;

@DomainObject(
        nature = Nature.VIEW_MODEL,
        objectType = "domainapp.HomePageViewModel"
        )
@HomePage
public class HomePageViewModel {

    public TranslatableString title() {
        return TranslatableString.tr("{num} projects", "num", getObjects().size());
    }

    public List<Project> getObjects() {
        return projects.listAll();
    }

    @Inject Projects projects;
}
