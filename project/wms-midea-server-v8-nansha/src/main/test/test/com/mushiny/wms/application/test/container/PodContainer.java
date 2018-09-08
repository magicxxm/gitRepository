package test.com.mushiny.wms.application.test.container;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import test.com.mushiny.wms.application.test.bean.Pod;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: wms-midea-server
 * @description: 驮料 空料车 管理器
 * @author: mingchun.mu@mushiny.com
 * @create: 2018-07-09 20:35
 **/
@Component
@Scope("singleton")
public class PodContainer {

    private Map<Integer, Pod> container = new ConcurrentHashMap<>();

    public Pod removeFirst(){
        if(!CollectionUtils.isEmpty(container)){
            return container.remove(0);
        }
        return null;
    }
    public Pod remove(Integer key){
        if(!CollectionUtils.isEmpty(container)){
            return container.remove(key);
        }
        return null;
    }

    public Pod get(int id){
        return container.get(id);
    }

    public void add(Pod pod){
        if(pod == null){
            return;
        }
        if(contains(pod)){
            return;
        }
        container.put(pod.getId(), pod);
    }

    public boolean contains(Pod pod){
        return contains(pod.getId());
    }
    public boolean contains(int id){
        if(get(id) != null){
            return true;
        }
        return false;
    }


    public boolean isEmpty(){
        return CollectionUtils.isEmpty(container);
    }

    public Map<Integer, Pod> getContainer() {
        return container;
    }
}
