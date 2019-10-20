/*
 * Copyright 2014 JBoss Inc
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
package org.hibernate.bugs;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.model.relational.Namespace;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.bugs.entities.AnEntity;
import org.hibernate.bugs.entities.EntityA;
import org.hibernate.bugs.entities.EntityB;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.mapping.Table;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

import java.util.stream.StreamSupport;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using its built-in unit test framework.
 * Although ORMStandaloneTestCase is perfectly acceptable as a reproducer, usage of this class is much preferred.
 * Since we nearly always include a regression test with bug fixes, providing your reproducer using this method
 * simplifies the process.
 *
 * What's even better?  Fork hibernate-orm itself, add your test case directly to a module's unit tests, then
 * submit it as a PR!
 */
public class ORMUnitTestCase extends BaseCoreFunctionalTestCase {

	// Add your entities here.
	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
				AnEntity.class,
				EntityA.class,
				EntityB.class
		};
	}

	// If you use *.hbm.xml mappings, instead of annotations, add the mappings here.
	@Override
	protected String[] getMappings() {
		return new String[] {
//				"Foo.hbm.xml",
//				"Bar.hbm.xml"
		};
	}
	// If those mappings reside somewhere other than resources/org/hibernate/test, change this.
	@Override
	protected String getBaseForMappings() {
		return "org/hibernate/bugs/entities";
	}

	@Override
	protected BootstrapServiceRegistry buildBootstrapServiceRegistry() {
		final BootstrapServiceRegistryBuilder builder = new BootstrapServiceRegistryBuilder();
		builder.applyIntegrator(MetadataExtractorIntegrator.INSTANCE);
		builder.applyClassLoader( getClass().getClassLoader() );
		prepareBootstrapRegistryBuilder( builder );
		return builder.build();
	}

	// Add in any settings that are specific to your test.  See resources/hibernate.properties for the defaults.
	@Override
	protected void configure(Configuration configuration) {
		super.configure( configuration );

		configuration.setProperty( AvailableSettings.SHOW_SQL, Boolean.TRUE.toString() );
		configuration.setProperty( AvailableSettings.FORMAT_SQL, Boolean.TRUE.toString() );
		//configuration.setProperty( AvailableSettings.GENERATE_STATISTICS, "true" );
	}

	// Add your tests, using standard JUnit.
	@Test
	public void hhh13615() {

		Namespace schemaSecondary = StreamSupport.stream(MetadataExtractorIntegrator.INSTANCE.getDatabase().getNamespaces().spliterator(), false)
				.filter(namespace -> namespace.getName().getSchema().getText().equals("schema_secondary"))
				.findAny()
				.orElse(null);

		assertNotNull(schemaSecondary);
		assertArrayEquals(new String[]{"EntityA", "EntityB", "EntityA_EntityB", "EntityB_EntityA"}, schemaSecondary.getTables().stream().map(Table::getName).toArray(String[]::new));
		assertEquals(4, schemaSecondary.getTables().size());
	}


	static class MetadataExtractorIntegrator implements org.hibernate.integrator.spi.Integrator {

		public static final MetadataExtractorIntegrator INSTANCE = new MetadataExtractorIntegrator();

		private Database database;

		public Database getDatabase() {
			return database;
		}

		@Override
		public void integrate(
				Metadata metadata,
				SessionFactoryImplementor sessionFactory,
				SessionFactoryServiceRegistry serviceRegistry) {

			database = metadata.getDatabase();
		}

		@Override
		public void disintegrate(
				SessionFactoryImplementor sessionFactory,
				SessionFactoryServiceRegistry serviceRegistry) {
		}
	}
}


