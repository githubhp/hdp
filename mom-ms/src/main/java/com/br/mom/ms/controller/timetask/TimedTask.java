package com.br.mom.ms.controller.timetask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.br.mom.ms.common.enums.MOMType;
import com.br.mom.ms.common.enums.PersistentType;
import com.br.mom.ms.controller.monitor.ConsumerStatusController;
import com.br.mom.ms.model.Consumer;
import com.br.mom.ms.model.ConsumerOffset;
import com.br.mom.ms.model.ConsumerStatus;
import com.br.mom.ms.model.ConsumerThreshold;
import com.br.mom.ms.model.Producer;
import com.br.mom.ms.model.ProducerStatus;
import com.br.mom.ms.model.Queue;
import com.br.mom.ms.service.ConsumerOffsetService;
import com.br.mom.ms.service.ConsumerService;
import com.br.mom.ms.service.ConsumerStatusService;
import com.br.mom.ms.service.ConsumerThresholdService;
import com.br.mom.ms.service.ProducerService;
import com.br.mom.ms.service.ProducerStatusService;
import com.br.mom.ms.service.QueueService;
import com.br.mom.ms.zk.BrCuratorClient;
import com.br.mom.ms.zk.KafkaBrokerClient;

@Service
public class TimedTask {
	private final static Logger logger = LoggerFactory.getLogger(ConsumerStatusController.class);

	@Autowired
	private QueueService queueService;
	@Autowired
	private ConsumerService consumerService;
	@Autowired
	private ProducerService producerService;
	@Autowired
	private ConsumerOffsetService consumerOffsetService;
	@Autowired
	private ConsumerStatusService consumerStatusService;
	@Autowired
	private ProducerStatusService producerStatusService;
	@Autowired
	private ConsumerThresholdService consumerThresholdService;
	@Autowired
	private BrCuratorClient brCuratorClient;
	private Map<Integer, String> zkPathCache = new HashMap<Integer, String>();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// 定时0点更新昨天消费的消息数...
	@Scheduled(cron = "0 0 0 * * ?")
	public void timeTask_1() {
		logger.info("定时存储昨天消费的消息数...");
		// Calendar calendar_1 = Calendar.getInstance();
		// calendar_1.add(Calendar.DAY_OF_MONTH, 1);
		// calendar_1.set(Calendar.HOUR_OF_DAY, 0);
		// calendar_1.set(Calendar.MINUTE, 5);
		// calendar_1.set(Calendar.SECOND, 0);
		// 每天0点更新consumerOffset数据库
		List<Consumer> consumers = consumerService.selectAllSelective(null);
		for (Consumer consumer : consumers) {
			if (consumer.getMomType() != 2) {
				continue;
			}

			// 根据Consumer的id和updateTime查询某一天最后插入的一条consumerStatus记录
			ConsumerStatus consumerStatus = new ConsumerStatus();
			consumerStatus.setConsumerId(consumer.getId());

			Calendar calendar = Calendar.getInstance();
			// SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd
			// HH:mm:ss");

			// 查询昨天插入的最后一条consumerStatus记录
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			// String date_1 = dateFormat.format(calendar.getTime());
			consumerStatus.setUpdateTime(dateFormat.format(calendar.getTime()));

			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			// String date_11 = dateFormat.format(calendar.getTime());
			consumerStatus.setCreateTime(dateFormat.format(calendar.getTime()));

			ConsumerStatus consumerStatus_1 = consumerStatusService.selectByConsumerIdAndTime(consumerStatus);

			// 查询前天插入的最后一条consumerStatus记录
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			// String date_22 = dateFormat.format(calendar.getTime());
			consumerStatus.setCreateTime(dateFormat.format(calendar.getTime()));

			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			// String date_2 = dateFormat.format(calendar.getTime());
			consumerStatus.setUpdateTime(dateFormat.format(calendar.getTime()));

			ConsumerStatus consumerStatus_2 = consumerStatusService.selectByConsumerIdAndTime(consumerStatus);

			// 根据昨天和前天的最后一条记录计算昨天的consumerOffset插入consumer_offset表中
			ConsumerOffset consumerOffset = new ConsumerOffset();
			try {
				consumerOffset.setOffset(consumerStatus_1.getDequeue() - consumerStatus_2.getDequeue());
			} catch (NullPointerException e) {
				continue;
			}

			consumerOffset.setConsumerId(consumerStatus_1.getConsumerId());
			if (consumerStatus_1.getUpdateTime() == null || consumerStatus_2.getUpdateTime() == null) {
				continue;
			}
			consumerOffset.setCreateTime(consumerStatus_1.getUpdateTime());

			// Date newtime = new Date();
			consumerOffset.setUpdateTime(String.format(dateFormat.format(new Date())));
			consumerOffsetService.insert(consumerOffset);
			calendar = null;
			consumerOffset = null;
		}
		consumers = null;
		logger.info("存储消息数完成...");
	}

