/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.api.internal.artifacts.ivyservice;

import org.gradle.cache.internal.CacheVersionMapping;
import org.gradle.util.VersionNumber;

import java.io.File;

import static org.gradle.cache.internal.CacheVersionMapping.introducedIn;

public enum CacheLayout {

    ROOT(null, "modules", introducedIn("1.9-rc-1").incrementedIn("1.9-rc-2")),

    // If you update FILE_STORE, you may also need to update LocallyAvailableResourceFinderFactory
    FILE_STORE(ROOT, "files", introducedIn("1.9-rc-1")),

    META_DATA(ROOT, "metadata",
        // 3-5, 7-11, 18-20, 22, 25-30, 32-35, 37-47, 49, 50, 52, 54, 55, 57 were never used in a release
        introducedIn("1.9-rc-2")
        .changedTo(2, "1.11-rc-1")
        .changedTo(6, "1.12-rc-1")
        .changedTo(12, "2.0-rc-1")
        .changedTo(13, "2.1-rc-3")
        .changedTo(14, "2.2-rc-1")
        .changedTo(15, "2.4-rc-1")
        .changedTo(16, "2.8-rc-1")
        .changedTo(17, "3.0-milestone-1")
        .changedTo(21, "3.1-rc-1")
        .changedTo(23, "3.2-rc-1")
        .changedTo(24, "4.2-rc-1")
        .changedTo(31, "4.3-rc-1")
        .changedTo(36, "4.4-rc-1")
        .changedTo(48, "4.5-rc-1")
        .changedTo(51, "4.5.1")
        .changedTo(53, "4.6-rc-1")
        .changedTo(56, "4.7-rc-1")
        .changedTo(58, "4.8-rc-1")),

    RESOURCES(ROOT, "resources", introducedIn("1.9-rc-1")),

    TRANSFORMS(null, "transforms", introducedIn("3.5-rc-1")),

    TRANSFORMS_META_DATA(TRANSFORMS, "metadata", introducedIn("3.5-rc-1")),

    TRANSFORMS_STORE(TRANSFORMS, "files", introducedIn("3.5-rc-1"));

    private final String name;
    private final CacheLayout parent;
    private final CacheVersionMapping versionMapping;

    CacheLayout(CacheLayout parent, String name, CacheVersionMapping.Builder versionMappingBuilder) {
        this.parent = parent;
        this.name = name;
        this.versionMapping = versionMappingBuilder.build();
    }

    public VersionNumber getVersion() {
        return VersionNumber.parse(getFormattedVersion());
    }

    public String getKey() {
        StringBuilder key = new StringBuilder();
        key.append(name);
        key.append("-");
        key.append(getFormattedVersion());
        return key.toString();
    }

    public String getFormattedVersion() {
        if (parent == null) {
            return String.valueOf(versionMapping.getLatestVersion());
        }
        return parent.getFormattedVersion() + '.' + String.valueOf(versionMapping.getLatestVersion());
    }

    public CacheVersionMapping getVersionMapping() {
        return versionMapping;
    }

    public File getPath(File parentDir) {
        return new File(parentDir, getKey());
    }
}
