/**
 * Copyright (c) 2008-2011, http://www.snakeyaml.org
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

package org.yaml.snakeyaml;

import java.util.LinkedHashMap;

import junit.framework.TestCase;

import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.ArtificialProperty;

public class EnumBeanGenTest extends TestCase {
    // Dumping
    public void testDumpEnumBean() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        EnumBeanGen<Suit> bean = new EnumBeanGen<Suit>();
        bean.setId(17);
        bean.setSuit(Suit.SPADES);
        LinkedHashMap<Suit, Integer> map = new LinkedHashMap<Suit, Integer>();
        map.put(Suit.CLUBS, 1);
        map.put(Suit.DIAMONDS, 2);
        bean.setMap(map);
        String output = yaml.dump(bean);
        yaml.load(output);
        assertEquals(
                "!!org.yaml.snakeyaml.EnumBeanGen\nid: 17\nmap:\n  !!org.yaml.snakeyaml.Suit 'CLUBS': 1\n  !!org.yaml.snakeyaml.Suit 'DIAMONDS': 2\nsuit: !!org.yaml.snakeyaml.Suit 'SPADES'\n",
                output);
    }

    // Loading
    public void testLoadEnumBeanExplicitTags() {
        Yaml yaml = new Yaml();
        @SuppressWarnings("unchecked")
        EnumBeanGen<Suit> bean = (EnumBeanGen<Suit>) yaml
                .load("!!org.yaml.snakeyaml.EnumBeanGen\nid: 174\nmap:\n  !!org.yaml.snakeyaml.Suit 'CLUBS': 1\n  !!org.yaml.snakeyaml.Suit 'DIAMONDS': 2\nsuit: !!org.yaml.snakeyaml.Suit 'CLUBS'");

        LinkedHashMap<Suit, Integer> map = new LinkedHashMap<Suit, Integer>();
        map.put(Suit.CLUBS, 1);
        map.put(Suit.DIAMONDS, 2);

        assertEquals(Suit.CLUBS, bean.getSuit());
        assertEquals(174, bean.getId());
        assertEquals(map, bean.getMap());
    }

    public void testLoadNoTag4GenEnumProperty() {
        TypeDescription td = new TypeDescription(EnumBeanGen.class);
        td.addPropertyMock("suit", Suit.class, null, null);

        Constructor constructor = new Constructor();
        constructor.addTypeDescription(td);
        Yaml yaml = new Yaml(constructor);

        @SuppressWarnings("unchecked")
        EnumBeanGen<Suit> bean = (EnumBeanGen<Suit>) yaml
                .load("!!org.yaml.snakeyaml.EnumBeanGen\nid: 174\nmap:\n  !!org.yaml.snakeyaml.Suit 'CLUBS': 1\n  !!org.yaml.snakeyaml.Suit 'DIAMONDS': 2\nsuit: CLUBS");

        LinkedHashMap<Suit, Integer> map = new LinkedHashMap<Suit, Integer>();
        map.put(Suit.CLUBS, 1);
        map.put(Suit.DIAMONDS, 2);

        assertEquals(Suit.CLUBS, bean.getSuit());
        assertEquals(174, bean.getId());
        assertEquals(map, bean.getMap());
    }

    public void testLoadNoTags() {
        Constructor c = new Constructor();
        TypeDescription td = new TypeDescription(EnumBeanGen.class);
        td.addPropertyMock("suit", Suit.class, null, null);
        td.addPropertyMock(new ArtificialProperty("map", null, Suit.class, Object.class));

        c.addTypeDescription(td);
        Yaml yaml = new Yaml(c);
        @SuppressWarnings("unchecked")
        EnumBeanGen<Suit> bean = (EnumBeanGen<Suit>) yaml
                .load("!!org.yaml.snakeyaml.EnumBeanGen\nid: 174\nmap:\n  CLUBS: 1\n  DIAMONDS: 2\nsuit: CLUBS");

        LinkedHashMap<Suit, Integer> map = new LinkedHashMap<Suit, Integer>();
        map.put(Suit.CLUBS, 1);
        map.put(Suit.DIAMONDS, 2);

        assertEquals(Suit.CLUBS, bean.getSuit());
        assertEquals(174, bean.getId());
        assertEquals(map, bean.getMap());
    }

}