	/*
	 * 定时5分钟任务，向数据库推送获取的offset
	 * (zookeeper对应consumer_status,kafka对应producer_status)
	 */
	@Scheduled(cron = "0 0/10 * * * ?")
	public void timeTask_2() {
		// 遍历topic，绑定主题id和主题
		List<Queue> queues = queueService.selectAllSelective(null);
		Map<Integer, Queue> idAndQueue = new HashMap<Integer, Queue>();
		for (Queue q : queues) {
			idAndQueue.put(q.getId(), q);
		}
		// 遍历consumer，绑定消费者id和消费者
		List<Consumer> consumers = consumerService.selectAllSelective(null);
		Map<Integer, Consumer> idAndConsumer = new HashMap<Integer, Consumer>();
		for (Consumer p : consumers) {
			idAndConsumer.put(p.getId(), p);
		}
		// 遍历producer，绑定生产者id和生产者
		List<Producer> producers = producerService.selectAllSelective(null);
		Map<Integer, Producer> idAndProducer = new HashMap<Integer, Producer>();
		for (Producer p : producers) {
			idAndProducer.put(p.getId(), p);
		}
		// 获取所有的consumerStatus
		List<ConsumerStatus> consumerStatus = consumerStatusService.selectSelective(null);
		for (ConsumerStatus consumerstatus : consumerStatus) {
			// 获取相应的消费者
			Consumer consumer = idAndConsumer.get(consumerstatus.getConsumerId());
			// 如果消费者已停用，跳过本次循环
			if (consumer == null) {
				continue;
			}
			// 获取中间件类型为kafka的消费者
			if (consumer.getMomType().equals(MOMType.KAFKA.getV())) {
				try {
					if (!zkPathCache.containsKey(consumer.getId())) {
						// 获取消费者对应的主题
						Queue queue = idAndQueue.get(consumer.getQueueId());
						if(queue!=null) {
							String topic = queue.getName();
							// 获取消费者的持久化类型
							String persistentType = PersistentType.fromInt(consumer.getPersistentType()).getName();
							if (persistentType.equals("HADOOP")) {
								persistentType = "HDFS";
							}
							// 获取zookeeper的offset值路径
							String path = "/consumers/" + topic + "_" + persistentType + "/offsets/" + topic;
							zkPathCache.put(consumer.getId(), path);
						}
					}
					String path = zkPathCache.get(consumer.getId());
					if(path!=null) {
						// 保存出队数
						consumerstatus.setDequeue(brCuratorClient.getAllOffset(path));
						// 保存分区信息
						consumerstatus
								.setPartitionData(brCuratorClient.getOffset(path).toString());
						// 保存更新时间
						// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd
						// HH:mm:ss");
						// Date newtime = new Date();
						consumerstatus.setUpdateTime(String.format(dateFormat.format(new Date())));
						// 插入该条数据到数据库中
						consumerStatusService.insert(consumerstatus);
					}
				} catch (Exception e) {
					logger.info("insert consumerStatus error:", e);
				}
				consumer = null;
			}
		}
		// 获取所有的producerStatus
		List<ProducerStatus> producerStatus = producerStatusService.selectSelective(null);
		for (ProducerStatus producerstatus : producerStatus) {
			// 获取相应的生产者
			Producer producer = idAndProducer.get(producerstatus.getProducerId());
			// 如果生产者已停用，跳过本次循环
			if (producer == null) {
				continue;
			}
			// 获取中间件类型为kafka的生产者
			if (producer.getMomType().equals(MOMType.KAFKA.getV())) {
				try {
					// 获取对应主题的分区信息
					Queue queue = idAndQueue.get(producer.getQueueId());
					if(queue!=null) {
						Map<Integer, Long> partitionData = KafkaBrokerClient
								.getKafkaOffset(queue.getName());
						// 保存分区信息
						producerstatus.setPartitionData(partitionData.toString());
						// 保存入队数
						Long enqueue = 0L;
						for (Entry<Integer, Long> enq : partitionData.entrySet()) {
							enqueue += enq.getValue();
						}
						producerstatus.setEnqueue(enqueue);
						// 保存更新时间
						// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd
						// HH:mm:ss");
						// Date newtime = new Date();
						producerstatus.setUpdateTime(String.format(dateFormat.format(new Date())));
						// 插入该条数据到数据库中
						producerStatusService.insert(producerstatus);
						enqueue = null;
					}
				} catch (Exception e) {
					logger.error("insert producerStatus error:", e);
				}
				producer = null;
			}
		}
		queues = null;
		consumers = null;
		producers = null;
		idAndQueue = null;
		idAndConsumer = null;
		idAndProducer = null;
		consumerStatus = null;
		producerStatus = null;
	}

	@PostConstruct
	private void init() {
		logger.info("zk timed task has been start...");
		// 如果mom后台停止较长时间后重新启动，消费者有可能变化，因此需要初始化消费端监控中的消费者数
		List<ConsumerThreshold> consumerThresholds = consumerThresholdService.selectAllSelective(null);
		for (ConsumerThreshold consumerThreshold : consumerThresholds) {
			try {
				String consumerNumberPath = "/consumers/" + consumerThreshold.getGroupName() + "/ids";
				List<String> child = brCuratorClient.getChild(consumerNumberPath);
				StringBuffer consumerNumber = new StringBuffer();
				for (String s : child) {
					consumerNumber.append(s);
					consumerNumber.append("\n");
				}
				if (consumerNumber.toString().length() == 0) {
					continue;
				}
				if (consumerNumber.toString().equals(consumerThreshold.getConsumerNumber())) {
					continue;
				}
				consumerThreshold.setConsumerNumber(consumerNumber.toString());
				// 设置状态
				if (child.size() == Integer.valueOf(consumerThreshold.getThreshold())) {
					consumerThreshold.setStatus("1");
				} else {
					consumerThreshold.setStatus("0");
				}
				// 设置更新时间
				// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd
				// HH:mm:ss");
				// Date newtime = new Date();
				// String time = String.format(dateFormat.format(new Date()));
				consumerThreshold.setUpdateTime(String.format(dateFormat.format(new Date())));

				consumerThresholdService.updateByPrimaryKeySelective(consumerThreshold);
			} catch (Exception e) {
				logger.error("init topic " + consumerThreshold.getQueueName() + " ConsumerNumber error:", e);
			}
		}
		logger.info("init the consumerNumbers success!");
	}

}
