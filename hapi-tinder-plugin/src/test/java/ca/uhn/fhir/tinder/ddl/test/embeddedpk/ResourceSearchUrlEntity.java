/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
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
 * #L%
 */
package ca.uhn.fhir.tinder.ddl.test.embeddedpk;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.time.LocalDate;
import java.util.Date;

/**
 * This entity is used to enforce uniqueness on a given search URL being
 * used as a conditional operation URL, e.g. a conditional create or a
 * conditional update. When we perform a conditional operation that is
 * creating a new resource, we store an entity with the conditional URL
 * in this table. The URL is the PK of the table, so the database
 * ensures that two concurrent threads don't accidentally create two
 * resources with the same conditional URL.
 */
@Entity
@Table(
		name = "HFJ_RES_SEARCH_URL",
		indexes = {
			@Index(name = "IDX_RESSEARCHURL_RES", columnList = "RES_ID"),
			@Index(name = "IDX_RESSEARCHURL_TIME", columnList = "CREATED_TIME")
		})
public class ResourceSearchUrlEntity {

	public static final String RES_SEARCH_URL_COLUMN_NAME = "RES_SEARCH_URL";
	public static final String PARTITION_ID = "PARTITION_ID";

	@EmbeddedId
	private ResourceSearchUrlEntityPK myPk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(
			value = {
				@JoinColumn(name = "RES_ID", nullable = false, insertable = false, updatable = false),
				@JoinColumn(name = "PARTITION_ID", nullable = false, insertable = false, updatable = false)
			},
			foreignKey = @ForeignKey(name = "FK_RES_SEARCH_URL_RESOURCE"))
	private AResourceTable myResourceTable;

	@Column(name = "RES_ID", updatable = false, nullable = false, insertable = true)
	private Long myResourcePid;

	@Column(name = "PARTITION_ID", updatable = false, nullable = false, insertable = false)
	private Integer myPartitionIdValue;

	@Column(name = "PARTITION_DATE", nullable = true, insertable = true, updatable = false)
	private LocalDate myPartitionDate;

	@Column(name = "CREATED_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date myCreatedTime;

}
