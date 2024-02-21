package uk.ncl.giacomobergami.components.iot_protocol;

public class CoAPProtocol extends IoTProtocol {

	private static final float BATTERY_DRAINAGE_RATE=1.00f;
	private static final float TRANSIMISON_SPEED=3.00f;
	
	public CoAPProtocol() {
		super("CoAP", BATTERY_DRAINAGE_RATE, TRANSIMISON_SPEED);
	}
}