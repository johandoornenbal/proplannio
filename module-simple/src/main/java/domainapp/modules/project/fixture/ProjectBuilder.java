package domainapp.modules.project.fixture;

import javax.inject.Inject;

import org.apache.isis.testing.fixtures.applib.fixturescripts.BuilderScriptWithResult;

import domainapp.modules.project.dom.Project;
import domainapp.modules.project.dom.Projects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class ProjectBuilder extends BuilderScriptWithResult<Project> {

    @Getter @Setter
    private String name;

    @Override
    protected Project buildResult(final ExecutionContext ec) {
        
        checkParam("name", ec, String.class);
        
        return wrap(projects).create(name);
    }
    
    // -- DEPENDENCIES

    @Inject Projects projects;

}
