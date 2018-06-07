package com.br.mom.ms.common.enums;

/**
 *
 * @author xin.cao@100credit.com
 */
public enum MOMType {

	// ACTIVEMQ(1, "ACTIVEMQ"),
	KAFKA(1 << 1, "KAFKA"),
	REDIS(1 << 2, "REDIS"), 
	// ALARM(1 << 3, "ALARM");
	KAFKA2(1 << 4, "KAFKA2");
	// ACTIVEMQ_ALARM(ACTIVEMQ.v | ALARM.v, "ACTIVEMQ和ALARM"),
	// ACTIVEMQ_KAFKA(ACTIVEMQ.v | KAFKA.v, "ACTIVEMQ和KAFKA"),
	// ACTIVEMQ_KAFKA_REDIS(ACTIVEMQ.v | KAFKA.v | REDIS.v,
	// "ACTIVEMQ和KAFKA和REDIS");

	private int v;
	private String name;

	private MOMType(int v, String name) {
		this.v = v;
		this.name = name;
	}

	// public static boolean isActiveMq(MOMType momt) {
	// return (momt.v & ACTIVEMQ.v) > 0;
	// }

	public static boolean isKafka(MOMType momt) {
		return (momt.v & KAFKA.v) > 0;
	}

	public static boolean isRedis(MOMType momt) {
		return (momt.v & REDIS.v) > 0;
	}

	public static boolean isKafka2(MOMType momt) {
		return (momt.v & KAFKA2.v) > 0;
	}

	// public static boolean isAlarm(MOMType momt) {
	// return (momt.v & ALARM.v) > 0;
	// }

	public static MOMType fromInt(int v) {
		for (MOMType m : values()) {
			if (m.getV() == v) {
				return m;
			}
		}
		return null;
	}

	public int getV() {
		return v;
	}

	public String getName() {
		return name;
	}

}
