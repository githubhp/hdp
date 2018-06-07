package com.br.mom.ms.controller.monitor;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.br.mom.ms.common.enums.MOMType;
import com.br.mom.ms.bean.Page;
import com.br.mom.ms.model.Producer;
import com.br.mom.ms.model.ProducerStatus;
import com.br.mom.ms.model.Queue;
import com.br.mom.ms.service.ProducerService;
import com.br.mom.ms.service.ProducerStatusService;
import com.br.mom.ms.service.QueueService;
import com.br.mom.ms.util.DateUtil;

/**
 * @author xin.cao@100credit.com
 */
@Controller
@RequestMapping("/monitor")
public class ProducerStatusController {
	@Autowired
	private QueueService queueService;
	@Autowired
	private ProducerService producerService;
	@Autowired
	private ProducerStatusService producerStatusService;

	/**
	 * 生产者状态配置
	 *
	 * @param httpServletRequest
	 * @param model
	 * @return
	 * @throws java.io.IOException
	 */
	@RequestMapping(value = "/producer_status")
	public String producerStatusAction(HttpServletRequest httpServletRequest, Model model) throws IOException {
		
		List<Producer> producers = producerService.selectAllSelective(null);
		Map<Integer, Producer> idAndProducer = new HashMap<Integer, Producer>();
		for (Producer p : producers) {
			idAndProducer.put(p.getId(), p);
		}
		model.addAttribute("idAndProducer", idAndProducer);

		List<Queue> queues = queueService.selectAllSelective(null);
		Map<Integer, Queue> idAndQueue = new HashMap<Integer, Queue>();
		for (Queue q : queues) {
			idAndQueue.put(q.getId(), q);
		}
		model.addAttribute("idAndQueue", idAndQueue);
		
		// String action = httpServletRequest.getParameter("action");
		String pageNow = httpServletRequest.getParameter("pageNumber");
		String pageSize = httpServletRequest.getParameter("customPageSize");
		//分页查询
		Page<ProducerStatus> page = new Page<ProducerStatus>();
		if (pageNow != null) {
			page.setPageNow(Integer.valueOf(pageNow));
		}
		if (pageSize != null) {
			page.setPageSize(Integer.valueOf(pageSize));
		}

		List<ProducerStatus> producerStatuss = producerStatusService.selectByPage(page.getStartIndex(),
				page.getPageSize());
		//过滤producer已经删除的记录
		Iterator<ProducerStatus> iterator = producerStatuss.iterator();
		while (iterator.hasNext()) {
			try {
				idAndProducer.get(iterator.next().getProducerId()).getName();
			} catch (ConcurrentModificationException | NullPointerException e) {
				iterator.remove();
			}
		}
		//过滤producer的中间件为activemq的记录,如果中间件类型除了activemq外还有其他类型则保留
		Iterator<ProducerStatus> iterator_2 = producerStatuss.iterator();
		while(iterator_2.hasNext()){
			if(idAndProducer.get(iterator_2.next().getProducerId()).getMomType()==1){
				iterator_2.remove();
			}
		}
		int totalRecords = producerStatusService.selectTotalRecords();
		page.setTotalRecords(totalRecords);
		page.setList(producerStatuss);
		model.addAttribute("page", page);
		
		model.addAttribute("producerStatuss", producerStatuss);
		model.addAttribute("MOMType", MOMType.KAFKA);
		model.addAttribute("DateUtil", DateUtil.getInstance());
		return "monitor/producer_status";
	}
}
