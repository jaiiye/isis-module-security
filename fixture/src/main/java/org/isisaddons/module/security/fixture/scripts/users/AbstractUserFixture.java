package org.isisaddons.module.security.fixture.scripts.users;

import org.isisaddons.module.security.dom.actor.ApplicationUser;
import org.isisaddons.module.security.dom.actor.ApplicationUsers;
import org.apache.isis.applib.fixturescripts.FixtureScript;

public abstract class AbstractUserFixture extends FixtureScript {

    protected ApplicationUser create(
            final String name,
            final ExecutionContext executionContext) {
        final ApplicationUser entity = applicationUsers.newUser(name);
        executionContext.add(this, name, entity);
        return entity;
    }

    @javax.inject.Inject
    private ApplicationUsers applicationUsers;

}