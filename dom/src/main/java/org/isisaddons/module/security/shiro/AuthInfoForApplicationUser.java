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
package org.isisaddons.module.security.shiro;

import java.util.Collection;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

class AuthInfoForApplicationUser implements AuthenticationInfo, AuthorizationInfo {
    private final PrincipalForApplicationUser principal;
    private final String realmName;
    private final Object credentials;

    public AuthInfoForApplicationUser(PrincipalForApplicationUser principal, String realmName, Object credentials) {
        this.principal = principal;
        this.realmName = realmName;
        this.credentials = credentials;
    }

    @Override
    public PrincipalCollection getPrincipals() {
        return new SimplePrincipalCollection(principal, realmName);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Collection<String> getRoles() {
        return principal.getRoles();
    }

    @Override
    public Collection<String> getStringPermissions() {
        return principal.getStringPermissions();
    }

    @Override
    public Collection<Permission> getObjectPermissions() {
        return principal.getObjectPermissions();
    }
}
