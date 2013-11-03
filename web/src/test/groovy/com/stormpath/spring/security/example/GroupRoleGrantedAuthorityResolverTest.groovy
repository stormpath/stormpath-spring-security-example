/*
 * Copyright 2013 Stormpath, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stormpath.spring.security.example

import com.stormpath.sdk.group.Group
import com.stormpath.spring.security.example.mapping.GroupRoleGrantedAuthorityResolver
import org.easymock.EasyMock
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.fail

class GroupRoleGrantedAuthorityResolverTest {

    private static final String ROLE_A = "ROLE_A"
    private static final String ROLE_B = "ROLE_B"
    private static final String ROLE_C = "ROLE_C"
    private static final String GROUP_XHref = "https://api.stormpath.com/v1/groups/d8eDkz3QPca2z73j93m18"
    private static final String GROUP_YHref = "https://api.stormpath.com/v1/groups/A2eakz0lpRa1S7gj91qAA"
    private GroupRoleGrantedAuthorityResolver resolver
    private Map<String, List<String>> rolesMap
    private List<String> groupXRoles
    private List<String> groupYRoles

    @Before
    void setUp() {
        groupXRoles = new ArrayList<>()
        groupYRoles = new ArrayList<>()
        groupXRoles.add(ROLE_A)
        groupXRoles.add(ROLE_C)
        groupYRoles.add(ROLE_B)

        rolesMap = new HashMap<>()
        rolesMap.put(GROUP_XHref, groupXRoles)
        rolesMap.put(GROUP_YHref, groupYRoles)
        resolver = new GroupRoleGrantedAuthorityResolver(rolesMap)
    }

    @Test
    void testConstructor() {
        Assert.assertEquals 2, resolver.rolesMap.size()
        Assert.assertEquals 2, resolver.rolesMap.get(GROUP_XHref).size()
        Assert.assertEquals 1, resolver.rolesMap.get(GROUP_YHref).size()
    }

    @Test
    void testConstructorError() {
        try {
            new GroupRoleGrantedAuthorityResolver(null)
            fail("IllegalArgumentException expected")
        } catch (IllegalArgumentException e) {
            try {
                new GroupRoleGrantedAuthorityResolver(new HashMap<String, List<String>>())
                fail("IllegalArgumentException expected")
            } catch (IllegalArgumentException ex) {
                return;
            }
        }
        fail();
    }

    @Test
    void testSetRolesMap() {
        def Map<String, List<String>> map = new HashMap<String, List<String>>()
        map.put(GROUP_XHref, groupXRoles)
        resolver.rolesMap = map
        Assert.assertEquals map, resolver.rolesMap
    }

    @Test
    void testSetRolesMapError() {
        try {
            resolver.rolesMap = null
            fail("IllegalArgumentException expected")
        } catch (IllegalArgumentException e) {
            try {
                resolver.rolesMap = new HashMap<String, List<String>>();
                fail("IllegalArgumentException expected")
            } catch (IllegalArgumentException ex) {
                return;
            }
        }
        fail();
    }

    @Test
    void testResolveGrantedAuthorities() {
        def groupX = EasyMock.createStrictMock(Group)

        EasyMock.expect(groupX.getHref()).andReturn(GROUP_XHref)

        EasyMock.replay groupX

        def set = resolver.resolveGrantedAuthorities(groupX)
        assertEquals 2, set.size()

        EasyMock.verify groupX
    }

    @Test(expected = IllegalStateException)
    void testResolveGrantedAuthoritiesNoHrefForGroup() {
        def group = EasyMock.createStrictMock(Group)

        EasyMock.expect(group.getHref()).andReturn(null)

        EasyMock.replay group

        resolver.resolveGrantedAuthorities(group)

        EasyMock.verify group
    }

    @Test(expected = UnsupportedOperationException)
    void testResolveGrantedAuthoritiesNoMapping() {
        def group = EasyMock.createStrictMock(Group)

        EasyMock.expect(group.getHref()).andReturn("https://api.stormpath.com/v1/groups/DoesNotExistInMap")

        EasyMock.replay group

        resolver.resolveGrantedAuthorities(group)

        EasyMock.verify group
    }

}
