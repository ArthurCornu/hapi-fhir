package ca.uhn.fhir.empi.rules.svc;

/*-
 * #%L
 * hapi-fhir-empi-rules
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.rules.config.IEmpiConfig;
import ca.uhn.fhir.empi.rules.json.EmpiFieldMatchJson;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * The EmpiResourceComparator is in charge of performing actual comparisons between left and right records.
 * It does so by comparing individual comparators, and returning a weight based on the combination of
 * field comparators that matched.
 */

@Lazy
@Service
public class EmpiResourceComparatorSvc {
	private final FhirContext myFhirContext;
	private final IEmpiConfig myEmpiConfig;
	private EmpiRulesJson myEmpiRulesJson;
	private final List<EmpiResourceFieldComparator> myFieldComparators = new ArrayList<>();

	@Autowired
	public EmpiResourceComparatorSvc(FhirContext theFhirContext, IEmpiConfig theEmpiConfig) {
		myFhirContext = theFhirContext;
		myEmpiConfig = theEmpiConfig;
	}

	@PostConstruct
	public void init() {
		myEmpiRulesJson = myEmpiConfig.getEmpiRules();
		for (EmpiFieldMatchJson matchFieldJson : myEmpiRulesJson.getMatchFields()) {
			myFieldComparators.add(new EmpiResourceFieldComparator(myFhirContext, matchFieldJson));
		}

	}
	public EmpiMatchResultEnum getMatchResult(IBaseResource theLeftResource, IBaseResource theRightResource) {
		return compare(theLeftResource, theRightResource);
	}

	EmpiMatchResultEnum compare(IBaseResource theLeftResource, IBaseResource theRightResource) {
		long matchVector = getMatchVector(theLeftResource, theRightResource);
		return myEmpiRulesJson.getMatchResult(matchVector);
	}

	/**
	 * This function generates a `match vector`, which is a long representation of a binary string
	 * generated by the results of each of the given comparator matches. For example.
	 * start with a binary representation of the value 0 for long: 0000
	 * first_name matches, so the value `1` is bitwise-ORed to the current value (0) in right-most position.
	 * `0001`
	 *
	 * Next, we look at the second field comparator, and see if it matches. If it does, we left-shift 1 by the index
	 * of the comparator, in this case also 1.
	 * `0010`
	 *
	 * Then, we bitwise-or it with the current retval:
	 * 0001|0010 = 0011
	 * The binary string is now `0011`, which when you return it as a long becomes `3`.
	 */
	private long getMatchVector(IBaseResource theLeftResource, IBaseResource theRightResource) {
		long retval = 0;
		for (int i = 0; i < myFieldComparators.size(); ++i) {
			//any that are not for the resourceType in question.
			EmpiResourceFieldComparator fieldComparator = myFieldComparators.get(i);
			if (fieldComparator.match(theLeftResource, theRightResource)) {
				retval |= (1 << i);
			}
		}
		return retval;
	}
}
