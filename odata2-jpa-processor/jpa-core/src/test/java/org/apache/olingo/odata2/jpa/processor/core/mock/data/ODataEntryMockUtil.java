/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 ******************************************************************************/
package org.apache.olingo.odata2.jpa.processor.core.mock.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.olingo.odata2.api.ep.entry.EntryMetadata;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.jpa.processor.core.mock.data.JPATypeMock.JPARelatedTypeMock;
import org.apache.olingo.odata2.jpa.processor.core.mock.data.JPATypeMock.JPATypeEmbeddableMock;
import org.apache.olingo.odata2.jpa.processor.core.mock.data.JPATypeMock.JPATypeEmbeddableMock2;
import org.easymock.EasyMock;

public class ODataEntryMockUtil {

  public static final int VALUE_MINT = 20;
  public static Calendar VALUE_DATE_TIME = null;
  public static final String VALUE_MSTRING = "Mock";
  public static final long VALUE_MLONG = 1234567890L;
  public static final double VALUE_MDOUBLE = 20.12;
  public static final byte VALUE_MBYTE = 0XA;
  public static final byte[] VALUE_MBYTEARRAY = { 0XA, 0XB };
  public static final float VALUE_MFLOAT = 2.00F;
  public static final UUID VALUE_UUID = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
  public static final short VALUE_SHORT = 2;

  public static ODataEntry mockODataEntry(final String entityName) {
    ODataEntry oDataEntry = EasyMock.createMock(ODataEntry.class);
    EasyMock.expect(oDataEntry.getProperties()).andReturn(mockODataEntryProperties(entityName)).anyTimes();

    enhanceMockODataEntry(oDataEntry, false, new ArrayList<String>());
    EasyMock.replay(oDataEntry);
    return oDataEntry;
  }

  public static ODataEntry mockODataEntryWithComplexType(final String entityName) {
    ODataEntry oDataEntry = EasyMock.createMock(ODataEntry.class);
    EasyMock.expect(oDataEntry.getProperties()).andReturn(mockODataEntryPropertiesWithComplexType(entityName))
        .anyTimes();

    enhanceMockODataEntry(oDataEntry, false, new ArrayList<String>());
    EasyMock.replay(oDataEntry);
    return oDataEntry;
  }

  public static Map<String, Object> mockODataEntryProperties(final String entityName) {
    Map<String, Object> propertyMap = new HashMap<String, Object>();

    if (entityName.equals(JPATypeMock.ENTITY_NAME)) {
      propertyMap.put(JPATypeMock.PROPERTY_NAME_MINT, VALUE_MINT);

      VALUE_DATE_TIME = Calendar.getInstance(TimeZone.getDefault());
      VALUE_DATE_TIME.set(2013, 1, 1, 1, 1, 1);
      propertyMap.put(JPATypeMock.PROPERTY_NAME_MDATETIME, VALUE_DATE_TIME);

      propertyMap.put(JPATypeMock.PROPERTY_NAME_MSTRING, VALUE_MSTRING);
    } else if (entityName.equals(JPARelatedTypeMock.ENTITY_NAME)) {
      propertyMap.put(JPARelatedTypeMock.PROPERTY_NAME_MLONG, VALUE_MLONG);
      propertyMap.put(JPARelatedTypeMock.PROPERTY_NAME_MDOUBLE, VALUE_MDOUBLE);
      propertyMap.put(JPARelatedTypeMock.PROPERTY_NAME_MBYTE, VALUE_MBYTE);
      propertyMap.put(JPARelatedTypeMock.PROPERTY_NAME_MBYTEARRAY, VALUE_MBYTEARRAY);
    } else if (entityName.equals(JPATypeEmbeddableMock.ENTITY_NAME)) {
      propertyMap.put(JPATypeEmbeddableMock.PROPERTY_NAME_MSHORT, VALUE_SHORT);
      propertyMap.put(JPATypeEmbeddableMock.PROPERTY_NAME_MEMBEDDABLE,
          mockODataEntryProperties(JPATypeEmbeddableMock2.ENTITY_NAME));
    } else if (entityName.equals(JPATypeEmbeddableMock2.ENTITY_NAME)) {
      propertyMap.put(JPATypeEmbeddableMock2.PROPERTY_NAME_MFLOAT, VALUE_MFLOAT);
      propertyMap.put(JPATypeEmbeddableMock2.PROPERTY_NAME_MUUID, VALUE_UUID);
    }

    return propertyMap;
  }

  public static Map<String, Object> mockODataEntryPropertiesWithComplexType(final String entityName) {
    Map<String, Object> propertyMap = mockODataEntryProperties(entityName);
    propertyMap
        .put(JPATypeMock.PROPERTY_NAME_MCOMPLEXTYPE, mockODataEntryProperties(JPATypeEmbeddableMock.ENTITY_NAME));
    return propertyMap;
  }

  public static Map<String, Object> mockODataEntryPropertiesWithInline(final String entityName) {
    Map<String, Object> propertyMap = mockODataEntryProperties(entityName);
    List<ODataEntry> relatedEntries = new ArrayList<ODataEntry>();
    relatedEntries.add(mockODataEntry(JPARelatedTypeMock.ENTITY_NAME));
    ODataFeed feed = EasyMock.createMock(ODataFeed.class);
    EasyMock.expect(feed.getEntries()).andReturn(relatedEntries);
    EasyMock.replay(feed);
    propertyMap.put(JPATypeMock.NAVIGATION_PROPERTY_X, feed);

    return propertyMap;

  }

  public static ODataEntry mockODataEntryWithInline(final String entityName) {
    ODataEntry oDataEntry = EasyMock.createMock(ODataEntry.class);
    EasyMock.expect(oDataEntry.getProperties()).andReturn(mockODataEntryPropertiesWithInline(entityName)).anyTimes();
    if (entityName.equals(JPATypeMock.ENTITY_NAME)) {
      List<String> links = new ArrayList<String>();
      links.add(JPATypeMock.ENTITY_NAME + "(" + ODataEntryMockUtil.VALUE_MINT + ")/"
          + JPATypeMock.NAVIGATION_PROPERTY_X);
      enhanceMockODataEntry(oDataEntry, true, links);
    } else {
      enhanceMockODataEntry(oDataEntry, false, new ArrayList<String>());
    }
    EasyMock.replay(oDataEntry);
    return oDataEntry;
  }

  private static void
      enhanceMockODataEntry(final ODataEntry oDataEntry, final boolean hasInline, final List<String> associationURIs) {
    EasyMock.expect(oDataEntry.containsInlineEntry()).andReturn(hasInline).anyTimes();
    EntryMetadata entryMetadata = EasyMock.createMock(EntryMetadata.class);
    if (hasInline) {
      EasyMock.expect(entryMetadata.getAssociationUris(JPATypeMock.NAVIGATION_PROPERTY_X)).andReturn(associationURIs)
          .anyTimes();
      EasyMock.expect(entryMetadata.getAssociationUris(JPATypeMock.NAVIGATION_PROPERTY_XS)).andReturn(
          new ArrayList<String>())
          .anyTimes();
    } else {
      EasyMock.expect(entryMetadata.getAssociationUris(EasyMock.isA(String.class))).andReturn(associationURIs)
          .anyTimes();
    }

    EasyMock.replay(entryMetadata);
    EasyMock.expect(oDataEntry.getMetadata()).andReturn(entryMetadata).anyTimes();
  }
}
