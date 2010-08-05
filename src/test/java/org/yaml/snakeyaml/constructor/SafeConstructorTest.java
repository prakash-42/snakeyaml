/**
 * Copyright (c) 2008-2010, http://code.google.com/p/snakeyaml/
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

package org.yaml.snakeyaml.constructor;

import junit.framework.TestCase;

import org.yaml.snakeyaml.SnakeYaml;

public class SafeConstructorTest extends TestCase {

    public void testConstructFloat() {
        SnakeYaml yaml = new SnakeYaml();
        assertEquals(3.1416, yaml.load("+3.1416"));
        assertEquals(Double.POSITIVE_INFINITY, yaml.load("+.inf"));
        assertEquals(Double.POSITIVE_INFINITY, yaml.load(".inf"));
        assertEquals(Double.NEGATIVE_INFINITY, yaml.load("-.inf"));
    }

    public void testSafeConstruct() {
        SnakeYaml yaml = new SnakeYaml(new SafeConstructor());
        assertEquals(3.1416, yaml.load("+3.1416"));
    }

    public void testSafeConstructJavaBean() {
        SnakeYaml yaml = new SnakeYaml(new SafeConstructor());
        String data = "--- !!org.yaml.snakeyaml.constructor.Person\nfirstName: Andrey\nage: 99";
        try {
            yaml.load(data);
            fail("JavaBeans cannot be created by SafeConstructor.");
        } catch (ConstructorException e) {
            assertTrue(e
                    .getMessage()
                    .contains(
                            "could not determine a constructor for the tag tag:yaml.org,2002:org.yaml.snakeyaml.constructor.Person"));
        }
    }
}
