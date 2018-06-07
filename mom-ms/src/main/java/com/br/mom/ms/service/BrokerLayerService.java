package com.br.mom.ms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.mom.ms.common.enums.MOMType;
import com.br.mom.ms.common.enums.ResultStatus;
import com.br.mom.ms.common.enums.Status;
import com.br.mom.ms.model.Producer;
import com.br.mom.ms.model.ProducerPermission;
import com.br.mom.ms.model.ProducerStatus;
import com.br.mom.ms.model.Queue;

/**
 *
 * @author xin.cao@100credit.com
 */
@Service
public class BrokerLayerService {

	@Autowired
	private QueueService queueService;
	@Autowired
	private ProducerService producerService;
	@Autowired
	private ProducerStatusService producerStatusService;
	private ProducerPermission notExist = new ProducerPermission(Status.NOT_EXIST.getV(), false,
			false, false, false);
	private ProducerPermission notPermission = new ProducerPermission(Status.NOT_PERMISSION.getV(),
			false, false, false, false);

	public ProducerPermission checkProducerPermission(String name, String destinationName) {
		Queue queue = new Queue();
		queue.setName(destinationName);
		queue = queueService.selectSelective(queue);
		if (queue == null) {
			return notExist;
		}
		List<Producer> producers = producerService.getProducerByName(name);
		Producer producer = null;
		for (Producer p : producers) {
			if (p.getQueueId() == queue.getId()) {
				producer = p;
			}
		}
		if (producer == null) {
			return notPermission;
		}
		ProducerPermission permission = new ProducerPermission();
		if (queue.getStatus() == ResultStatus.YES.getNo()) {
			permission.setStatus(Status.DEBUG.getV());
		} else {
			permission.setStatus(Status.OK.getV());
		}
		MOMType momt = MOMType.fromInt(producer.getMomType());
//		if (MOMType.isActiveMq(momt)) {
//			permission.setActiveMqOk(true);
//		}
		if (MOMType.isKafka(momt)) {
			permission.setKafkaOk(true);
		}
		if (MOMType.isKafka2(momt)) {
			permission.setKafkaOk2(true);
		}
		if (MOMType.isRedis(momt)) {
			permission.setRedisOk(true);
		}
//		if (MOMType.isAlarm(momt)) {
//			permission.setAlarmOk(true);
//		}
		if (Switch.COUNTER) {
			ProducerStatus producerStatus = new ProducerStatus();
			producerStatus.setProducerId(producer.getId());
			producerStatus.setEnqueue(1L);
			producerStatusService.addEnqueue(producerStatus);
		}
		return permission;
	}

}
