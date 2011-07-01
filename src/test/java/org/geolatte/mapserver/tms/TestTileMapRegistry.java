/*
 * Copyright 2009-2010  Geovise BVBA, QMINO BVBA
 *
 * This file is part of GeoLatte Mapserver.
 *
 * GeoLatte Mapserver is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GeoLatte Mapserver is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GeoLatte Mapserver.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geolatte.mapserver.tms;

import org.geolatte.mapserver.config.Configuration;
import org.geolatte.mapserver.config.ConfigurationException;
import org.geolatte.mapserver.util.SRS;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestTileMapRegistry {

    TileMapRegistry tileMaps;

    @Before
    public void setUp() throws ConfigurationException {
        Configuration config = Configuration.load("test-config.xml");
        tileMaps = TileMapRegistry.configure(config);
    }

    @Test
    public void check_tilemaps_present() {
        List<String> expected = new ArrayList<String>();
        expected.add("basic");
        expected.add("osm");
        expected.add("tms-vlaanderen");
        List<String> received = tileMaps.getTileMapNames();
        assertEquals(expected, received);
    }

    @Test
    public void check_supported_srs() {
        assertTrue(tileMaps.getSupportedSRS("basic").isEmpty());
        List<SRS> expected = new ArrayList<SRS>();
        expected.add(SRS.parse("EPSG:25831"));
        expected.add(SRS.parse("EPSG:9100913"));
        List<SRS> received = tileMaps.getSupportedSRS("tms-vlaanderen");
        assertEquals(expected, received);
    }

    @Test
    public void check_is_srs_supported() {
        assertTrue(tileMaps.supportsSRS("tms-vlaanderen", SRS.parse("EPSG:31370")));
        assertTrue(tileMaps.supportsSRS("tms-vlaanderen", SRS.parse("EPSG:9100913")));
        assertTrue(tileMaps.supportsSRS("tms-vlaanderen", SRS.parse("EPSG:25831")));
        assertFalse(tileMaps.supportsSRS("tms-vlaanderen", SRS.parse("EPSG:4326")));

    }

    @Test
    public void verify_tilemap() {
        TileMap tileMap = tileMaps.getTileMap("basic");
        assertNotNull(tileMap);
    }


}
