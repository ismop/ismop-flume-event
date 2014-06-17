package pl.cyfronet.ismop.flume.events;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.io.JsonDecoder;
import org.apache.avro.io.JsonEncoder;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import pl.cyfronet.ismop.flume.events.MeasurmentEvent;
import pl.cyfronet.ismop.flume.events.MeasurmentEvent.Builder;

public class MeasurmentEventTest {

	public static void main(String[] args) throws IOException {
		
		byte[] byteArray = write();
		
		read(byteArray);
	
	}

	private static void read(byte[] byteArray) throws IOException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
		
		JsonDecoder jsonDecoder = new DecoderFactory().jsonDecoder(MeasurmentEvent.getClassSchema(), inputStream);
		
		SpecificDatumReader<MeasurmentEvent> datumReader = new SpecificDatumReader<MeasurmentEvent>(MeasurmentEvent.class);
		
		MeasurmentEvent reuse = new MeasurmentEvent();
		
		datumReader.read(reuse, jsonDecoder);
		
		System.out.println(reuse);
	}

	private static byte[] write() throws IOException {
		Builder newBuilder = MeasurmentEvent.newBuilder();
		
		ByteBuffer bb = ByteBuffer.wrap("my_payload".getBytes());
		HashMap<CharSequence, CharSequence> metadata = new HashMap<CharSequence, CharSequence>();
		metadata.put("id", "123");
		
		newBuilder.setPayload(bb);
		newBuilder.setMetadata(metadata);
		
		MeasurmentEvent event = newBuilder.build();
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		JsonEncoder jsonEncoder = new EncoderFactory().jsonEncoder(MeasurmentEvent.getClassSchema(), stream);
		
		SpecificDatumWriter<MeasurmentEvent> datumWriter = new SpecificDatumWriter<MeasurmentEvent>(MeasurmentEvent.class);
		datumWriter.write(event, jsonEncoder);
		jsonEncoder.flush();

		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}

}