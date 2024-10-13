/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemdelete;

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.jpa.model.entity.IdAndPartitionId;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemDeleteJobSvc;
import ca.uhn.fhir.jpa.term.models.CodeSystemVersionPIDResult;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteJobParameters;
import jakarta.annotation.Nonnull;

import java.util.Iterator;

public class ReadTermConceptVersionsStep
		implements IFirstJobStepWorker<TermCodeSystemDeleteJobParameters, CodeSystemVersionPIDResult> {

	private final ITermCodeSystemDeleteJobSvc myITermCodeSystemSvc;

	public ReadTermConceptVersionsStep(ITermCodeSystemDeleteJobSvc theCodeSystemDeleteJobSvc) {
		myITermCodeSystemSvc = theCodeSystemDeleteJobSvc;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<TermCodeSystemDeleteJobParameters, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<CodeSystemVersionPIDResult> theDataSink)
			throws JobExecutionFailedException {
		TermCodeSystemDeleteJobParameters parameters = theStepExecutionDetails.getParameters();

		long pid = parameters.getTermPid();

		Iterator<IdAndPartitionId> versionPids = myITermCodeSystemSvc.getAllCodeSystemVersionForCodeSystemPid(pid);
		while (versionPids.hasNext()) {
			IdAndPartitionId next = versionPids.next();
			CodeSystemVersionPIDResult versionPidResult = new CodeSystemVersionPIDResult();
			versionPidResult.setCodeSystemVersionPID(next.getId());
			versionPidResult.setPartitionId(next.getPartitionIdValue());
			theDataSink.accept(versionPidResult);
		}

		return RunOutcome.SUCCESS;
	}
}
