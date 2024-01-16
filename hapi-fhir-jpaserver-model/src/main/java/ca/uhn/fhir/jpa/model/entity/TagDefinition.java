/*
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.model.entity;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(
		name = "HFJ_TAG_DEF",
		indexes = {
			@Index(
					name = "IDX_TAG_DEF_TP_CD_SYS",
					columnList = "TAG_TYPE, TAG_CODE, TAG_SYSTEM, TAG_ID, TAG_VERSION, TAG_USER_SELECTED"),
		})
public class TagDefinition implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "TAG_CODE", length = 200)
	private String myCode;

	@Column(name = "TAG_DISPLAY", length = 200)
	private String myDisplay;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_TAGDEF_ID")
	@SequenceGenerator(name = "SEQ_TAGDEF_ID", sequenceName = "SEQ_TAGDEF_ID")
	@Column(name = "TAG_ID")
	private Long myId;

	@OneToMany(
			cascade = {},
			fetch = FetchType.LAZY,
			mappedBy = "myTag")
	private Collection<ResourceTag> myResources;

	@OneToMany(
			cascade = {},
			fetch = FetchType.LAZY,
			mappedBy = "myTag")
	private Collection<ResourceHistoryTag> myResourceVersions;

	@Column(name = "TAG_SYSTEM", length = 200)
	private String mySystem;

	@Column(name = "TAG_TYPE", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private TagTypeEnum myTagType;

	@Column(name = "TAG_VERSION", length = 30)
	private String myVersion;

	@Column(name = "TAG_USER_SELECTED")
	private Boolean myUserSelected;

	@Transient
	private transient Integer myHashCode;

	/**
	 * Constructor
	 */
	public TagDefinition() {
		super();
	}

	public TagDefinition(TagTypeEnum theTagType, String theSystem, String theCode, String theDisplay) {
		setTagType(theTagType);
		setCode(theCode);
		setSystem(theSystem);
		setDisplay(theDisplay);
	}

	public String getCode() {
		return myCode;
	}

	public void setCode(String theCode) {
		myCode = theCode;
		myHashCode = null;
	}

	public String getDisplay() {
		return myDisplay;
	}

	public void setDisplay(String theDisplay) {
		myDisplay = theDisplay;
	}

	public Long getId() {
		return myId;
	}

	public void setId(Long theId) {
		myId = theId;
	}

	public String getSystem() {
		return mySystem;
	}

	public void setSystem(String theSystem) {
		mySystem = theSystem;
		myHashCode = null;
	}

	public TagTypeEnum getTagType() {
		return myTagType;
	}

	public void setTagType(TagTypeEnum theTagType) {
		myTagType = theTagType;
		myHashCode = null;
	}

	public String getVersion() {
		return myVersion;
	}

	public void setVersion(String theVersion) {
		setVersionAfterTrim(theVersion);
	}

	private void setVersionAfterTrim(String theVersion) {
		if (theVersion != null) {
			myVersion = StringUtils.truncate(theVersion, 30);
		}
	}

	/**
	 * Warning - this is nullable, while IBaseCoding getUserSelected isn't.
	 * todo maybe rename?
	 */
	public Boolean getUserSelected() {
		return myUserSelected;
	}

	public void setUserSelected(Boolean theUserSelected) {
		myUserSelected = theUserSelected;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof TagDefinition)) {
			return false;
		}
		TagDefinition other = (TagDefinition) obj;

		EqualsBuilder b = new EqualsBuilder();

		if (myId != null && other.myId != null) {
			b.append(myId, other.myId);
		} else {
			b.append(myTagType, other.myTagType);
			b.append(mySystem, other.mySystem);
			b.append(myCode, other.myCode);
			b.append(myVersion, other.myVersion);
			b.append(myUserSelected, other.myUserSelected);
		}

		return b.isEquals();
	}

	@Override
	public int hashCode() {
		if (myHashCode == null) {
			HashCodeBuilder b = new HashCodeBuilder();
			b.append(myTagType);
			b.append(mySystem);
			b.append(myCode);
			b.append(myVersion);
			b.append(myUserSelected);
			myHashCode = b.toHashCode();
		}
		return myHashCode;
	}

	@Override
	public String toString() {
		ToStringBuilder retVal = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		retVal.append("id", myId);
		retVal.append("system", mySystem);
		retVal.append("code", myCode);
		retVal.append("display", myDisplay);
		retVal.append("version", myVersion);
		retVal.append("userSelected", myUserSelected);
		return retVal.build();
	}
}
