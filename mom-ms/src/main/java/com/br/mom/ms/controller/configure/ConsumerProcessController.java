package com.br.mom.ms.controller.configure;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.br.mom.ms.model.Consumer;
import com.br.mom.ms.model.ConsumerBroker;
import com.br.mom.ms.service.ConsumerBrokerService;
import com.br.mom.ms.service.ConsumerService;

/**
 *
 * @author xin.cao@100credit.com
 */
@Controller
@RequestMapping("/configure")
public class ConsumerProcessController {
@Autowired
private ConsumerService consumerService;
@Autowired
private ConsumerBrokerService consumerBrokerService;
//    private static final Logger logger = LoggerFactory.getLogger(ConsumerProcessController.class);

    /**
     * 消费者状态配置
     *
     * @param httpServletRequest
     * @param model
     * @return
     * @throws java.io.IOException
     */
    @RequestMapping(value = "/consumer_process_create")
    public String createAction(HttpServletRequest httpServletRequest, Model model) throws IOException {
        
//        List<Consumer> consumers = databaseService.selectAllConsumerSelective(null);
        List<Consumer> consumers = consumerService.selectAllSelective(null);
        Set<String> names = new HashSet<String>();
        if (consumers != null) {
            for (Consumer p : consumers) {
                names.add(p.getName());
            }
        }
//        List<ConsumerBroker> consumerBrokers = databaseService.selectAllConsumerBrokerSelective(null);
        List<ConsumerBroker> consumerBrokers = consumerBrokerService.selectAllSelective(null);
        model.addAttribute("names", names);
        model.addAttribute("consumerBrokers", consumerBrokers);
        return "/configure/consumer_process_create";
    }

    @RequestMapping(value = "/consumer_process_list")
    public String listAction(HttpServletRequest httpServletRequest, Model model) throws IOException {
    	//FIXME TODO 查询所有的rpc的信息并进行监控
    	
       /* DatabaseServiceClient databaseService = DatabaseServiceClient.getInstance();
        List<ConsumerBroker> consumerBrokers = databaseService.selectAllConsumerBrokerSelective(null);
        List<ConsumerProcess> consumerProcesses = new ArrayList<ConsumerProcess>();
        try {
            if (consumerBrokers != null && !consumerBrokers.isEmpty()) {
                for (ConsumerBroker consumerBroker : consumerBrokers) {
                    NettyTransceiver client = new NettyTransceiver(new InetSocketAddress(consumerBroker.getIp(), Integer.valueOf(consumerBroker.getPort())));
                    TaskMonitorService monitorService = (TaskMonitorService) SpecificRequestor.getClient(TaskMonitorService.class, client);
                    List<Node> nodes = monitorService.wokerInfos("");
                    if (nodes != null) {
                        for (Node node : nodes) {
                            ConsumerProcess consumerProcess = new ConsumerProcess();
                            consumerProcess.setName(String.valueOf(node.getName()));
                            consumerProcess.setIp(consumerBroker.getIp());
                            consumerProcess.setPid(node.getPid());
                            consumerProcess.setTime((int) (System.currentTimeMillis() - node.getTimestamp()));
                            consumerProcesses.add(consumerProcess);
                        }
                    }
                    client.close();
                }
            }
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        model.addAttribute("consumerProcesses", consumerProcesses);
        return "/configure/consumer_process_list";*/
    	return "";
    }

    @RequestMapping(value = "/consumer_process_boot")
    public String bootAction(HttpServletRequest httpServletRequest, Model model) throws IOException {
    	//FIXME TODO 进程刷新
    	
        /*DatabaseServiceClient databaseService = DatabaseServiceClient.getInstance();
        String name = httpServletRequest.getParameter("name");
        String consumerBrokerId = httpServletRequest.getParameter("consumer_broker_id");
        if (name == null || name.isEmpty()) {
            return "redirect:/configure/consumer_process_list";
        }
        if (consumerBrokerId == null || consumerBrokerId.isEmpty()) {
            return "redirect:/configure/consumer_process_list";
        }
        String c = String.format("nohup /usr/local/flume/bin/flume-ng agent -DCONSUMER_NAME=%s --conf /usr/local/flume/conf --conf-file /usr/local/flume/conf/mom_to_mom.conf  --name A -Dflume.root.logger=INFO,console &", name.trim());
        ConsumerBroker consumerBroker = new ConsumerBroker();
        consumerBroker.setId(Integer.valueOf(consumerBrokerId));
        consumerBroker = databaseService.selectConsumerBrokerSelective(consumerBroker);
        try {
            NettyTransceiver client = new NettyTransceiver(new InetSocketAddress(consumerBroker.getIp(), Integer.valueOf(consumerBroker.getPort())));
            TaskMonitorService monitorService = (TaskMonitorService) SpecificRequestor.getClient(TaskMonitorService.class, client);
            Command command = new Command();
            command.setHeaders("shell 命令");
            command.setBody(new Utf8(c));
            String output = String.valueOf(monitorService.exec(command));
            logger.info(output);
            client.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "redirect:/configure/consumer_process_list";*/
    	
    	return "";
    }
}
