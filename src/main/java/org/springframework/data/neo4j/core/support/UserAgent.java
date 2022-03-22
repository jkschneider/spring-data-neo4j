/*
 * Copyright 2011-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.neo4j.core.support;

import org.neo4j.driver.Driver;
import org.springframework.data.mapping.context.AbstractMappingContext;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.lang.Nullable;

/**
 * Representation of a user agent containing sensible information to identify queries generated by or executed via Spring Data Neo4j.
 *
 * @author Michael J. Simons
 * @since 6.1.11
 */
public enum UserAgent {

	INSTANCE(
			getVersionOf(Driver.class),
			getVersionOf(AbstractMappingContext.class),
			getVersionOf(EnableNeo4jRepositories.class)
	);

	@Nullable
	private final String driverVersion;

	@Nullable
	private final String springDataVersion;

	@Nullable
	private final String sdnVersion;

	private final String representation;

	UserAgent(@Nullable String driverVersion, @Nullable String springDataVersion, @Nullable String sdnVersion) {
		int idxOfDash = driverVersion == null ? -1 : driverVersion.indexOf('-');
		this.driverVersion = driverVersion == null ?
				null :
				driverVersion.substring(0, idxOfDash > 0 ? idxOfDash : driverVersion.length());
		this.springDataVersion = springDataVersion;
		this.sdnVersion = sdnVersion;

		String unknown = "-";
		this.representation = String.format("Java/%s (%s %s %s) neo4j-java/%s spring-data/%s spring-data-neo4j/%s",
				System.getProperty("java.version"),
				System.getProperty("java.vm.vendor"),
				System.getProperty("java.vm.name"),
				System.getProperty("java.vm.version"),
				this.driverVersion == null ? unknown : this.driverVersion,
				this.springDataVersion == null ? unknown : this.springDataVersion,
				this.sdnVersion == null ? unknown : this.sdnVersion
		);
	}

	@Nullable
	public String getDriverVersion() {
		return driverVersion;
	}

	@Nullable
	public String getSpringDataVersion() {
		return springDataVersion;
	}

	@Nullable
	public String getSdnVersion() {
		return sdnVersion;
	}

	@Nullable
	private static String getVersionOf(Class<?> type) {

		Package p = type.getPackage();
		String version = p.getImplementationVersion();
		if (!(version == null || version.trim().isEmpty())) {
			return version;
		}
		return null;
	}

	@Override
	public String toString() {
		return this.representation;
	}
}
