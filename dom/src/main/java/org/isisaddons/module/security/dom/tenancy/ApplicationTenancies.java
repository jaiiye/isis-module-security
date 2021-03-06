/*
 *  Copyright 2014 Dan Haywood
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.isisaddons.module.security.dom.tenancy;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.isisaddons.module.security.SecurityModule;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RestrictTo;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        repositoryFor = ApplicationTenancy.class
)
@DomainServiceLayout(
        named="Security",
        menuBar = DomainServiceLayout.MenuBar.SECONDARY,
        menuOrder = "100.30"
)
public class ApplicationTenancies extends AbstractFactoryAndRepository {

    public static abstract class PropertyDomainEvent<T> extends SecurityModule.PropertyDomainEvent<ApplicationTenancies, T> {
        public PropertyDomainEvent(final ApplicationTenancies source, final Identifier identifier) {
            super(source, identifier);
        }

        public PropertyDomainEvent(final ApplicationTenancies source, final Identifier identifier, final T oldValue, final T newValue) {
            super(source, identifier, oldValue, newValue);
        }
    }

    public static abstract class CollectionDomainEvent<T> extends SecurityModule.CollectionDomainEvent<ApplicationTenancies, T> {
        public CollectionDomainEvent(final ApplicationTenancies source, final Identifier identifier, final Of of) {
            super(source, identifier, of);
        }

        public CollectionDomainEvent(final ApplicationTenancies source, final Identifier identifier, final Of of, final T value) {
            super(source, identifier, of, value);
        }
    }

    public static abstract class ActionDomainEvent extends SecurityModule.ActionDomainEvent<ApplicationTenancies> {
        public ActionDomainEvent(final ApplicationTenancies source, final Identifier identifier) {
            super(source, identifier);
        }

        public ActionDomainEvent(final ApplicationTenancies source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }

        public ActionDomainEvent(final ApplicationTenancies source, final Identifier identifier, final List<Object> arguments) {
            super(source, identifier, arguments);
        }
    }

    // //////////////////////////////////////

    //region > iconName

    public String iconName() {
        return "applicationTenancy";
    }

    //endregion

    //region > init

    @Programmatic
    @PostConstruct
    public void init() {
        if(applicationTenancyFactory == null) {
            applicationTenancyFactory = new ApplicationTenancyFactory.Default(getContainer());
        }
    }

    //endregion

    //region > findTenancyByName

    public static class FindTenancyByNameDomainEvent extends ActionDomainEvent {
        public FindTenancyByNameDomainEvent(final ApplicationTenancies source, final Identifier identifier, final Object... args) {
            super(source, identifier, args);
        }
    }

    @Action(
            domainEvent = FindTenancyByNameDomainEvent.class,
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            cssClassFa = "fa-crosshairs"
    )
    @MemberOrder(sequence = "100.30.1")
    public ApplicationTenancy findTenancyByName(
            @Parameter(maxLength = ApplicationTenancy.MAX_LENGTH_NAME)
            @ParameterLayout(named = "Name", typicalLength = ApplicationTenancy.TYPICAL_LENGTH_NAME)
            final String name) {
        return uniqueMatch(new QueryDefault<>(ApplicationTenancy.class, "findByName", "name", name));
    }

    //endregion

    //region > findTenancyByPath

    public static class FindTenancyByPathDomainEvent extends ActionDomainEvent {
        public FindTenancyByPathDomainEvent(final ApplicationTenancies source, final Identifier identifier, final Object... args) {
            super(source, identifier, args);
        }
    }

    @Action(
            domainEvent = FindTenancyByPathDomainEvent.class,
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            cssClassFa = "fa-crosshairs"
    )
    @MemberOrder(sequence = "100.30.2")
    public ApplicationTenancy findTenancyByPath(
            @Parameter(maxLength = ApplicationTenancy.MAX_LENGTH_PATH)
            @ParameterLayout(named = "Path")
            final String path) {
        if(path == null) {
            return null;
        }
        return uniqueMatch(new QueryDefault<>(ApplicationTenancy.class, "findByPath", "path", path));
    }

    //endregion


    //region > newTenancy

    public static class NewTenancyDomainEvent extends ActionDomainEvent {
        public NewTenancyDomainEvent(final ApplicationTenancies source, final Identifier identifier, final Object... args) {
            super(source, identifier, args);
        }
    }

    @Action(
            domainEvent = NewTenancyDomainEvent.class,
            semantics = SemanticsOf.IDEMPOTENT
    )
    @ActionLayout(
            cssClassFa = "fa-plus"
    )
    @MemberOrder(sequence = "100.30.3")
    public ApplicationTenancy newTenancy(
            @Parameter(maxLength = ApplicationTenancy.MAX_LENGTH_NAME)
            @ParameterLayout(named = "Name", typicalLength = ApplicationTenancy.TYPICAL_LENGTH_NAME)
            final String name,
            @Parameter(maxLength = ApplicationTenancy.MAX_LENGTH_PATH)
            @ParameterLayout(named = "Path")
            final String path,
            @Parameter(optionality = Optionality.OPTIONAL)
            @ParameterLayout(named = "Parent")
            final ApplicationTenancy parent) {
        ApplicationTenancy tenancy = findTenancyByName(name);
        if (tenancy == null){
            tenancy = applicationTenancyFactory.newApplicationTenancy();
            tenancy.setName(name);
            tenancy.setPath(path);
            tenancy.setParent(parent);
            persist(tenancy);
        }
        return tenancy;
    }

    //endregion

    //region > allTenancies
    public static class AllTenanciesDomainEvent extends ActionDomainEvent {
        public AllTenanciesDomainEvent(final ApplicationTenancies source, final Identifier identifier, final Object... args) {
            super(source, identifier, args);
        }
    }

    @Action(
            domainEvent = AllTenanciesDomainEvent.class,
            semantics = SemanticsOf.SAFE,
            restrictTo = RestrictTo.PROTOTYPING
    )
    @ActionLayout(
            cssClassFa = "fa-list"
    )
    @MemberOrder(sequence = "100.30.4")
    public List<ApplicationTenancy> allTenancies() {
        return allInstances(ApplicationTenancy.class);
    }

    //endregion

    //region > autoComplete
    @Programmatic // not part of metamodel
    public List<ApplicationTenancy> autoComplete(final String name) {
        return allMatches(new QueryDefault<>(
                ApplicationTenancy.class,
                "findByNameContaining", "name", name));
    }
    //endregion

    //region > injected
    /**
     * Will only be injected to if the programmer has supplied an implementation.  Otherwise
     * this class will install a default implementation in {@link #init()}.
     */
    @Inject
    ApplicationTenancyFactory applicationTenancyFactory;
    //endregion

}
