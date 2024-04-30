package ca.uhn.fhir.jpa.binstore;

import ca.uhn.fhir.jpa.binary.svc.NullBinaryStorageSvcImpl;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

public class NullBinaryStorageSvcImplTest {

	private final NullBinaryStorageSvcImpl mySvc = new NullBinaryStorageSvcImpl();

	@Test
<<<<<<< HEAD
	public void shouldStoreBlob() {
		assertThat(mySvc.shouldStoreBlob(1, new IdType("Patient/2"), "application/json")).isFalse();
	}

	@Test
	public void storeBlob() {
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> mySvc.storeBlob(null, null, null, null, null));
	}

	@Test
	public void fetchBlobDetails() {
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> mySvc.fetchBlobDetails(null, null));
	}

	@Test
	public void writeBlob() {
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> mySvc.writeBlob(null, null, null));
	}

	@Test
	public void expungeBlob() {
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> mySvc.expungeBlob(null, null));
	}

	@Test
	public void fetchBlob() {
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> mySvc.fetchBlob(null, null));
	}

	@Test
	public void newBlobId() {
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> mySvc.newBlobId());
=======
	public void shouldStoreBinaryContent() {
		assertFalse(mySvc.shouldStoreBinaryContent(1, new IdType("Patient/2"), "application/json"));
	}

	@Test
	public void storeBinaryContent() {
		assertThrows(UnsupportedOperationException.class, () -> mySvc.storeBinaryContent(null, null, null, null, null));
	}

	@Test
	public void fetchBinaryContentDetails() {
		assertThrows(UnsupportedOperationException.class, () -> mySvc.fetchBinaryContentDetails(null, null));
	}

	@Test
	public void writeBinaryContent() {
		assertThrows(UnsupportedOperationException.class, () -> mySvc.writeBinaryContent(null, null, null));
	}

	@Test
	public void expungeBinaryContent() {
		assertThrows(UnsupportedOperationException.class, () -> mySvc.expungeBinaryContent(null, null));
	}

	@Test
	public void fetchBinaryContent() {
		assertThrows(UnsupportedOperationException.class, () -> mySvc.fetchBinaryContent(null, null));
	}

	@Test
	public void newBinaryContentId() {
		assertThrows(UnsupportedOperationException.class, () -> mySvc.newBinaryContentId());
>>>>>>> master
	}
}
