package domainapp.modules.project.integtests;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import org.apache.isis.core.config.presets.IsisPresets;
import org.apache.isis.core.runtimeservices.IsisModuleCoreRuntimeServices;
import org.apache.isis.persistence.jdo.datanucleus5.IsisModuleJdoDataNucleus5;
import org.apache.isis.security.bypass.IsisModuleSecurityBypass;
import org.apache.isis.testing.fixtures.applib.IsisIntegrationTestAbstractWithFixtures;
import org.apache.isis.testing.fixtures.applib.IsisModuleTestingFixturesApplib;

import domainapp.modules.project.ProjectModule;
import domainapp.modules.simple.SimpleModule;

@SpringBootTest(
        classes = ProjectModuleIntegTestAbstract.AppManifest.class
)
@TestPropertySource({
        IsisPresets.H2InMemory_withUniqueSchema,
        IsisPresets.DataNucleusAutoCreate,
        IsisPresets.UseLog4j2Test,
})
public abstract class ProjectModuleIntegTestAbstract extends IsisIntegrationTestAbstractWithFixtures {

    @Configuration
    @Import({
        IsisModuleCoreRuntimeServices.class,
        IsisModuleSecurityBypass.class,
        IsisModuleJdoDataNucleus5.class,
        IsisModuleTestingFixturesApplib.class,

        ProjectModule.class
    })
    public static class AppManifest {
    }
}
