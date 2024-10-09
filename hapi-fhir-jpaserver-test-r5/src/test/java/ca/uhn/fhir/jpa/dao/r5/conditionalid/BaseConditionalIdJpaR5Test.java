package ca.uhn.fhir.jpa.dao.r5.conditionalid;

import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class BaseConditionalIdJpaR5Test extends BaseJpaR5Test {

	public static final String PARTITION_NAME_1 = "Partition_1";
	public static final String PARTITION_NAME_2 = "Partition_2";
	public static final int PARTITION_1 = 1;
	public static final int PARTITION_2 = 2;

	protected final PartitionSelectorInterceptor myPartitionSelectorInterceptor = new PartitionSelectorInterceptor();

	@Autowired
	private IPartitionLookupSvc myPartitionConfigSvc;

	@Override
	@AfterEach
	protected void afterResetInterceptors() {
		super.afterResetInterceptors();
		myPartitionSettings.setPartitioningEnabled(false);
		myInterceptorRegistry.unregisterInterceptor(myPartitionSelectorInterceptor);
	}

	protected void registerPartitionInterceptorAndCreatePartitions() {
		assertFalse(myInterceptorRegistry.hasHooks(Pointcut.STORAGE_PARTITION_IDENTIFY_READ), ()->myInterceptorRegistry.getAllRegisteredInterceptors().toString());
		myInterceptorRegistry.registerInterceptor(myPartitionSelectorInterceptor);

		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(PARTITION_1).setName(PARTITION_NAME_1), null);
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(PARTITION_2).setName(PARTITION_NAME_2), null);

		// Load to pre-cache and avoid adding SQL queries
		myPartitionConfigSvc.getPartitionById(PARTITION_1);
		myPartitionConfigSvc.getPartitionById(PARTITION_2);
		myPartitionConfigSvc.getPartitionByName(PARTITION_NAME_1);
		myPartitionConfigSvc.getPartitionByName(PARTITION_NAME_2);
	}

}
