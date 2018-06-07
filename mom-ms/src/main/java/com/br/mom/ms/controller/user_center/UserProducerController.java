package com.br.mom.ms.controller.user_center;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.br.mom.ms.model.Producer;
import com.br.mom.ms.model.Queue;
import com.br.mom.ms.model.User;
import com.br.mom.ms.model.UserProducer;
import com.br.mom.ms.service.ProducerService;
import com.br.mom.ms.service.QueueService;
import com.br.mom.ms.service.UserProducerService;
import com.br.mom.ms.service.UserService;

/**
 *
 * @author xin.cao@100credit.com
 */
@Controller
@RequestMapping("/user_center")
public class UserProducerController {
@Autowired
private UserService userService;
@Autowired
private QueueService queueService;
@Autowired
private ProducerService producerService;
@Autowired
private UserProducerService userProducerService;
    /**
     * 消费者配置
     *
     * @param httpServletRequest
     * @param model
     * @return
     * @throws java.io.IOException
     */
    @RequestMapping(value = "/user_producer")
    public String userProducerAction(HttpServletRequest httpServletRequest, Model model) throws IOException {
//        DatabaseServiceClient databaseService = DatabaseServiceClient.getInstance();
        String action = httpServletRequest.getParameter("action");
        if (action == null || "".equals(action)) {
//            List<User> users = databaseService.selectAllUserSelective(null);
        	List<User> users = userService.selectAllSelective(null);
            Map<Integer, User> idAndUser = new HashMap<Integer, User>();
            for (User user : users) {
                idAndUser.put(user.getId(), user);
            }
//            List<Queue> queues = databaseService.selectAllQueueSelective(null);
            List<Queue> queues = queueService.selectAllSelective(null);
            Map<Integer, Queue> idAndQueue = new HashMap<Integer, Queue>();
            for (Queue queue : queues) {
                idAndQueue.put(queue.getId(), queue);
            }
//            List<Producer> producers = databaseService.selectAllProducerSelective(null);
            List<Producer> producers = producerService.selectAllSelective(null);
            Map<Integer, Producer> idAndProducer = new HashMap<Integer, Producer>();
            for (Producer producer : producers) {
                idAndProducer.put(producer.getId(), producer);
            }
//            List<UserProducer> userProducers = databaseService.selectAllUserProducerSelective(null);
            List<UserProducer> userProducers = userProducerService.selectAllSelective(null);
            model.addAttribute("users", users);
            model.addAttribute("producers", producers);
            model.addAttribute("idAndUser", idAndUser);
            model.addAttribute("idAndQueue", idAndQueue);
            model.addAttribute("idAndProducer", idAndProducer);
            model.addAttribute("userProducers", userProducers);
            return "user_center/user_producer";
        } else if ("save".equals(action)) {
            String[] ids = httpServletRequest.getParameterValues("ids[]");
            if (ids != null) {
                for (String id : ids) {
                    UserProducer userProducer = new UserProducer();
                    String userId = httpServletRequest.getParameter(id + ":userId");
                    String producerId = httpServletRequest.getParameter(id + ":producerId");
                    if (userId == null || producerId == null) {
                        continue;
                    }
                    userProducer.setId(Integer.valueOf(id));
                    userProducer.setUserId(Integer.valueOf(userId));
                    userProducer.setProducerId(Integer.valueOf(producerId));
//                    databaseService.updateUserProducerByPrimaryKeySelective(userProducer);
                    userProducerService.updateByPrimaryKeySelective(userProducer);
                }
            }
            String[] newUserId = httpServletRequest.getParameterValues("new:userId[]");
            String[] newProducerId = httpServletRequest.getParameterValues("new:producerId[]");
            if (newProducerId != null) {
                int len = newProducerId.length;
                for (int i = 0; i < len; i++) {
                    if (newUserId[i] == null || newProducerId[i] == null) {
                        continue;
                    }
                    UserProducer userProducer = new UserProducer();
                    userProducer.setUserId(Integer.valueOf(newUserId[i]));
                    userProducer.setProducerId(Integer.valueOf(newProducerId[i]));
//                    databaseService.insertUserProducerSelective(userProducer);
                    userProducerService.insertSelective(userProducer);
                }
            }
        } else if ("delete".equals(action)) {
            String[] selectedIds = httpServletRequest.getParameterValues("selected_ids[]");
            if (selectedIds != null) {
                for (String selectedId : selectedIds) {
//                    databaseService.deleteUserProducerByPrimaryKey(Integer.valueOf(selectedId));
                	userProducerService.deleteByPrimaryKey(Integer.valueOf(selectedId));
                }
            }
        }
        return "redirect:/user_center/user_producer";
    }
}
