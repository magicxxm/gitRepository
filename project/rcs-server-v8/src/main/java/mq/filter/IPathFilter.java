package mq.filter;


import java.util.Map;

/**
 * Created by Laptop-6 on 2017/9/5.
 */
public interface IPathFilter {
    void doFilter(Map<String, Object> message);
}
