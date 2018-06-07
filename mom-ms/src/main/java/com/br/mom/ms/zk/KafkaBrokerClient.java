package com.br.mom.ms.zk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.br.mom.ms.util.PropertiesUtil;

import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.TopicAndPartition;
import kafka.javaapi.OffsetRequest;
import kafka.javaapi.OffsetResponse;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.consumer.SimpleConsumer;

@Service
public class KafkaBrokerClient {

	private static int port;
	private static String brokerId;
	private static List<String> seeds;
	private static List<String> topics;
	private static OffsetRequest request;
	private static OffsetResponse response;
	private static TopicMetadataRequest req;
	private static Map<Integer, Long> offsetMap;
	private static List<TopicMetadata> metaData;
	private static SimpleConsumer simpleconsumer;
	private static PartitionOffsetRequestInfo por;
	private static TopicAndPartition topicAndPartition;
	private static kafka.javaapi.TopicMetadataResponse resp;
	private static HashMap<Integer, PartitionMetadata> metadatas;
	private static Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo;
	private static Logger logger = LoggerFactory.getLogger(KafkaBrokerClient.class);

	/*
	 * 获取topic的每个分区的offset
	 */
	public static Map<Integer, Long> getKafkaOffset(String topic) {
		if (seeds == null || seeds.size() == 0) {
			seeds = new ArrayList<String>();
			brokerId = PropertiesUtil.getStringValue("kafka.broker.id");
			port = Integer.valueOf(PropertiesUtil.getStringValue("kafka.broker.port"));
			if (brokerId.contains(",")) {
				String[] str = brokerId.split(",");
				for (String s : str) {
					seeds.add(s);
				}
			} else {
				seeds.add(brokerId);
			}
		}

		HashMap<Integer, PartitionMetadata> metadatas = findLeader(topic);

		offsetMap = new HashMap<Integer, Long>();

		for (Entry<Integer, PartitionMetadata> entry : metadatas.entrySet()) {

			int partition = entry.getKey();
			String leadBroker = entry.getValue().leader().host();
			String clientName = "Client_" + topic + "_" + partition;
			try {
				simpleconsumer = new SimpleConsumer(leadBroker, port, 100000, 64 * 1024, clientName);

				topicAndPartition = new TopicAndPartition(topic, partition);

				requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();

				por = new PartitionOffsetRequestInfo(kafka.api.OffsetRequest.LatestTime(), 1);

				requestInfo.put(topicAndPartition, por);

				request = new OffsetRequest(requestInfo, kafka.api.OffsetRequest.CurrentVersion(), clientName);

				response = simpleconsumer.getOffsetsBefore(request);

				if (response.hasError()) {
					logger.error("Fetching  offset data from  broker error: " + response.errorCode(topic, partition));
					return offsetMap;
				}

				long[] offsets = response.offsets(topic, partition);
				long readOffset = offsets[0];
				offsetMap.put(partition, readOffset);
				request = null;
				response = null;
			} catch (Exception e) {
				logger.error("Get topic[" + topic + "] offset in partition[" + entry.getKey() + "] error:", e);
			} finally {
				if (simpleconsumer != null) {
					simpleconsumer.close();
					simpleconsumer = null;
				}
				por = null;
				topicAndPartition = null;
				requestInfo = null;
			}
		}
		return offsetMap;
	}

	/*
	 * 定义map存放topic分区leader
	 */
	public static HashMap<Integer, PartitionMetadata> findLeader(String topic) {
		metadatas = new HashMap<Integer, PartitionMetadata>();
		for (String brokerid : seeds) {
			try {
				simpleconsumer = new SimpleConsumer(brokerid, port, 100000, 64 * 1024,
						"leaderLookup" + new Date().getTime());
				topics = Collections.singletonList(topic);
				req = new TopicMetadataRequest(topics);
				resp = simpleconsumer.send(req);
				metaData = resp.topicsMetadata();
				for (TopicMetadata item : metaData) {
					for (PartitionMetadata part : item.partitionsMetadata()) {
						metadatas.put(part.partitionId(), part);
					}
				}
			} catch (Exception e) {
				logger.error("Find leader for topic[" + topic + "] in broker[" + brokerid + "] error:", e);
			} finally {
				if (simpleconsumer != null) {
					simpleconsumer.close();
					simpleconsumer = null;
				}
				topics = null;
				resp = null;
				metaData = null;
			}
		}
		return metadatas;
	}
}
